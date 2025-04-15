package com.tomotives.tomotives;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ToastService {
    public static void show(Stage ownerStage, String message, ToastController.ToastType type) {
        Platform.runLater(() -> {
            try {
                // load the FXML
                FXMLLoader loader = new FXMLLoader(ToastController.class.getResource("toast.fxml"));
                StackPane root = loader.load();

                // create the controller
                ToastController controller = loader.getController();
                controller.initialize(message, type);

                // show the toast
                controller.showToast(ownerStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static void show(Stage ownerStage, String message, ToastController.ToastType type, int duration) {
        Platform.runLater(() -> {
            try {
                // load the FXML
                FXMLLoader loader = new FXMLLoader(ToastController.class.getResource("toast.fxml"));
                StackPane root = loader.load();

                // create and configure the controller
                ToastController controller = loader.getController();
                controller.initialize(message, type);

                // set custom duration and show toast
                controller.setDuration(duration);
                controller.showToast(ownerStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
