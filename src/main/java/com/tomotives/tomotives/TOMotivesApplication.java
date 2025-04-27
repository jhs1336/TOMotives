package com.tomotives.tomotives;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TOMotivesApplication extends Application {
    private static Scene scene;
    private static Stage stage;

    /**Joshua
     * Initializes the JavaFX application and sets up the main stage
     * This method is called when the application is launched
     * It sets the title of the stage, disables resizing, and loads the home page
     */
    @Override
    public void start(Stage stage) {
        // set up options
        this.stage = stage;
        stage.setTitle("TOMotives");
        stage.setResizable(false); // fixed window size

        loadPage("home.fxml");
    }

    /**Joshua
     * Loads a page from the specified FXML file and sets it as the scene for the application
     *
     * @param url The path to the FXML file to load, relative to the application's resources
     */
    public static void loadPage(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(TOMotivesApplication.class.getResource(url));
            // DIMENSIONS: 1280 x 720
            scene = new Scene(loader.load(), 1280, 720);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Joshua
     * Gets the stage of the application
     *
     * @return the stage of the application
     */
    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}