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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.Main;
import app.model.ResumeData;

import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

public class WizardController implements Initializable {
    
    // Navigation
    @FXML private VBox wizardContainer;
    @FXML private HBox navigationContainer;
    @FXML private Button backButton;
    @FXML private Button nextButton;
    @FXML private Label stepLabel;
    @FXML private ProgressBar progressBar;
    @FXML private HBox stepIndicators;
    
    // Step 1 - Personal Information
    @FXML private VBox step1Container;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressField;
    @FXML private TextField linkedinField;
    @FXML private TextField githubField;
    @FXML private TextField portfolioField;
    
    // Step 2 - Education
    @FXML private VBox step2Container;
    @FXML private VBox educationContainer;
    @FXML private Button addEducationButton;
    
    // Step 3 - Skills
    @FXML private VBox step3Container;
    @FXML private TextField skillInput;
    @FXML private FlowPane technicalSkillsPane;
    @FXML private FlowPane softSkillsPane;
    @FXML private VBox suggestedSkillsContainer;
    @FXML private ComboBox<String> skillCategoryCombo;
    
    // Step 4 - Experience
    @FXML private VBox step4Container;
    @FXML private VBox experienceContainer;
    @FXML private Button addExperienceButton;
    
    // Step 5 - Projects
    @FXML private VBox step5Container;
    @FXML private VBox projectsContainer;
    @FXML private Button addProjectButton;
    
    // Step 6 - Summary & Objective
    @FXML private VBox step6Container;
    @FXML private TextArea summaryArea;
    @FXML private TextArea objectiveArea;
    @FXML private VBox aiSuggestionsContainer;
    @FXML private Button generateSuggestionsButton;
    
    private static final Logger logger = Logger.getLogger(WizardController.class.getName());
    private ResumeData resumeData;
    private Map<String, Object> selectedRoleData;
    private int currentStep = 1;
    private final int totalSteps = 6;
    private List<VBox> steps;
    private List<VBox> educationEntries = new ArrayList<>();
    private List<VBox> experienceEntries = new ArrayList<>();
    private List<VBox> projectEntries = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSteps();
        setupSkillsUI();
        setupNavigation();
        
