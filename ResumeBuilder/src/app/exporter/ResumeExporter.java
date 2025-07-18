package app.exporter;

import app.model.ResumeData;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.imageio.ImageIO;

/**
 * PDF Resume Exporter
 * Generates professional PDF resumes with custom layouts
 * Note: This is a simplified implementation using Java 2D Graphics
 * In production, you would use Apache PDFBox or iText for better PDF handling
 */
public class ResumeExporter {
    
    private static final Logger logger = Logger.getLogger(ResumeExporter.class.getName());
    
    // Page dimensions (A4 in points: 595 x 842)
    private static final int PAGE_WIDTH = 595;
    private static final int PAGE_HEIGHT = 842;
    private static final int MARGIN = 50;
    private static final int CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN;
    
    // Colors
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Blue
    private static final Color SECONDARY_COLOR = new Color(71, 85, 105); // Gray
    private static final Color TEXT_COLOR = new Color(30, 41, 59); // Dark gray
    private static final Color LIGHT_GRAY = new Color(248, 250, 252);
    
    /**
     * Exports resume data to PDF format
     * 
     * @param resumeData The resume data to export
     * @param templateId The template to use for layout
     * @param outputPath The path where the PDF should be saved
     * @param qrCodePath Optional path to QR code image
     * @return true if export was successful, false otherwise
     */
    public static boolean exportResume(ResumeData resumeData, String templateId, String outputPath, String qrCodePath) {
        try {
            logger.info("Starting PDF export for template: " + templateId);
            
            // Create output directory if it doesn't exist
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Generate PDF content as image (simplified approach)
            BufferedImage resumeImage = generateResumeImage(resumeData, templateId, qrCodePath);
            
            // Convert to PDF-like format (for demonstration, we'll save as PNG)
            // In real implementation, use PDFBox to create actual PDF
            String tempImagePath = outputPath.replace(".pdf", "_temp.png");
            ImageIO.write(resumeImage, "PNG", new File(tempImagePath));
            
            // Create a simple PDF wrapper (placeholder)
            createSimplePDF(resumeImage, outputPath);
            
            // Clean up temp file
            new File(tempImagePath).delete();
            
            logger.info("PDF export completed successfully: " + outputPath);
            return true;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to export resume to PDF", e);
            return false;
        }
    }
    
    /**
     * Generates the resume as a BufferedImage
     */
    private static BufferedImage generateResumeImage(ResumeData resumeData, String templateId, String qrCodePath) {
        BufferedImage image = new BufferedImage(PAGE_WIDTH, PAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Fill background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, PAGE_WIDTH, PAGE_HEIGHT);
        
        // Generate content based on template
        switch (templateId) {
            case "template1":
                generateModernProfessionalTemplate(g2d, resumeData, qrCodePath);
                break;
            case "template2":
                generateCleanSimpleTemplate(g2d, resumeData, qrCodePath);
                break;
            case "template3":
                generateCreativeTemplate(g2d, resumeData, qrCodePath);
                break;
            case "template4":
                generateTechnicalTemplate(g2d, resumeData, qrCodePath);
                break;
            default:
                generateModernProfessionalTemplate(g2d, resumeData, qrCodePath);
                break;
        }
        
        g2d.dispose();
        return image;
    }
    
    /**
     * Template 1: Modern Professional
     */
    private static void generateModernProfessionalTemplate(Graphics2D g2d, ResumeData resumeData, String qrCodePath) {
        int currentY = MARGIN;
        
        // Header with blue background
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillRect(0, 0, PAGE_WIDTH, 120);
        
        // Name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        String name = resumeData.getFullName() != null ? resumeData.getFullName().toUpperCase() : "YOUR NAME";
        g2d.drawString(name, MARGIN, 45);
        
        // Role
        if (resumeData.getSelectedRole() != null) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            g2d.drawString(resumeData.getSelectedRole(), MARGIN, 70);
        }
        
        // Contact info
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        String contact = buildContactString(resumeData);
        g2d.drawString(contact, MARGIN, 95);
        
        currentY = 140;
        
        // Main content
        g2d.setColor(TEXT_COLOR);
        
        // Objective
        if (resumeData.getObjective() != null && !resumeData.getObjective().trim().isEmpty()) {
            currentY = drawSection(g2d, "CAREER OBJECTIVE", resumeData.getObjective(), currentY);
        }
        
