package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LargeLocationController {
    public ImageView imageView;
    public Image image;
    Rectangle2D cropRegion = new Rectangle2D(300, 425, 425, 300);

    @FXML
    public void initialize() {
        image = imageView.getImage();

        System.out.println(image.getWidth() / image.getHeight());
        System.out.println(425.0 / 300.0);

        double targetAspectRatio = 425.0 / 300.0;
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double currentAspectRatio = imageWidth / imageHeight;

        double cropX = 0;
        double cropY = 0;
        double cropWidth = imageWidth;
        double cropHeight = imageHeight;

        if (currentAspectRatio > targetAspectRatio) {
            // image is wider than needed
            cropWidth = imageHeight * targetAspectRatio;
            cropX = (imageWidth - cropWidth) / 2;
        } else if (currentAspectRatio < targetAspectRatio) {
            // image is taller than needed
            cropHeight = imageWidth / targetAspectRatio;
            cropY = (imageHeight - cropHeight) / 2;
        }

        cropRegion = new Rectangle2D(cropX, cropY, cropWidth, cropHeight);
        imageView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            }
        });
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.R) {
            imageView.setPreserveRatio(true);
        }
        if (event.getCode() == KeyCode.W) {
            imageView.setFitWidth(1000);
        }
        if (event.getCode() == KeyCode.S) {
            imageView.setViewport(cropRegion);
        }
    }
}
