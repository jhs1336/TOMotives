package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ResizableImageController {
    @FXML
    ImageView imageView;
    Image image;

    @FXML
    public void initialize() {
        image = imageView.getImage();
        resize(425, 300);
    }

    public void resize(double width, double height) {
        double targetAspectRatio = width / height;
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

        Rectangle2D viewport = new Rectangle2D(cropX, cropY, cropWidth, cropHeight);
        imageView.setViewport(viewport);

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        System.out.println("resized");
    }
}