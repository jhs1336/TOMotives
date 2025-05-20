package com.tomotives.tomotives;

import com.tomotives.tomotives.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Application extends javafx.application.Application {
    private static Scene scene;
    private static Stage stage;
    private static User user;
    private static String page;

    public static User getUser() {
        return user;
    }
    public static void setUser(User user) {
        Application.user = user;
    }

    /**Joshua
     * Gets the stage of the application
     *
     * @return the stage of the application
     */
    public static Stage getStage() {
        return stage;
    }
    public static String getPage() {
        return page;
    }

    public static InputStream getResourceAsStream(String resourceName) {
        return Application.class.getResourceAsStream(resourceName);
    }
    public static URL getResource(String resourceName) {
        return Application.class.getResource(resourceName);
    }

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
     * @param pageName The name of the page loaded
     */
    public static void loadPage(String url, String pageName) {
        try {
            page = pageName;
            FXMLLoader loader = new FXMLLoader(Application.class.getResource(url));
            // DIMENSIONS: 1280 x 720
            scene = new Scene(loader.load(), 1280, 720);
            // global stylesheet applied to all pages
            scene.getStylesheets().add(getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadPage(String url) {
        loadPage(url, url.substring(0, url.indexOf('.')));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
