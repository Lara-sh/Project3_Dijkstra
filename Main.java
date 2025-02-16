package Project3;

import java.io.File;
import java.io.IOException;
import Project3.MainProj;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class Main extends Application {
    File file;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create the root pane
            StackPane backgroundPane = new StackPane();
            BorderPane layout = new BorderPane();
            Scene scene = new Scene(backgroundPane, 1000, 600);

            // Load the background image
            String imagePath = "file:/C:/Users/ASUS/Desktop/Algo/Project_3/src/Project3/AirPl.jpg"; // Update with your image path
            Image backgroundImage = new Image(imagePath);
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(1000); // Set desired width
            backgroundView.setFitHeight(600); // Set desired height
            backgroundView.setPreserveRatio(false);

            // Add the background image to the StackPane
            backgroundPane.getChildren().add(backgroundView);

            // Add the "Welcome to the Airport" text at the top
            Text text = new Text("Welcome to the Airport");
            text.setFont(new Font("Arial", 50)); // Increased font size for the text
            text.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 2;");
            layout.setTop(text);
            BorderPane.setAlignment(text, javafx.geometry.Pos.CENTER); // Center the text horizontally
            BorderPane.setMargin(text, new Insets(60, 0, 0, 0)); // Add 60px margin from the top

            // Create the "Choose File" button and place it at the bottom
            Button chooseFileButton = new Button("Choose File");
            chooseFileButton.setStyle(
                "-fx-font-size: 20; " + // Increased font size for the button
                "-fx-background-color: black; " + // Black background color
                "-fx-text-fill: white; " + // White text color
                "-fx-padding: 10 20; " + // Padding for button size
                "-fx-border-color: white; " + // Optional: Add a white border
                "-fx-border-width: 2;"
            );
            layout.setBottom(chooseFileButton);
            BorderPane.setAlignment(chooseFileButton, javafx.geometry.Pos.CENTER); // Center the button horizontally
            BorderPane.setMargin(chooseFileButton, new Insets(0, 0, 70, 0)); // Reduced bottom margin to move button up

            // Add the BorderPane to the StackPane
            backgroundPane.getChildren().add(layout);

            // File chooser configuration
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File Chooser");

            // Event handler for file selection
            chooseFileButton.setOnAction(e -> {
            	ExtensionFilter filterTXT = new ExtensionFilter("Text files", "*txt");
    			fileChooser.getExtensionFilters().clear();
    			fileChooser.getExtensionFilters().add(filterTXT);

    			file = fileChooser.showOpenDialog(primaryStage);

    			try {
    				if (file.length() == 0)
    					throw new IOException();
    				MainProj mapScene = new MainProj(primaryStage, scene, file);
    				primaryStage.setScene(mapScene);
                    primaryStage.setMaximized(true);
    			} catch (Exception e2) {
    				e2.printStackTrace();
    				Alert alert = new Alert(AlertType.ERROR);
    				alert.setTitle("Error");
    				alert.setHeaderText("File is empty, try another valid file");
    				alert.showAndWait();
    			}
            });


            // Set up the scene and stage
            primaryStage.setTitle("Airport Greeting");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: Unable to load application. Check image path and JavaFX setup.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
