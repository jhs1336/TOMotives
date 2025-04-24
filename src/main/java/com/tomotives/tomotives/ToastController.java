package com.tomotives.tomotives;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ToastController {
    public enum ToastType {
        SUCCESS, ERROR, INFO
    }

    private int duration = 3000; // 3 seconds
    private static final int FADE_IN_DURATION = 1000; // 1 second
    private static final int FADE_OUT_DURATION = 1000; // 1 second

    @FXML
    private StackPane toastContainer;

    @FXML
    private Label toastMessageLabel;

    private Popup popup;
    private ToastType type;
    private String message;

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @FXML
    private void initialize() {
        // called by javafx when first loaded
    }

    public void initialize(String message, ToastType type) {
        this.message = message;
        this.type = type;

        // set message text
        toastMessageLabel.setText(message);

        // apply style class based on type
        String typeClass = type.toString().toLowerCase();
        toastContainer.getStyleClass().add(typeClass);

        // create popup
        popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(false);
        popup.setHideOnEscape(false);
        popup.getContent().add(toastContainer);
    }

    public void showToast(Stage ownerStage) {
        // position toast at the bottom center of screen
        popup.setOnShown(e -> {
            popup.setX(ownerStage.getX() + (ownerStage.getWidth() - popup.getWidth()) / 2);
            popup.setY(ownerStage.getY() + ownerStage.getHeight() - popup.getHeight() - 50);
        });

        // show the toast with animation
        toastContainer.setOpacity(0);
        popup.show(ownerStage);

        // fade in animation
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey = new KeyFrame(Duration.millis(FADE_IN_DURATION),
                new KeyValue(toastContainer.opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey);
        fadeInTimeline.setOnFinished(e -> {
            // wait for duration
            Timeline holdTimeline = new Timeline(
                    new KeyFrame(Duration.millis(duration - FADE_IN_DURATION - FADE_OUT_DURATION))
            );
            holdTimeline.setOnFinished(e2 -> {
                // fade out
                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey = new KeyFrame(Duration.millis(FADE_OUT_DURATION),
                        new KeyValue(toastContainer.opacityProperty(), 0));
                fadeOutTimeline.getKeyFrames().add(fadeOutKey);
                fadeOutTimeline.setOnFinished(e3 -> popup.hide());
                fadeOutTimeline.play();
            });
            holdTimeline.play();
        });
        fadeInTimeline.play();
    }
}