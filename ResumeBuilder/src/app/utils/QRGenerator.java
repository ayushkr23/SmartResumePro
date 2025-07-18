package app.utils;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.imageio.ImageIO;

public class QRGenerator {
    
    private static final Logger logger = Logger.getLogger(QRGenerator.class.getName());
    
    /**
     * Generates a QR code image for the given URL
     * Note: This is a placeholder implementation. In a real application,
     * you would use a library like ZXing to generate actual QR codes.
     * 
     * @param url The URL to encode in the QR code
     * @param outputPath The path where the QR code image should be saved
     * @param size The size of the QR code image (width and height)
     * @return true if the QR code was generated successfully, false otherwise
     */
    public static boolean generateQRCode(String url, String outputPath, int size) {
        try {
            // Create a placeholder QR code image
            BufferedImage qrImage = createPlaceholderQR(url, size);
            
            // Ensure output directory exists
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Save the image
            ImageIO.write(qrImage, "PNG", outputFile);
            
            logger.info("QR code generated successfully: " + outputPath);
            return true;
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to generate QR code", e);
            return false;
        }
    }
    
    /**
     * Creates a placeholder QR code image with text
     * In a real implementation, this would use ZXing library
     */
    private static BufferedImage createPlaceholderQR(String url, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Set background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size, size);
        
        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(5, 5, size - 10, size - 10);
        
        // Draw QR pattern (simplified)
        drawQRPattern(g2d, size);
        
        // Add text in center (for demonstration)
        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, Math.max(10, size / 20));
        g2d.setFont(font);
        
        FontMetrics fm = g2d.getFontMetrics();
        String text = "QR CODE";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int x = (size - textWidth) / 2;
        int y = (size + textHeight) / 2;
        
        // White background for text
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x - 5, y - textHeight + 5, textWidth + 10, textHeight);
        
        // Draw text
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x, y);
        
        // Add URL at bottom (small text)
        if (url.length() > 0) {
            Font smallFont = new Font("Arial", Font.PLAIN, Math.max(8, size / 30));
            g2d.setFont(smallFont);
            FontMetrics smallFm = g2d.getFontMetrics();
            
            String displayUrl = url.length() > 20 ? url.substring(0, 17) + "..." : url;
            int urlWidth = smallFm.stringWidth(displayUrl);
            int urlX = (size - urlWidth) / 2;
            int urlY = size - 15;
            
            g2d.setColor(Color.WHITE);
            g2d.fillRect(urlX - 2, urlY - smallFm.getAscent(), urlWidth + 4, smallFm.getHeight());
            
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString(displayUrl, urlX, urlY);
        }
        
        g2d.dispose();
        return image;
    }
    
    /**
     * Draws a simplified QR code pattern
     */
    private static void drawQRPattern(Graphics2D g2d, int size) {
        int moduleSize = Math.max(2, size / 25);
        g2d.setColor(Color.BLACK);
        
        // Draw corner squares (finder patterns)
        drawFinderPattern(g2d, 10, 10, moduleSize);
        drawFinderPattern(g2d, size - 10 - 7 * moduleSize, 10, moduleSize);
        drawFinderPattern(g2d, 10, size - 10 - 7 * moduleSize, moduleSize);
        
        // Draw some random modules for QR appearance
        for (int i = 0; i < size / moduleSize; i++) {
            for (int j = 0; j < size / moduleSize; j++) {
                if (Math.random() > 0.6) {
                    int x = 10 + i * moduleSize;
                    int y = 10 + j * moduleSize;
                    
                    // Avoid corners
                    if (!isInCorner(i, j, size / moduleSize)) {
                        g2d.fillRect(x, y, moduleSize - 1, moduleSize - 1);
                    }
                }
            }
        }
    }
    
    private static void drawFinderPattern(Graphics2D g2d, int x, int y, int moduleSize) {
        // Outer square
        g2d.fillRect(x, y, 7 * moduleSize, 7 * moduleSize);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + moduleSize, y + moduleSize, 5 * moduleSize, 5 * moduleSize);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x + 2 * moduleSize, y + 2 * moduleSize, 3 * moduleSize, 3 * moduleSize);
    }
    
    private static boolean isInCorner(int i, int j, int maxSize) {
        return (i < 9 && j < 9) || 
               (i > maxSize - 9 && j < 9) || 
               (i < 9 && j > maxSize - 9);
    }
    
    /**
     * Generates QR code for resume download link
     */
    public static boolean generateResumeQR(String resumeFileName, String outputPath) {
        String downloadUrl = "https://yourresume.com/download/" + resumeFileName;
        return generateQRCode(downloadUrl, outputPath, 150);
    }
    
    /**
     * Generates QR code for LinkedIn profile
     */
    public static boolean generateLinkedInQR(String linkedinUrl, String outputPath) {
        return generateQRCode(linkedinUrl, outputPath, 150);
    }
    
    /**
     * Generates QR code for GitHub profile
     */
    public static boolean generateGitHubQR(String githubUrl, String outputPath) {
        return generateQRCode(githubUrl, outputPath, 150);
    }
    
    /**
     * Generates QR code for portfolio website
     */
    public static boolean generatePortfolioQR(String portfolioUrl, String outputPath) {
        return generateQRCode(portfolioUrl, outputPath, 150);
    }
}