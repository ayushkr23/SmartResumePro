package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application {
    
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        
        // Load the login screen
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root, 1000, 700);
        
        // Apply CSS styling
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        
        stage.setTitle("AI-Powered Resume Builder - Tier 2/3 Students");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        // Set application icon
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/app_icon.png")));
        } catch (Exception e) {
            System.out.println("App icon not found, continuing without icon");
        }
        
        stage.show();
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}