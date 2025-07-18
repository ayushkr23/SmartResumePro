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
        
        logger.info("Role selector initialized");
    }
    
    private void loadRoleData() {
        try (InputStream is = getClass().getResourceAsStream("/data/role_data.json")) {
            if (is != null) {
                String jsonContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                rolesData = parseJson(jsonContent);
                logger.info("Role data loaded successfully");
            } else {
                logger.warning("Role data file not found, using default data");
                createDefaultRoleData();
            }
        } catch (IOException e) {
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
        
        // Add more default roles...
        rolesData.put("roles", roles);
    }
    
    @SuppressWarnings("unchecked")
    private void createRoleCards() {
        roleCardsContainer.getChildren().clear();
        roleCards.clear();
        
        List<Map<String, Object>> roles = (List<Map<String, Object>>) rolesData.get("roles");
        if (roles == null) return;
        
        for (Map<String, Object> role : roles) {
            VBox roleCard = createRoleCard(role);
            roleCards.add(roleCard);
            roleCardsContainer.getChildren().add(roleCard);
        }
    }
    
    private VBox createRoleCard(Map<String, Object> role) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(12);
        card.setPrefWidth(200);
        card.setPrefHeight(180);
        card.getStyleClass().addAll("role-card");
        
        // Icon
        Label icon = new Label((String) role.get("icon"));
        icon.getStyleClass().add("role-icon");
        
        // Name
        Label name = new Label((String) role.get("name"));
        name.getStyleClass().add("role-name");
        name.setWrapText(true);
        name.setMaxWidth(180);
        
        // Description
        Label description = new Label((String) role.get("description"));
        description.getStyleClass().add("role-description");
        description.setWrapText(true);
        description.setMaxWidth(180);
        
        card.getChildren().addAll(icon, name, description);
        
        // Add click handler
        card.setOnMouseClicked(event -> selectRole((String) role.get("id"), role, card));
        
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