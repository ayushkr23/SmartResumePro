package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import app.Main;
import app.model.ResumeData;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RoleController implements Initializable {
    
    @FXML private ScrollPane roleScrollPane;
    @FXML private FlowPane roleCardsContainer;
    @FXML private Button continueButton;
    @FXML private Label welcomeLabel;
    @FXML private Label selectedRoleLabel;
    @FXML private VBox roleDetailsContainer;
    @FXML private VBox skillsContainer;
    @FXML private VBox tipsContainer;
    @FXML private VBox projectsContainer;
    @FXML private TextArea objectivePreview;
    
    private static final Logger logger = Logger.getLogger(RoleController.class.getName());
    private Map<String, Object> rolesData;
    private String selectedRoleId;
    private Map<String, Object> selectedRoleData;
    private static ResumeData resumeData = new ResumeData();
    private List<VBox> roleCards = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set welcome message
        String currentUser = LoginController.getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser + "! 👋");
        
        // Load role data
        loadRoleData();
        
        // Create role cards
        createRoleCards();
        
        // Initially disable continue button
        continueButton.setDisable(true);
        
        // Fallback: if no cards were created, force create default ones
        if (roleCards.isEmpty()) {
            logger.warning("No role cards were created, forcing default data creation");
            createDefaultRoleData();
            createRoleCards();
        }
        
        // Emergency fallback: create simple test cards if still empty
        if (roleCardsContainer.getChildren().isEmpty()) {
            logger.warning("FlowPane still empty, creating emergency test cards");
            createEmergencyCards();
        }
        
        // Final check
        logger.info("Role selector initialized with " + roleCards.size() + " cards");
        logger.info("FlowPane children count: " + roleCardsContainer.getChildren().size());
    }
    
    private void loadRoleData() {
        try (InputStream is = getClass().getResourceAsStream("/data/role_data.json")) {
            if (is != null) {
                String jsonContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                logger.info("JSON content loaded, length: " + jsonContent.length());
                logger.info("JSON preview: " + jsonContent.substring(0, Math.min(200, jsonContent.length())));
                
                rolesData = parseJson(jsonContent);
                
                if (rolesData != null && rolesData.containsKey("roles")) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> roles = (List<Map<String, Object>>) rolesData.get("roles");
                    logger.info("Loaded " + (roles != null ? roles.size() : 0) + " roles from JSON");
                } else {
                    logger.warning("Invalid JSON structure, using default data");
                    createDefaultRoleData();
                }
            } else {
                logger.warning("Role data file not found at /data/role_data.json, using default data");
                createDefaultRoleData();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to load role data, using defaults", e);
            createDefaultRoleData();
        }
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String jsonContent) {
        // Simple JSON parser for our specific structure
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> roles = new ArrayList<>();
        
        // Extract roles array
        Pattern rolesPattern = Pattern.compile("\"roles\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher rolesMatcher = rolesPattern.matcher(jsonContent);
        
        if (rolesMatcher.find()) {
            String rolesContent = rolesMatcher.group(1);
            
            // Split by role objects
            Pattern rolePattern = Pattern.compile("\\{([^{}]*(?:\\{[^{}]*\\}[^{}]*)*)\\}", Pattern.DOTALL);
            Matcher roleMatcher = rolePattern.matcher(rolesContent);
            
            while (roleMatcher.find()) {
                String roleContent = roleMatcher.group(1);
                Map<String, Object> role = parseRoleObject(roleContent);
                if (!role.isEmpty()) {
                    roles.add(role);
                }
            }
        }
        
        result.put("roles", roles);
        return result;
    }
    
    private Map<String, Object> parseRoleObject(String roleContent) {
        Map<String, Object> role = new HashMap<>();
        
        // Extract simple string fields
        role.put("id", extractStringValue(roleContent, "id"));
        role.put("name", extractStringValue(roleContent, "name"));
        role.put("icon", extractStringValue(roleContent, "icon"));
        role.put("description", extractStringValue(roleContent, "description"));
        role.put("objective_template", extractStringValue(roleContent, "objective_template"));
        
        // Extract arrays
        role.put("skills", extractArrayValues(roleContent, "skills"));
        role.put("tips", extractArrayValues(roleContent, "tips"));
        role.put("projects", extractArrayValues(roleContent, "projects"));
        
        return role;
    }
    
    private String extractStringValue(String content, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*?)\"");
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1) : "";
    }
    
    private List<String> extractArrayValues(String content, String key) {
        List<String> values = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        
        if (matcher.find()) {
            String arrayContent = matcher.group(1);
            Pattern itemPattern = Pattern.compile("\"([^\"]*?)\"");
            Matcher itemMatcher = itemPattern.matcher(arrayContent);
            
            while (itemMatcher.find()) {
                values.add(itemMatcher.group(1));
            }
        }
        
        return values;
    }
    
    private void createDefaultRoleData() {
        logger.info("Creating default role data");
        rolesData = new HashMap<>();
        List<Map<String, Object>> roles = new ArrayList<>();
        
        // Software Developer
        Map<String, Object> softwareDev = new HashMap<>();
        softwareDev.put("id", "software_developer");
        softwareDev.put("name", "Software Developer");
        softwareDev.put("icon", "💻");
        softwareDev.put("description", "Build applications, websites, and software solutions");
        softwareDev.put("skills", Arrays.asList("Java", "Python", "JavaScript", "React", "Node.js", "SQL", "Git"));
        softwareDev.put("tips", Arrays.asList("Highlight coding projects with GitHub links", "Show technical skills with specific technologies"));
        softwareDev.put("projects", Arrays.asList("E-commerce Website", "Task Management App", "Weather App"));
        softwareDev.put("objective_template", "Aspiring Software Developer with strong programming fundamentals seeking to contribute to innovative projects.");
        roles.add(softwareDev);
        
        // Data Analyst
        Map<String, Object> dataAnalyst = new HashMap<>();
        dataAnalyst.put("id", "data_analyst");
        dataAnalyst.put("name", "Data Analyst");
        dataAnalyst.put("icon", "📊");
        dataAnalyst.put("description", "Analyze data to derive insights and support decision-making");
        dataAnalyst.put("skills", Arrays.asList("Python", "R", "SQL", "Tableau", "Excel", "Statistics", "Machine Learning"));
        dataAnalyst.put("tips", Arrays.asList("Quantify analysis results with specific metrics", "Show visualization skills"));
        dataAnalyst.put("projects", Arrays.asList("Sales Analysis Dashboard", "Customer Behavior Study", "Market Trend Analysis"));
        dataAnalyst.put("objective_template", "Detail-oriented Data Analyst passionate about transforming raw data into actionable insights.");
        roles.add(dataAnalyst);
        
        // Digital Marketer
        Map<String, Object> digitalMarketer = new HashMap<>();
        digitalMarketer.put("id", "digital_marketer");
        digitalMarketer.put("name", "Digital Marketer");
        digitalMarketer.put("icon", "📱");
        digitalMarketer.put("description", "Create and manage digital marketing campaigns");
        digitalMarketer.put("skills", Arrays.asList("Social Media", "SEO", "Google Analytics", "Content Marketing", "Email Marketing"));
        digitalMarketer.put("tips", Arrays.asList("Show campaign results with metrics", "Highlight social media growth"));
        digitalMarketer.put("projects", Arrays.asList("Social Media Campaign", "SEO Strategy", "Email Newsletter"));
        digitalMarketer.put("objective_template", "Creative Digital Marketer passionate about driving brand growth through innovative online strategies.");
        roles.add(digitalMarketer);
        
        // Business Analyst
        Map<String, Object> businessAnalyst = new HashMap<>();
        businessAnalyst.put("id", "business_analyst");
        businessAnalyst.put("name", "Business Analyst");
        businessAnalyst.put("icon", "📈");
        businessAnalyst.put("description", "Bridge business needs with technology solutions");
        businessAnalyst.put("skills", Arrays.asList("Requirements Analysis", "Process Mapping", "SQL", "Excel", "Business Intelligence"));
        businessAnalyst.put("tips", Arrays.asList("Show process improvements with metrics", "Highlight stakeholder management"));
        businessAnalyst.put("projects", Arrays.asList("Process Optimization", "Requirements Documentation", "Business Intelligence Dashboard"));
        businessAnalyst.put("objective_template", "Analytical Business Analyst focused on optimizing processes and driving business value through data-driven insights.");
        roles.add(businessAnalyst);
        
        // UI/UX Designer
        Map<String, Object> uxDesigner = new HashMap<>();
        uxDesigner.put("id", "ux_designer");
        uxDesigner.put("name", "UI/UX Designer");
        uxDesigner.put("icon", "🎨");
        uxDesigner.put("description", "Design user-friendly interfaces and experiences");
        uxDesigner.put("skills", Arrays.asList("Figma", "Adobe XD", "Prototyping", "User Research", "Wireframing", "Design Systems"));
        uxDesigner.put("tips", Arrays.asList("Create a strong portfolio", "Show design thinking process", "Include user research insights"));
        uxDesigner.put("projects", Arrays.asList("Mobile App Design", "Website Redesign", "Design System", "User Experience Study"));
        uxDesigner.put("objective_template", "Creative UI/UX Designer passionate about crafting intuitive user experiences through thoughtful design and user research.");
        roles.add(uxDesigner);
        
        rolesData.put("roles", roles);
        logger.info("Created " + roles.size() + " default roles");
    }
    
    private void createEmergencyCards() {
        logger.info("Creating emergency role cards");
        roleCardsContainer.getChildren().clear();
        
        // Create simple test cards that are guaranteed to be visible
        String[] roles = {"Software Developer", "Data Analyst", "Digital Marketer", "Business Analyst", "UI/UX Designer"};
        String[] icons = {"💻", "📊", "📱", "📈", "🎨"};
        String[] descriptions = {
            "Build applications and websites",
            "Analyze data for insights", 
            "Create digital marketing campaigns",
            "Bridge business needs with technology",
            "Design user-friendly interfaces"
        };
        
        for (int i = 0; i < roles.length; i++) {
            VBox card = new VBox();
            card.setPrefSize(200, 180);
            card.setMinSize(200, 180);
            card.setMaxSize(200, 180);
            card.setAlignment(Pos.CENTER);
            card.setSpacing(12);
            
            // Strong styling to ensure visibility
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-border-color: #2563eb; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 16; " +
                "-fx-background-radius: 16; " +
                "-fx-padding: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);"
            );
            
            // Icon
            Label icon = new Label(icons[i]);
            icon.setStyle("-fx-font-size: 48px;");
            
            // Name
            Label name = new Label(roles[i]);
            name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
            name.setWrapText(true);
            name.setMaxWidth(180);
            
            // Description
            Label description = new Label(descriptions[i]);
            description.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
            description.setWrapText(true);
            description.setMaxWidth(180);
            
            card.getChildren().addAll(icon, name, description);
            
            // Add click handler
            final String roleId = "emergency_" + i;
            final String roleName = roles[i];
            card.setOnMouseClicked(event -> {
                // Remove selection from all cards
                for (javafx.scene.Node node : roleCardsContainer.getChildren()) {
                    node.getStyleClass().remove("role-card-selected");
                }
                
                // Add selection to clicked card
                card.setStyle(card.getStyle() + "-fx-border-color: #1d4ed8; -fx-border-width: 3;");
                
                selectedRoleId = roleId;
                selectedRoleLabel.setText("Selected: " + roleName);
                continueButton.setDisable(false);
                
                // Update resume data
                resumeData.setSelectedRole(roleName);
                resumeData.setObjective("Aspiring " + roleName + " seeking to contribute to innovative projects while growing professional expertise.");
                
                logger.info("Emergency role selected: " + roleName);
            });
            
            roleCardsContainer.getChildren().add(card);
        }
        
        logger.info("Emergency cards created: " + roleCardsContainer.getChildren().size());
    }
    
    @SuppressWarnings("unchecked")
    private void createRoleCards() {
        roleCardsContainer.getChildren().clear();
        roleCards.clear();
        
        if (rolesData == null) {
            logger.warning("Roles data is null, creating default data");
            createDefaultRoleData();
        }
        
        List<Map<String, Object>> roles = (List<Map<String, Object>>) rolesData.get("roles");
        if (roles == null || roles.isEmpty()) {
            logger.warning("No roles found in data, creating default roles");
            createDefaultRoleData();
            roles = (List<Map<String, Object>>) rolesData.get("roles");
        }
        
        logger.info("Creating " + roles.size() + " role cards");
        
        for (Map<String, Object> role : roles) {
            try {
                VBox roleCard = createRoleCard(role);
                roleCards.add(roleCard);
                roleCardsContainer.getChildren().add(roleCard);
                logger.info("Created card for role: " + role.get("name"));
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to create card for role: " + role.get("name"), e);
            }
        }
        
        logger.info("Total role cards created: " + roleCards.size());
    }
    
    private VBox createRoleCard(Map<String, Object> role) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(12);
        card.setPrefWidth(200);
        card.setPrefHeight(180);
        card.setMinWidth(200);
        card.setMinHeight(180);
        card.setMaxWidth(200);
        card.setMaxHeight(180);
        card.getStyleClass().add("role-card");
        
        // Ensure the card is visible with a fallback background color
        if (card.getBackground() == null) {
            card.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 1; -fx-background-radius: 16; -fx-border-radius: 16;");
        }
        
        // Icon
        Label icon = new Label((String) role.get("icon"));
        icon.getStyleClass().add("role-icon");
        if (icon.getFont() == null) {
            icon.setStyle("-fx-font-size: 48px;");
        }
        
        // Name
        Label name = new Label((String) role.get("name"));
        name.getStyleClass().add("role-name");
        name.setWrapText(true);
        name.setMaxWidth(180);
        if (name.getFont() == null) {
            name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        }
        
        // Description
        Label description = new Label((String) role.get("description"));
        description.getStyleClass().add("role-description");
        description.setWrapText(true);
        description.setMaxWidth(180);
        if (description.getFont() == null) {
            description.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        }
        
        card.getChildren().addAll(icon, name, description);
        
        // Add click handler
        card.setOnMouseClicked(event -> selectRole((String) role.get("id"), role, card));
        
        logger.info("Created role card for: " + role.get("name") + " with size: " + card.getPrefWidth() + "x" + card.getPrefHeight());
        
        return card;
    }
    
    @SuppressWarnings("unchecked")
    private void selectRole(String roleId, Map<String, Object> roleData, VBox clickedCard) {
        // Remove selection from all cards
        for (VBox card : roleCards) {
            card.getStyleClass().remove("role-card-selected");
        }
        
        // Add selection to clicked card
        clickedCard.getStyleClass().add("role-card-selected");
        
        selectedRoleId = roleId;
        selectedRoleData = roleData;
        
        // Update resume data
        resumeData.setSelectedRole((String) roleData.get("name"));
        resumeData.setObjective((String) roleData.get("objective_template"));
        
        // Update UI
        selectedRoleLabel.setText("Selected: " + roleData.get("name"));
        continueButton.setDisable(false);
        
        // Show role details
        showRoleDetails(roleData);
        
        logger.info("Role selected: " + roleData.get("name"));
    }
    
    @SuppressWarnings("unchecked")
    private void showRoleDetails(Map<String, Object> roleData) {
        // Clear existing content
        skillsContainer.getChildren().clear();
        tipsContainer.getChildren().clear();
        projectsContainer.getChildren().clear();
        
        // Show skills
        Label skillsTitle = new Label("💡 Recommended Skills:");
        skillsTitle.getStyleClass().add("label-subheading");
        skillsContainer.getChildren().add(skillsTitle);
        
        FlowPane skillsFlow = new FlowPane();
        skillsFlow.setHgap(8);
        skillsFlow.setVgap(8);
        
        List<String> skills = (List<String>) roleData.get("skills");
        if (skills != null) {
            for (String skill : skills) {
                Label skillTag = new Label(skill);
                skillTag.getStyleClass().add("skill-tag");
                skillsFlow.getChildren().add(skillTag);
            }
        }
        skillsContainer.getChildren().add(skillsFlow);
        
        // Show tips
        Label tipsTitle = new Label("💡 Resume Tips:");
        tipsTitle.getStyleClass().add("label-subheading");
        tipsContainer.getChildren().add(tipsTitle);
        
        List<String> tips = (List<String>) roleData.get("tips");
        if (tips != null) {
            for (String tip : tips) {
                Label tipLabel = new Label("• " + tip);
                tipLabel.getStyleClass().add("label");
                tipLabel.setWrapText(true);
                tipLabel.setMaxWidth(300);
                tipsContainer.getChildren().add(tipLabel);
            }
        }
        
        // Show project suggestions
        Label projectsTitle = new Label("🚀 Project Ideas:");
        projectsTitle.getStyleClass().add("label-subheading");
        projectsContainer.getChildren().add(projectsTitle);
        
        List<String> projects = (List<String>) roleData.get("projects");
        if (projects != null) {
            for (String project : projects) {
                Label projectLabel = new Label("• " + project);
                projectLabel.getStyleClass().add("label");
                projectLabel.setWrapText(true);
                projectLabel.setMaxWidth(300);
                projectsContainer.getChildren().add(projectLabel);
            }
        }
        
        // Show objective preview
        objectivePreview.setText((String) roleData.get("objective_template"));
        
        // Make details visible
        roleDetailsContainer.setVisible(true);
    }
    
    @FXML
    private void handleContinue(ActionEvent event) {
        if (selectedRoleId == null) {
            showAlert("Please select a role first");
            return;
        }
        
        logger.info("Continuing to resume wizard with role: " + selectedRoleData.get("name"));
        navigateToWizard();
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("AI-Powered Resume Builder - Login");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load login screen", e);
        }
    }
    
    private void navigateToWizard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResumeWizard.fxml"));
            Parent root = loader.load();
            
            // Pass data to the wizard controller
            WizardController wizardController = loader.getController();
            wizardController.setResumeData(resumeData);
            wizardController.setSelectedRoleData(selectedRoleData);
            
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Resume Builder - Step-by-Step Wizard");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load resume wizard", e);
            showAlert("Error loading resume wizard");
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static ResumeData getResumeData() {
        return resumeData;
    }
}