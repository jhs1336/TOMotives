package com.tomotives.tomotives;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TOMotivesApplication extends Application {
    private Scene scene;

    @Override
    public void start(Stage stage) {
        try {
            // load xml
            FXMLLoader loader = new FXMLLoader(TOMotivesApplication.class.getResource("sign-up.fxml"));

            // DIMENSIONS: 1920 x 1080
            scene = new Scene(loader.load(), 1920, 1080);
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