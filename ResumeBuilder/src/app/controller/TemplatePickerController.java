package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import app.Main;
import app.model.ResumeData;
import app.exporter.ResumeExporter;
import app.utils.QRGenerator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class TemplatePickerController implements Initializable {
    
    @FXML private ScrollPane templateScrollPane;
    @FXML private HBox templateContainer;
    @FXML private Button exportButton;
    @FXML private Button saveButton;
    @FXML private Button backButton;
    @FXML private Label selectedTemplateLabel;
    @FXML private VBox previewContainer;
    @FXML private TextArea previewArea;
    @FXML private CheckBox includeQRCheckBox;
    @FXML private ComboBox<String> formatComboBox;
    @FXML private TextField fileNameField;
    @FXML private Label statusLabel;
    
    private static final Logger logger = Logger.getLogger(TemplatePickerController.class.getName());
    private ResumeData resumeData;
    private String selectedTemplate;
    private List<VBox> templateCards = new ArrayList<>();
    
    // Template configurations
    private static final String[] TEMPLATE_NAMES = {
        "Modern Professional", "Clean & Simple", "Creative", "Technical"
    };
    
    private static final String[] TEMPLATE_DESCRIPTIONS = {
        "Clean, modern design perfect for corporate roles",
        "Minimalist layout focusing on content clarity", 
        "Stylish design with subtle colors and graphics",
        "Technical layout optimized for engineering roles"
    };
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
        createTemplateCards();
        setupFileExport();
        
        logger.info("Template picker initialized");
    }
    
    public void setResumeData(ResumeData resumeData) {
        this.resumeData = resumeData;
        updatePreview();
        
        // Set default filename
        String defaultName = generateDefaultFileName();
        fileNameField.setText(defaultName);
    }
    
    private void setupUI() {
        // Setup format combo box
        formatComboBox.getItems().addAll("PDF", "HTML (Preview)");
        formatComboBox.setValue("PDF");
        
        // Setup other defaults
        includeQRCheckBox.setSelected(true);
        exportButton.setDisable(true);
        
        // Add listeners
        formatComboBox.setOnAction(event -> updateExportButton());
        includeQRCheckBox.setOnAction(event -> updatePreview());
    }
    
    private void setupFileExport() {
        fileNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateExportButton();
        });
    }
    
    private void createTemplateCards() {
        templateContainer.getChildren().clear();
        templateCards.clear();
        
        for (int i = 0; i < TEMPLATE_NAMES.length; i++) {
            VBox templateCard = createTemplateCard(i);
            templateCards.add(templateCard);
            templateContainer.getChildren().add(templateCard);
        }
    }
    
    private VBox createTemplateCard(int templateIndex) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(12);
        card.setPrefWidth(220);
        card.setPrefHeight(320);
        card.getStyleClass().addAll("template-card");
        
        // Template preview image (placeholder)
        VBox previewBox = new VBox();
        previewBox.setAlignment(Pos.CENTER);
        previewBox.setPrefWidth(200);
        previewBox.setPrefHeight(260);
        previewBox.getStyleClass().add("template-preview");
        
        // Create a simple preview representation
        Label previewLabel = new Label(createTemplatePreview(templateIndex));
        previewLabel.setStyle("-fx-font-size: 10px; -fx-text-alignment: center;");
        previewLabel.setWrapText(true);
        previewLabel.setMaxWidth(180);
        
        previewBox.getChildren().add(previewLabel);
        
        // Template name
        Label nameLabel = new Label(TEMPLATE_NAMES[templateIndex]);
        nameLabel.getStyleClass().add("template-name");
        
        // Template description
        Label descLabel = new Label(TEMPLATE_DESCRIPTIONS[templateIndex]);
        descLabel.getStyleClass().add("label-muted");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(200);
        descLabel.setStyle("-fx-font-size: 11px;");
        
        card.getChildren().addAll(previewBox, nameLabel, descLabel);
        
        // Add click handler
        final String templateId = "template" + (templateIndex + 1);
        card.setOnMouseClicked(event -> selectTemplate(templateId, card));
        
        return card;
    }
    
    private String createTemplatePreview(int templateIndex) {
        switch (templateIndex) {
            case 0: // Modern Professional
                return "JOHN DOE\n" +
                       "Software Developer\n" +
                       "john@email.com | +91 12345\n\n" +
                       "OBJECTIVE\n" +
                       "Aspiring software developer...\n\n" +
                       "SKILLS\n" +
                       "• Java  • Python  • React\n\n" +
                       "EDUCATION\n" +
                       "B.Tech Computer Science\n" +
                       "XYZ University (2024)\n\n" +
                       "PROJECTS\n" +
                       "Web Application\n" +
                       "Mobile App";
                       
            case 1: // Clean & Simple
                return "JOHN DOE\n" +
                       "john@email.com\n" +
                       "+91 12345 67890\n\n" +
                       "OBJECTIVE\n" +
                       "Aspiring software developer with strong\n" +
                       "programming fundamentals...\n\n" +
                       "EDUCATION\n" +
                       "B.Tech Computer Science\n" +
                       "XYZ University\n\n" +
                       "SKILLS\n" +
                       "Java, Python, JavaScript\n\n" +
                       "PROJECTS\n" +
                       "E-commerce Website\n" +
                       "Task Management App";
                       
            case 2: // Creative
                return "🎯 JOHN DOE\n" +
                       "Software Developer\n\n" +
                       "📧 john@email.com\n" +
                       "📱 +91 12345 67890\n\n" +
                       "💡 OBJECTIVE\n" +
                       "Creative developer passionate about...\n\n" +
                       "🎓 EDUCATION\n" +
                       "B.Tech Computer Science\n\n" +
                       "⚡ SKILLS\n" +
                       "[Java] [Python] [React]\n\n" +
                       "🚀 PROJECTS\n" +
                       "Innovative Web App\n" +
                       "Mobile Solution";
                       
            case 3: // Technical
                return "JOHN DOE\n" +
                       "SOFTWARE ENGINEER\n" +
                       "═══════════════════\n" +
                       "CONTACT\n" +
                       "📧 john@email.com\n" +
                       "🔗 github.com/johndoe\n\n" +
                       "TECHNICAL SKILLS\n" +
                       "Languages: Java, Python, C++\n" +
                       "Frameworks: Spring, React\n" +
                       "Tools: Git, Docker, Jenkins\n\n" +
                       "PROJECTS\n" +
                       "Distributed System\n" +
                       "ML Algorithm Implementation";
                       
            default:
                return "Resume Template Preview";
        }
    }
    
    private void selectTemplate(String templateId, VBox clickedCard) {
        // Remove selection from all cards
        for (VBox card : templateCards) {
            card.getStyleClass().remove("template-card-selected");
        }
        
        // Add selection to clicked card
        clickedCard.getStyleClass().add("template-card-selected");
        
        selectedTemplate = templateId;
        selectedTemplateLabel.setText("Selected: " + TEMPLATE_NAMES[getTemplateIndex(templateId)]);
        exportButton.setDisable(false);
        
        // Update preview
        updatePreview();
        
        logger.info("Template selected: " + templateId);
    }
    
    private int getTemplateIndex(String templateId) {
        return Integer.parseInt(templateId.replace("template", "")) - 1;
    }
    
    private void updatePreview() {
        if (resumeData == null || selectedTemplate == null) {
            return;
        }
        
        String preview = generatePreviewText();
        previewArea.setText(preview);
    }
    
    private String generatePreviewText() {
        StringBuilder preview = new StringBuilder();
        
        // Header
        preview.append("====== RESUME PREVIEW ======\n\n");
        
        // Personal Information
        preview.append(resumeData.getFullName() != null ? resumeData.getFullName().toUpperCase() : "YOUR NAME");
        preview.append("\n");
        if (resumeData.getSelectedRole() != null) {
            preview.append(resumeData.getSelectedRole()).append("\n");
        }
        preview.append("\n");
        
        // Contact Info
        if (resumeData.getEmail() != null) {
            preview.append("Email: ").append(resumeData.getEmail()).append("\n");
        }
        if (resumeData.getPhone() != null) {
            preview.append("Phone: ").append(resumeData.getPhone()).append("\n");
        }
        if (resumeData.getLinkedinUrl() != null) {
            preview.append("LinkedIn: ").append(resumeData.getLinkedinUrl()).append("\n");
        }
        if (resumeData.getGithubUrl() != null) {
            preview.append("GitHub: ").append(resumeData.getGithubUrl()).append("\n");
        }
        preview.append("\n");
        
        // Objective
        if (resumeData.getObjective() != null && !resumeData.getObjective().trim().isEmpty()) {
            preview.append("CAREER OBJECTIVE\n");
            preview.append(resumeData.getObjective()).append("\n\n");
        }
        
        // Skills
        if (!resumeData.getTechnicalSkills().isEmpty() || !resumeData.getSoftSkills().isEmpty()) {
            preview.append("SKILLS\n");
            if (!resumeData.getTechnicalSkills().isEmpty()) {
                preview.append("Technical: ");
                preview.append(String.join(", ", resumeData.getTechnicalSkills()));
                preview.append("\n");
            }
            if (!resumeData.getSoftSkills().isEmpty()) {
                preview.append("Soft Skills: ");
                preview.append(String.join(", ", resumeData.getSoftSkills()));
                preview.append("\n");
            }
            preview.append("\n");
        }
        
        // Education (placeholder)
        preview.append("EDUCATION\n");
        preview.append("Your educational background will appear here\n\n");
        
        // Experience (placeholder)
        preview.append("EXPERIENCE\n");
        preview.append("Your work experience will appear here\n\n");
        
        // Projects (placeholder)
        preview.append("PROJECTS\n");
        preview.append("Your projects will appear here\n\n");
        
        // QR Code note
        if (includeQRCheckBox.isSelected()) {
            preview.append("Note: QR code will be included in the exported PDF\n");
        }
        
        preview.append("\n====== END PREVIEW ======");
        
        return preview.toString();
    }
    
    private void updateExportButton() {
        boolean canExport = selectedTemplate != null && 
                           !fileNameField.getText().trim().isEmpty();
        exportButton.setDisable(!canExport);
    }
    
    @FXML
    private void handleExport(ActionEvent event) {
        if (resumeData == null || selectedTemplate == null) {
            showAlert("Please select a template first");
            return;
        }
        
        String fileName = fileNameField.getText().trim();
        if (fileName.isEmpty()) {
            showAlert("Please enter a file name");
            return;
        }
        
        String format = formatComboBox.getValue();
        
        try {
            if ("PDF".equals(format)) {
                exportToPDF(fileName);
            } else {
                exportToHTML(fileName);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Export failed", e);
            showAlert("Export failed: " + e.getMessage());
        }
    }
    
    private void exportToPDF(String fileName) throws Exception {
        statusLabel.setText("Generating PDF...");
        
        // Ensure .pdf extension
        if (!fileName.toLowerCase().endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Create output directory if it doesn't exist
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        String outputPath = "output/" + fileName;
        
        // Generate QR code if requested
        String qrPath = null;
        if (includeQRCheckBox.isSelected()) {
            qrPath = generateQRCode(fileName);
        }
        
        // Export using ResumeExporter
        boolean success = ResumeExporter.exportResume(resumeData, selectedTemplate, outputPath, qrPath);
        
        if (success) {
            statusLabel.setText("✅ PDF exported successfully: " + outputPath);
            logger.info("Resume exported to: " + outputPath);
            
            // Show success dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your resume has been exported to: " + outputPath);
            alert.showAndWait();
        } else {
            throw new Exception("Failed to generate PDF");
        }
    }
    
    private void exportToHTML(String fileName) throws Exception {
        statusLabel.setText("Generating HTML preview...");
        
        // Ensure .html extension
        if (!fileName.toLowerCase().endsWith(".html")) {
            fileName += ".html";
        }
        
        String outputPath = "output/" + fileName;
        String htmlContent = generateHTMLPreview();
        
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(htmlContent.getBytes("UTF-8"));
        }
        
        statusLabel.setText("✅ HTML preview exported: " + outputPath);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText("HTML preview exported to: " + outputPath);
        alert.showAndWait();
    }
    
    private String generateHTMLPreview() {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html><head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<title>Resume - ").append(resumeData.getFullName()).append("</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 40px; line-height: 1.6; }\n");
        html.append("h1 { color: #2563eb; margin-bottom: 5px; }\n");
        html.append("h2 { color: #1e40af; border-bottom: 2px solid #e5e7eb; padding-bottom: 5px; }\n");
        html.append(".contact { color: #6b7280; margin-bottom: 20px; }\n");
        html.append(".skills { display: flex; flex-wrap: wrap; gap: 8px; }\n");
        html.append(".skill { background: #e0e7ff; padding: 4px 8px; border-radius: 4px; font-size: 14px; }\n");
        html.append("</style>\n");
        html.append("</head><body>\n");
        
        // Header
        html.append("<h1>").append(resumeData.getFullName() != null ? resumeData.getFullName() : "").append("</h1>\n");
        if (resumeData.getSelectedRole() != null) {
            html.append("<p><strong>").append(resumeData.getSelectedRole()).append("</strong></p>\n");
        }
        
        // Contact
        html.append("<div class='contact'>\n");
        if (resumeData.getEmail() != null) {
            html.append("📧 ").append(resumeData.getEmail()).append(" | ");
        }
        if (resumeData.getPhone() != null) {
            html.append("📱 ").append(resumeData.getPhone()).append("<br>");
        }
        if (resumeData.getLinkedinUrl() != null) {
            html.append("🔗 <a href='").append(resumeData.getLinkedinUrl()).append("'>LinkedIn</a> | ");
        }
        if (resumeData.getGithubUrl() != null) {
            html.append("🔗 <a href='").append(resumeData.getGithubUrl()).append("'>GitHub</a>");
        }
        html.append("</div>\n");
        
        // Objective
        if (resumeData.getObjective() != null && !resumeData.getObjective().trim().isEmpty()) {
            html.append("<h2>Career Objective</h2>\n");
            html.append("<p>").append(resumeData.getObjective()).append("</p>\n");
        }
        
        // Skills
        if (!resumeData.getTechnicalSkills().isEmpty() || !resumeData.getSoftSkills().isEmpty()) {
            html.append("<h2>Skills</h2>\n");
            html.append("<div class='skills'>\n");
            
            for (String skill : resumeData.getTechnicalSkills()) {
                html.append("<span class='skill'>").append(skill).append("</span>\n");
            }
            for (String skill : resumeData.getSoftSkills()) {
                html.append("<span class='skill'>").append(skill).append("</span>\n");
            }
            
            html.append("</div>\n");
        }
        
        html.append("<h2>Education</h2>\n");
        html.append("<p><em>Educational background will be displayed here</em></p>\n");
        
        html.append("<h2>Experience</h2>\n");
        html.append("<p><em>Work experience will be displayed here</em></p>\n");
        
        html.append("<h2>Projects</h2>\n");
        html.append("<p><em>Projects will be displayed here</em></p>\n");
        
        html.append("<hr><p style='font-size: 12px; color: #6b7280;'>");
        html.append("Generated by AI-Powered Resume Builder on ");
        html.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        html.append("</p>\n");
        
        html.append("</body></html>");
        
        return html.toString();
    }
    
    private String generateQRCode(String fileName) {
        try {
            String qrFileName = fileName.replace(".pdf", "_qr.png");
            String qrPath = "resources/assets/qr/" + qrFileName;
            
            // Generate QR for LinkedIn if available, otherwise use a generic message
            String qrContent = resumeData.getLinkedinUrl();
            if (qrContent == null || qrContent.trim().isEmpty()) {
                qrContent = "Contact: " + resumeData.getEmail();
            }
            
            boolean success = QRGenerator.generateQRCode(qrContent, qrPath, 150);
            return success ? qrPath : null;
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to generate QR code", e);
            return null;
        }
    }
    
    @FXML
    private void handleSave(ActionEvent event) {
        // TODO: Implement save functionality for resume data
        showAlert("Save functionality will be implemented in the next version");
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        // Go back to wizard (last step)
        try {
            // Implementation for going back to wizard
            showAlert("Back functionality will navigate to the previous wizard step");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to navigate back", e);
        }
    }
    
    private String generateDefaultFileName() {
        String name = resumeData != null && resumeData.getFullName() != null ? 
                     resumeData.getFullName().replaceAll("[^a-zA-Z0-9]", "_") : "resume";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return name + "_" + timestamp;
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}