        // Skills
        if (!resumeData.getTechnicalSkills().isEmpty() || !resumeData.getSoftSkills().isEmpty()) {
            String skillsText = buildSkillsString(resumeData);
            currentY = drawSection(g2d, "SKILLS", skillsText, currentY);
        }
        
        // Education placeholder
        currentY = drawSection(g2d, "EDUCATION", "Educational background will be displayed here", currentY);
        
        // Experience placeholder
        currentY = drawSection(g2d, "EXPERIENCE", "Work experience will be displayed here", currentY);
        
        // Projects placeholder
        currentY = drawSection(g2d, "PROJECTS", "Projects will be displayed here", currentY);
        
        // QR Code
        if (qrCodePath != null) {
            drawQRCode(g2d, qrCodePath, PAGE_WIDTH - MARGIN - 80, PAGE_HEIGHT - MARGIN - 80);
        }
        
        // Footer
        drawFooter(g2d);
    }
    
    /**
     * Template 2: Clean & Simple
     */
    private static void generateCleanSimpleTemplate(Graphics2D g2d, ResumeData resumeData, String qrCodePath) {
        int currentY = MARGIN + 20;
        
        // Name (centered)
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String name = resumeData.getFullName() != null ? resumeData.getFullName() : "YOUR NAME";
        FontMetrics fm = g2d.getFontMetrics();
        int nameWidth = fm.stringWidth(name);
        g2d.drawString(name, (PAGE_WIDTH - nameWidth) / 2, currentY);
        currentY += 30;
        
        // Contact (centered)
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        String contact = buildContactString(resumeData);
        fm = g2d.getFontMetrics();
        int contactWidth = fm.stringWidth(contact);
        g2d.drawString(contact, (PAGE_WIDTH - contactWidth) / 2, currentY);
        currentY += 40;
        
        // Divider line
        g2d.setColor(SECONDARY_COLOR);
        g2d.drawLine(MARGIN, currentY, PAGE_WIDTH - MARGIN, currentY);
        currentY += 30;
        
        g2d.setColor(TEXT_COLOR);
        
        // Sections
        if (resumeData.getObjective() != null && !resumeData.getObjective().trim().isEmpty()) {
            currentY = drawSimpleSection(g2d, "OBJECTIVE", resumeData.getObjective(), currentY);
        }
        
        if (!resumeData.getTechnicalSkills().isEmpty() || !resumeData.getSoftSkills().isEmpty()) {
            String skillsText = buildSkillsString(resumeData);
            currentY = drawSimpleSection(g2d, "SKILLS", skillsText, currentY);
        }
        
        currentY = drawSimpleSection(g2d, "EDUCATION", "Educational background", currentY);
        currentY = drawSimpleSection(g2d, "EXPERIENCE", "Work experience", currentY);
        currentY = drawSimpleSection(g2d, "PROJECTS", "Projects", currentY);
        
        if (qrCodePath != null) {
            drawQRCode(g2d, qrCodePath, PAGE_WIDTH - MARGIN - 60, PAGE_HEIGHT - MARGIN - 60);
        }
        
        drawFooter(g2d);
    }
    
    /**
     * Template 3: Creative
     */
    private static void generateCreativeTemplate(Graphics2D g2d, ResumeData resumeData, String qrCodePath) {
        int currentY = MARGIN;
        
        // Gradient header
        GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, PAGE_WIDTH, 100, new Color(147, 51, 234));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, PAGE_WIDTH, 100);
        
        // Name with emoji
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 26));
        String name = "🎯 " + (resumeData.getFullName() != null ? resumeData.getFullName() : "YOUR NAME");
        g2d.drawString(name, MARGIN, 45);
        
        if (resumeData.getSelectedRole() != null) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString(resumeData.getSelectedRole(), MARGIN, 70);
        }
        
        currentY = 120;
        g2d.setColor(TEXT_COLOR);
        
        // Creative sections with emojis
        if (resumeData.getObjective() != null && !resumeData.getObjective().trim().isEmpty()) {
            currentY = drawCreativeSection(g2d, "💡 OBJECTIVE", resumeData.getObjective(), currentY);
        }
        
        if (!resumeData.getTechnicalSkills().isEmpty() || !resumeData.getSoftSkills().isEmpty()) {
            String skillsText = buildSkillsString(resumeData);
            currentY = drawCreativeSection(g2d, "⚡ SKILLS", skillsText, currentY);
        }
        
        currentY = drawCreativeSection(g2d, "🎓 EDUCATION", "Educational background", currentY);
        currentY = drawCreativeSection(g2d, "💼 EXPERIENCE", "Work experience", currentY);
        currentY = drawCreativeSection(g2d, "🚀 PROJECTS", "Projects showcase", currentY);
        
        if (qrCodePath != null) {
            drawQRCode(g2d, qrCodePath, PAGE_WIDTH - MARGIN - 70, PAGE_HEIGHT - MARGIN - 70);
        }
        
        drawFooter(g2d);
    }
    
    /**
     * Template 4: Technical
     */
    private static void generateTechnicalTemplate(Graphics2D g2d, ResumeData resumeData, String qrCodePath) {
        int currentY = MARGIN;
        
        // Technical header with borders
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Courier New", Font.BOLD, 20));
        String name = resumeData.getFullName() != null ? resumeData.getFullName().toUpperCase() : "YOUR NAME";
        g2d.drawString(name, MARGIN, currentY + 20);
        currentY += 30;
        
        if (resumeData.getSelectedRole() != null) {
            g2d.setFont(new Font("Courier New", Font.PLAIN, 14));
            g2d.drawString(resumeData.getSelectedRole().toUpperCase(), MARGIN, currentY);
            currentY += 20;
        }
        
        // Technical divider
        g2d.setColor(SECONDARY_COLOR);
        for (int i = 0; i < CONTENT_WIDTH / 10; i++) {
            g2d.drawString("=", MARGIN + i * 10, currentY);
        }
        currentY += 30;
        
        g2d.setColor(TEXT_COLOR);
        
        // Technical sections
        if (resumeData.getObjective() != null && !resumeData.getObjective().trim().isEmpty()) {
            currentY = drawTechnicalSection(g2d, "OBJECTIVE", resumeData.getObjective(), currentY);
        }
        
        if (!resumeData.getTechnicalSkills().isEmpty() || !resumeData.getSoftSkills().isEmpty()) {
            String skillsText = buildTechnicalSkillsString(resumeData);
            currentY = drawTechnicalSection(g2d, "TECHNICAL SKILLS", skillsText, currentY);
        }
        
        currentY = drawTechnicalSection(g2d, "EDUCATION", "Educational background", currentY);
        currentY = drawTechnicalSection(g2d, "EXPERIENCE", "Work experience", currentY);
        currentY = drawTechnicalSection(g2d, "PROJECTS", "Technical projects", currentY);
        
        if (qrCodePath != null) {
            drawQRCode(g2d, qrCodePath, PAGE_WIDTH - MARGIN - 60, PAGE_HEIGHT - MARGIN - 60);
        }
        
        drawFooter(g2d);
    }
    
    // Helper methods for drawing sections
    
    private static int drawSection(Graphics2D g2d, String title, String content, int startY) {
        int currentY = startY;
        
        // Section title
        g2d.setColor(PRIMARY_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(title, MARGIN, currentY);
        currentY += 20;
        
        // Underline
        g2d.drawLine(MARGIN, currentY, MARGIN + 100, currentY);
        currentY += 15;
        
        // Content
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        currentY = drawWrappedText(g2d, content, MARGIN, currentY, CONTENT_WIDTH);
        
        return currentY + 20;
    }
    
    private static int drawSimpleSection(Graphics2D g2d, String title, String content, int startY) {
        int currentY = startY;
        
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(title, MARGIN, currentY);
        currentY += 15;
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        currentY = drawWrappedText(g2d, content, MARGIN, currentY, CONTENT_WIDTH);
        
        return currentY + 15;
    }
    
    private static int drawCreativeSection(Graphics2D g2d, String title, String content, int startY) {
        int currentY = startY;
        
        g2d.setColor(PRIMARY_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.drawString(title, MARGIN, currentY);
        currentY += 18;
        
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        currentY = drawWrappedText(g2d, content, MARGIN, currentY, CONTENT_WIDTH);
        
        return currentY + 18;
    }
    
    private static int drawTechnicalSection(Graphics2D g2d, String title, String content, int startY) {
        int currentY = startY;
        
        g2d.setFont(new Font("Courier New", Font.BOLD, 12));
        g2d.drawString(title, MARGIN, currentY);
        currentY += 15;
        
        g2d.setFont(new Font("Courier New", Font.PLAIN, 10));
        currentY = drawWrappedText(g2d, content, MARGIN, currentY, CONTENT_WIDTH);
        
        return currentY + 15;
    }
    
    private static int drawWrappedText(Graphics2D g2d, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2d.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int currentY = y;
        
        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(testLine) > maxWidth && line.length() > 0) {
                g2d.drawString(line.toString(), x, currentY);
                currentY += fm.getHeight();
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        
        if (line.length() > 0) {
            g2d.drawString(line.toString(), x, currentY);
            currentY += fm.getHeight();
        }
        
        return currentY;
    }
    
    private static void drawQRCode(Graphics2D g2d, String qrCodePath, int x, int y) {
        try {
            BufferedImage qrImage = ImageIO.read(new File(qrCodePath));
            g2d.drawImage(qrImage, x, y, 60, 60, null);
        } catch (IOException e) {
            // Draw placeholder if QR code fails to load
            g2d.setColor(LIGHT_GRAY);
            g2d.fillRect(x, y, 60, 60);
            g2d.setColor(TEXT_COLOR);
            g2d.drawRect(x, y, 60, 60);
            g2d.setFont(new Font("Arial", Font.PLAIN, 8));
            g2d.drawString("QR CODE", x + 15, y + 30);
        }
    }
    
    private static void drawFooter(Graphics2D g2d) {
        g2d.setColor(SECONDARY_COLOR);
        g2d.setFont(new Font("Arial", Font.PLAIN, 8));
        String footer = "Generated by AI-Powered Resume Builder on " + 
                       LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        FontMetrics fm = g2d.getFontMetrics();
        int footerWidth = fm.stringWidth(footer);
        g2d.drawString(footer, (PAGE_WIDTH - footerWidth) / 2, PAGE_HEIGHT - 20);
    }
    
    // Helper methods for building content strings
    
    private static String buildContactString(ResumeData resumeData) {
        StringBuilder contact = new StringBuilder();
        
        if (resumeData.getEmail() != null) {
            contact.append("📧 ").append(resumeData.getEmail());
        }
        
        if (resumeData.getPhone() != null) {
            if (contact.length() > 0) contact.append(" | ");
            contact.append("📱 ").append(resumeData.getPhone());
        }
        
        if (resumeData.getLinkedinUrl() != null) {
            if (contact.length() > 0) contact.append(" | ");
            contact.append("🔗 LinkedIn");
        }
        
        if (resumeData.getGithubUrl() != null) {
            if (contact.length() > 0) contact.append(" | ");
            contact.append("🔗 GitHub");
        }
        
        return contact.toString();
    }
    
    private static String buildSkillsString(ResumeData resumeData) {
        StringBuilder skills = new StringBuilder();
        
        if (!resumeData.getTechnicalSkills().isEmpty()) {
            skills.append("Technical: ");
            skills.append(String.join(", ", resumeData.getTechnicalSkills()));
        }
        
        if (!resumeData.getSoftSkills().isEmpty()) {
            if (skills.length() > 0) skills.append("\n");
            skills.append("Soft Skills: ");
            skills.append(String.join(", ", resumeData.getSoftSkills()));
        }
        
        return skills.toString();
    }
    
    private static String buildTechnicalSkillsString(ResumeData resumeData) {
        StringBuilder skills = new StringBuilder();
        
        if (!resumeData.getTechnicalSkills().isEmpty()) {
            skills.append("Languages/Frameworks: ");
            skills.append(String.join(", ", resumeData.getTechnicalSkills()));
        }
        
        if (!resumeData.getSoftSkills().isEmpty()) {
            if (skills.length() > 0) skills.append("\n");
            skills.append("Additional Skills: ");
            skills.append(String.join(", ", resumeData.getSoftSkills()));
        }
        
        return skills.toString();
    }
    
    /**
     * Creates a simple PDF file (placeholder implementation)
     * In a real application, use Apache PDFBox or iText
     */
    private static void createSimplePDF(BufferedImage resumeImage, String outputPath) throws IOException {
        // This is a simplified implementation
        // In practice, you would use PDFBox to create a proper PDF:
        /*
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(tempImageFile, document);
        contentStream.drawImage(pdImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
        contentStream.close();
        
        document.save(outputPath);
        document.close();
        */
        
        // For now, save as PNG with PDF extension (demo purposes)
        ImageIO.write(resumeImage, "PNG", new File(outputPath));
        logger.info("Resume saved as image file: " + outputPath);
    }
}