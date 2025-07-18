package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import app.Main;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

public class LoginController implements Initializable {
    
    @FXML private VBox loginContainer;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField emailField;
    @FXML private VBox loginForm;
    @FXML private VBox registerForm;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button switchToRegisterButton;
    @FXML private Button switchToLoginButton;
    @FXML private Button guestModeButton;
    @FXML private Label statusLabel;
    @FXML private CheckBox rememberMeCheckBox;
    
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    private final Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
    
    private static String currentUser = "Guest";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set initial form visibility
        showLoginForm();
        
        // Load saved credentials if remember me was checked
        loadSavedCredentials();
        
        // Add input validation
        setupValidation();
        
        logger.info("Login screen initialized");
    }
    
    private void setupValidation() {
        // Add listeners for real-time validation
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateLoginForm();
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateLoginForm();
        });
        
        registerUsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateRegisterForm();
        });
        
        registerPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateRegisterForm();
        });
        
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateRegisterForm();
        });
        
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateRegisterForm();
        });
    }
    
    private void validateLoginForm() {
        boolean isValid = !usernameField.getText().trim().isEmpty() && 
                         !passwordField.getText().trim().isEmpty();
        loginButton.setDisable(!isValid);
    }
    
    private void validateRegisterForm() {
        boolean isValid = !registerUsernameField.getText().trim().isEmpty() &&
                         !registerPasswordField.getText().trim().isEmpty() &&
                         !confirmPasswordField.getText().trim().isEmpty() &&
                         !emailField.getText().trim().isEmpty() &&
                         registerPasswordField.getText().equals(confirmPasswordField.getText()) &&
                         isValidEmail(emailField.getText());
        registerButton.setDisable(!isValid);
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private void loadSavedCredentials() {
        String savedUsername = prefs.get("username", "");
        boolean rememberMe = prefs.getBoolean("rememberMe", false);
        
        if (rememberMe && !savedUsername.isEmpty()) {
            usernameField.setText(savedUsername);
            rememberMeCheckBox.setSelected(true);
        }
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please fill in all fields", "error");
            return;
        }
        
        // Simple authentication (in real app, this would be more secure)
        if (authenticateUser(username, password)) {
            currentUser = username;
            
            // Save credentials if remember me is checked
            if (rememberMeCheckBox.isSelected()) {
                prefs.put("username", username);
                prefs.putBoolean("rememberMe", true);
            } else {
                prefs.remove("username");
                prefs.putBoolean("rememberMe", false);
            }
            
            logger.log(Level.INFO, "User logged in: " + username);
            showStatus("Login successful! Redirecting...", "success");
            
            // Navigate to role selector
            navigateToRoleSelector();
        } else {
            showStatus("Invalid username or password", "error");
            logger.log(Level.WARNING, "Failed login attempt for user: " + username);
        }
    }
    
    @FXML
    private void handleRegister(ActionEvent event) {
        String username = registerUsernameField.getText().trim();
        String password = registerPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String email = emailField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showStatus("Please fill in all fields", "error");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showStatus("Passwords do not match", "error");
            return;
        }
        
        if (!isValidEmail(email)) {
            showStatus("Please enter a valid email address", "error");
            return;
        }
        
        if (password.length() < 6) {
            showStatus("Password must be at least 6 characters long", "error");
            return;
        }
        
        // Register user (in real app, this would save to database)
        if (registerUser(username, password, email)) {
            currentUser = username;
            logger.log(Level.INFO, "New user registered: " + username);
            showStatus("Registration successful! Redirecting...", "success");
            
            // Navigate to role selector
            navigateToRoleSelector();
        } else {
            showStatus("Username already exists", "error");
        }
    }
    
    @FXML
    private void handleGuestMode(ActionEvent event) {
        currentUser = "Guest";
        logger.info("User entered guest mode");
        showStatus("Entering guest mode...", "success");
        
        // Navigate to role selector
        navigateToRoleSelector();
    }
    
    @FXML
    private void showRegisterForm(ActionEvent event) {
        loginForm.setVisible(false);
        registerForm.setVisible(true);
        statusLabel.setText("");
    }
    
    @FXML
    private void showLoginForm(ActionEvent event) {
        loginForm.setVisible(true);
        registerForm.setVisible(false);
        statusLabel.setText("");
    }
    
    private void showLoginForm() {
        if (loginForm != null && registerForm != null) {
            loginForm.setVisible(true);
            registerForm.setVisible(false);
        }
    }
    
    private void showStatus(String message, String type) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("success", "error");
        statusLabel.getStyleClass().add(type);
    }
    
    private boolean authenticateUser(String username, String password) {
        // Simple authentication - in real app, this would check against a database
        String savedPassword = prefs.get("user_" + username, "");
        return !savedPassword.isEmpty() && savedPassword.equals(password);
    }
    
    private boolean registerUser(String username, String password, String email) {
        // Check if user already exists
        String existingPassword = prefs.get("user_" + username, "");
        if (!existingPassword.isEmpty()) {
            return false; // User already exists
        }
        
        // Save user credentials (in real app, this would be more secure)
        prefs.put("user_" + username, password);
        prefs.put("email_" + username, email);
        return true;
    }
    
    private void navigateToRoleSelector() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/RoleSelector.fxml"));
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            Stage stage = Main.getPrimaryStage();
            stage.setScene(scene);
            stage.setTitle("Select Your Career Path - Resume Builder");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load role selector", e);
            showStatus("Error navigating to next screen", "error");
        }
    }
    
    public static String getCurrentUser() {
        return currentUser;
    }
}