package com.tomotives.tomotives.services;

import com.tomotives.tomotives.controllers.ToastController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ToastService {
    /**Joshua
     * Displays a toast notification on the screen
     *
     * @param ownerStage The stage that owns the toast notification
     * @param message The message to display in the toast
     * @param type The type of toast to display (e.g. success, error, info)
     */
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
    /**Joshua
     * Displays a toast notification on the screen with a custom duration
     *
     * @param ownerStage The stage that owns the toast notification
     * @param message The message to display in the toast
     * @param type The type of toast to display (e.g. success, error, info)
     * @param duration The duration in milliseconds to display the toast
     */
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
