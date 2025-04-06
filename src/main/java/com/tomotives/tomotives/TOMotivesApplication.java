package com.tomotives.tomotives;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TOMotivesApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // load xml
            FXMLLoader loader = new FXMLLoader(TOMotivesApplication.class.getResource("home.fxml"));

            // DIMENSIONS: 1280 x 720
            Scene scene = new Scene(loader.load(), 1280, 720);
            scene.getStylesheets().add(getClass().getResource("small-location-display.css").toExternalForm());
            // set up options
            stage.setTitle("TOMotives");
            stage.setScene(scene);
            stage.setResizable(false); // fixed window size
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}