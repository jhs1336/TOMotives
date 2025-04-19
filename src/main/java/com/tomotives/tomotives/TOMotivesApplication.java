package com.tomotives.tomotives;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TOMotivesApplication extends Application {
    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) {
        // set up options
        this.stage = stage;
        stage.setTitle("TOMotives");
        stage.setResizable(false); // fixed window size

        loadPage("home.fxml");
    }

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

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}