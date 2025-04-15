package com.tomotives.tomotives;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TOMotivesApplication extends Application {
    private Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) {
        try {
            // load xml
            FXMLLoader loader = new FXMLLoader(TOMotivesApplication.class.getResource("sign-up.fxml"));

            // DIMENSIONS: 1280 x 720
            scene = new Scene(loader.load(), 1280, 720);
            // set up options
            this.stage = stage;
            stage.setTitle("TOMotives");
            stage.setScene(scene);
            stage.setResizable(false); // fixed window size
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