        logger.info("Resume wizard initialized");
    }
    
    public void setResumeData(ResumeData resumeData) {
        this.resumeData = resumeData;
        populateFields();
    }
    
    public void setSelectedRoleData(Map<String, Object> roleData) {
        this.selectedRoleData = roleData;
        setupRoleGuidance();
    }
    
    private void setupSteps() {
        steps = Arrays.asList(
            step1Container, step2Container, step3Container,
            step4Container, step5Container, step6Container
        );
        
        // Initially show only step 1
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).setVisible(i == 0);
            steps.get(i).setManaged(i == 0);
        }
        
        updateStepDisplay();
    }
    
    private void setupSkillsUI() {
        skillCategoryCombo.setItems(FXCollections.observableArrayList(
            "Technical Skills", "Soft Skills"
        ));
        skillCategoryCombo.setValue("Technical Skills");
        
        // Add skill input handler
        skillInput.setOnAction(event -> addSkill());
    }
    
    private void setupNavigation() {
        updateNavigationButtons();
        updateProgressBar();
    }
    
    @SuppressWarnings("unchecked")
    private void setupRoleGuidance() {
        if (selectedRoleData == null) return;
        
        // Show suggested skills for the selected role
        List<String> suggestedSkills = (List<String>) selectedRoleData.get("skills");
        if (suggestedSkills != null) {
            setupSuggestedSkills(suggestedSkills);
        }
        
        // Pre-fill objective if available
        String objectiveTemplate = (String) selectedRoleData.get("objective_template");
        if (objectiveTemplate != null && objectiveArea != null) {
            objectiveArea.setText(objectiveTemplate);
        }
    }
    
    private void setupSuggestedSkills(List<String> skills) {
        if (suggestedSkillsContainer == null) return;
        
        suggestedSkillsContainer.getChildren().clear();
        
        Label title = new Label("💡 Suggested Skills for " + selectedRoleData.get("name") + ":");
        title.getStyleClass().add("label-subheading");
        suggestedSkillsContainer.getChildren().add(title);
        
        FlowPane skillsFlow = new FlowPane();
        skillsFlow.setHgap(8);
        skillsFlow.setVgap(8);
        
        for (String skill : skills) {
            Button skillButton = new Button(skill);
            skillButton.getStyleClass().addAll("button-secondary");
            skillButton.setOnAction(event -> addSuggestedSkill(skill));
            skillsFlow.getChildren().add(skillButton);
        }
        
        suggestedSkillsContainer.getChildren().add(skillsFlow);
    }
    
    private void populateFields() {
        if (resumeData == null) return;
        
        // Personal Information
        if (fullNameField != null) fullNameField.setText(resumeData.getFullName() != null ? resumeData.getFullName() : "");
        if (emailField != null) emailField.setText(resumeData.getEmail() != null ? resumeData.getEmail() : "");
        if (phoneField != null) phoneField.setText(resumeData.getPhone() != null ? resumeData.getPhone() : "");
        if (addressField != null) addressField.setText(resumeData.getAddress() != null ? resumeData.getAddress() : "");
        if (linkedinField != null) linkedinField.setText(resumeData.getLinkedinUrl() != null ? resumeData.getLinkedinUrl() : "");
        if (githubField != null) githubField.setText(resumeData.getGithubUrl() != null ? resumeData.getGithubUrl() : "");
        if (portfolioField != null) portfolioField.setText(resumeData.getPortfolioUrl() != null ? resumeData.getPortfolioUrl() : "");
        
        // Summary and Objective
        if (summaryArea != null) summaryArea.setText(resumeData.getSummary() != null ? resumeData.getSummary() : "");
        if (objectiveArea != null && resumeData.getObjective() != null) {
            objectiveArea.setText(resumeData.getObjective());
        }
        
        // Skills
        populateSkills();
    }
    
    private void populateSkills() {
        if (technicalSkillsPane != null && resumeData.getTechnicalSkills() != null) {
            for (String skill : resumeData.getTechnicalSkills()) {
                addSkillChip(skill, technicalSkillsPane);
            }
        }
        
        if (softSkillsPane != null && resumeData.getSoftSkills() != null) {
            for (String skill : resumeData.getSoftSkills()) {
                addSkillChip(skill, softSkillsPane);
            }
        }
    }
    
    @FXML
    private void handleNext(ActionEvent event) {
        if (validateCurrentStep()) {
            saveCurrentStepData();
            
            if (currentStep < totalSteps) {
                currentStep++;
                updateStepDisplay();
                updateNavigationButtons();
                updateProgressBar();
            } else {
                // Final step - navigate to template picker
                navigateToTemplatePicker();
            }
        }
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        if (currentStep > 1) {
            saveCurrentStepData();
            currentStep--;
            updateStepDisplay();
            updateNavigationButtons();
            updateProgressBar();
        } else {
            // Go back to role selector
            navigateToRoleSelector();
        }
    }
    
    private void updateStepDisplay() {
        // Hide all steps
        for (VBox step : steps) {
            step.setVisible(false);
            step.setManaged(false);
        }
        
        // Show current step
        if (currentStep <= steps.size()) {
            VBox currentStepContainer = steps.get(currentStep - 1);
            currentStepContainer.setVisible(true);
            currentStepContainer.setManaged(true);
        }
        
        // Update step label
        stepLabel.setText("Step " + currentStep + " of " + totalSteps);
        
        // Update step indicators
        updateStepIndicators();
    }
    
    private void updateStepIndicators() {
        if (stepIndicators == null) return;
        
        stepIndicators.getChildren().clear();
        
        for (int i = 1; i <= totalSteps; i++) {
            Label indicator = new Label(String.valueOf(i));
            indicator.getStyleClass().add("step-indicator");
            
            if (i < currentStep) {
                indicator.getStyleClass().add("step-indicator-completed");
            } else if (i == currentStep) {
                indicator.getStyleClass().add("step-indicator-active");
            }
            
            stepIndicators.getChildren().add(indicator);
            
            if (i < totalSteps) {
                Label arrow = new Label("→");
                arrow.getStyleClass().add("label-muted");
                stepIndicators.getChildren().add(arrow);
            }
        }
    }
    
    private void updateNavigationButtons() {
        backButton.setText(currentStep == 1 ? "← Role Selector" : "← Previous");
        nextButton.setText(currentStep == totalSteps ? "Choose Template →" : "Next →");
    }
    
    private void updateProgressBar() {
        double progress = (double) currentStep / totalSteps;
        progressBar.setProgress(progress);
    }
    
    private boolean validateCurrentStep() {
        switch (currentStep) {
            case 1:
                return validatePersonalInfo();
            case 2:
                return validateEducation();
            case 3:
                return validateSkills();
            case 4:
                return true; // Experience is optional
            case 5:
                return true; // Projects are optional
            case 6:
                return validateSummary();
            default:
                return true;
        }
    }
    
    private boolean validatePersonalInfo() {
        if (fullNameField.getText().trim().isEmpty()) {
            showValidationError("Please enter your full name");
            fullNameField.requestFocus();
            return false;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            showValidationError("Please enter your email address");
            emailField.requestFocus();
            return false;
        }
        
        if (!isValidEmail(emailField.getText().trim())) {
            showValidationError("Please enter a valid email address");
            emailField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean validateEducation() {
        if (educationEntries.isEmpty()) {
            showValidationError("Please add at least one education entry");
            return false;
        }
        return true;
    }
    
    private boolean validateSkills() {
        if (technicalSkillsPane.getChildren().isEmpty()) {
            showValidationError("Please add at least a few technical skills");
            return false;
        }
        return true;
    }
    
    private boolean validateSummary() {
        if (objectiveArea.getText().trim().isEmpty()) {
            showValidationError("Please provide an objective or career summary");
            objectiveArea.requestFocus();
            return false;
        }
        return true;
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void saveCurrentStepData() {
        if (resumeData == null) return;
        
        switch (currentStep) {
            case 1:
                savePersonalInfo();
                break;
            case 2:
                saveEducation();
                break;
            case 3:
                saveSkills();
                break;
            case 4:
                saveExperience();
                break;
            case 5:
                saveProjects();
                break;
            case 6:
                saveSummary();
                break;
        }
        
        resumeData.updateLastModified();
    }
    
    private void savePersonalInfo() {
        resumeData.setFullName(fullNameField.getText().trim());
        resumeData.setEmail(emailField.getText().trim());
        resumeData.setPhone(phoneField.getText().trim());
        resumeData.setAddress(addressField.getText().trim());
        resumeData.setLinkedinUrl(linkedinField.getText().trim());
        resumeData.setGithubUrl(githubField.getText().trim());
        resumeData.setPortfolioUrl(portfolioField.getText().trim());
    }
    
    private void saveEducation() {
        // Implementation for saving education data
        // This would iterate through educationEntries and extract data
    }
    
    private void saveSkills() {
        List<String> technicalSkills = new ArrayList<>();
        List<String> softSkills = new ArrayList<>();
        
        // Extract technical skills
        technicalSkillsPane.getChildren().forEach(node -> {
            if (node instanceof HBox) {
                HBox skillChip = (HBox) node;
                Label skillLabel = (Label) skillChip.getChildren().get(0);
                technicalSkills.add(skillLabel.getText());
            }
        });
        
        // Extract soft skills
        softSkillsPane.getChildren().forEach(node -> {
            if (node instanceof HBox) {
                HBox skillChip = (HBox) node;
                Label skillLabel = (Label) skillChip.getChildren().get(0);
                softSkills.add(skillLabel.getText());
            }
        });
        
        resumeData.setTechnicalSkills(technicalSkills);
        resumeData.setSoftSkills(softSkills);
    }
    
    private void saveExperience() {
        // Implementation for saving experience data
    }
    
    private void saveProjects() {
        // Implementation for saving projects data
    }
    
    private void saveSummary() {
        resumeData.setSummary(summaryArea.getText().trim());
        resumeData.setObjective(objectiveArea.getText().trim());
    }
    
    @FXML
    private void addSkill() {
        String skill = skillInput.getText().trim();
        if (!skill.isEmpty()) {
            String category = skillCategoryCombo.getValue();
            FlowPane targetPane = "Technical Skills".equals(category) ? technicalSkillsPane : softSkillsPane;
            
            addSkillChip(skill, targetPane);
            skillInput.clear();
        }
    }
    
    private void addSuggestedSkill(String skill) {
        addSkillChip(skill, technicalSkillsPane);
    }
    
    private void addSkillChip(String skill, FlowPane targetPane) {
        // Check if skill already exists
        for (javafx.scene.Node node : targetPane.getChildren()) {
            if (node instanceof HBox) {
                HBox existingChip = (HBox) node;
                Label existingLabel = (Label) existingChip.getChildren().get(0);
                if (existingLabel.getText().equals(skill)) {
                    return; // Skill already exists
                }
            }
        }
        
        HBox skillChip = new HBox();
        skillChip.setAlignment(Pos.CENTER);
        skillChip.setSpacing(8);
        skillChip.getStyleClass().add("skill-tag");
        
        Label skillLabel = new Label(skill);
        Button removeButton = new Button("×");
        removeButton.getStyleClass().addAll("button-secondary");
        removeButton.setStyle("-fx-min-width: 20px; -fx-min-height: 20px; -fx-padding: 0;");
        removeButton.setOnAction(event -> targetPane.getChildren().remove(skillChip));
        
        skillChip.getChildren().addAll(skillLabel, removeButton);
        targetPane.getChildren().add(skillChip);
    }
    
    @FXML
    private void addEducation() {
        VBox educationEntry = createEducationEntry();
        educationContainer.getChildren().add(educationEntry);
        educationEntries.add(educationEntry);
    }
    
    private VBox createEducationEntry() {
        VBox entry = new VBox();
        entry.setSpacing(12);
        entry.getStyleClass().add("form-section");
        
        // Header with remove button
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12);
        
        Label title = new Label("🎓 Education Entry");
        title.getStyleClass().add("label-subheading");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button removeButton = new Button("Remove");
        removeButton.getStyleClass().add("button-secondary");
        removeButton.setOnAction(event -> {
            educationContainer.getChildren().remove(entry);
            educationEntries.remove(entry);
        });
        
        header.getChildren().addAll(title, spacer, removeButton);
        
        // Form fields
        TextField degreeField = new TextField();
        degreeField.setPromptText("Degree (e.g., B.Tech in Computer Science)");
        
        TextField institutionField = new TextField();
        institutionField.setPromptText("Institution Name");
        
        HBox yearGradeRow = new HBox();
        yearGradeRow.setSpacing(16);
        
        TextField yearField = new TextField();
        yearField.setPromptText("Year (e.g., 2020-2024)");
        yearField.setPrefWidth(150);
        
        TextField gradeField = new TextField();
        gradeField.setPromptText("Grade/CGPA (e.g., 8.5/10)");
        gradeField.setPrefWidth(150);
        
        yearGradeRow.getChildren().addAll(yearField, gradeField);
        
        TextField locationField = new TextField();
        locationField.setPromptText("Location (City, State)");
        
        entry.getChildren().addAll(header, degreeField, institutionField, yearGradeRow, locationField);
        
        return entry;
    }
    
    @FXML
    private void addExperience() {
        // Similar to addEducation but for work experience
    }
    
    @FXML
    private void addProject() {
        // Similar to addEducation but for projects
    }
    
    @FXML
    private void generateAISuggestions() {
        // Placeholder for AI suggestions
        showAISuggestions();
    }
    
    private void showAISuggestions() {
        if (aiSuggestionsContainer == null) return;
        
        aiSuggestionsContainer.getChildren().clear();
        
        Label title = new Label("🤖 AI Suggestions");
        title.getStyleClass().add("label-subheading");
        
        String currentObjective = objectiveArea.getText().trim();
        String suggestion = generateObjectiveSuggestion(currentObjective);
        
        TextArea suggestionArea = new TextArea(suggestion);
        suggestionArea.setPrefRowCount(3);
        suggestionArea.setWrapText(true);
        suggestionArea.setEditable(false);
        
        Button applyButton = new Button("Apply Suggestion");
        applyButton.getStyleClass().addAll("button", "button-success");
        applyButton.setOnAction(event -> objectiveArea.setText(suggestion));
        
        aiSuggestionsContainer.getChildren().addAll(title, suggestionArea, applyButton);
    }
    
    private String generateObjectiveSuggestion(String current) {
        // Simple AI suggestion based on role
        if (selectedRoleData == null) return current;
        
        String roleName = (String) selectedRoleData.get("name");
        String template = (String) selectedRoleData.get("objective_template");
        
        if (template != null && !template.isEmpty()) {
            return template + " Passionate about leveraging technology to solve real-world problems and create meaningful impact.";
        }
        
        return "Motivated " + roleName + " with strong foundational skills seeking to contribute to innovative projects while continuously learning and growing in a dynamic environment.";
    }
    
    private void navigateToTemplatePicker() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TemplatePicker.fxml"));
            Parent root = loader.load();
            
            // Pass data to template picker
            TemplatePickerController controller = loader.getController();
            controller.setResumeData(resumeData);
            
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Choose Resume Template");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load template picker", e);
        }
    }
    
    private void navigateToRoleSelector() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/RoleSelector.fxml"));
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Select Your Career Path");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load role selector", e);
        }
    }
}