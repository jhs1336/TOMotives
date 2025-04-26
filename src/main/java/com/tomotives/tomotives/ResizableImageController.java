package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;

public class ResizableImageController {
    @FXML
    ImageView imageView;
    Image image;

    /**
     * Sets the image to be displayed in the ImageView and resizes the ImageView to fit the image.
     *
     * @param image the new image to be displayed
     */
        public void setImage(Image image) {
        imageView.setImage(image);
        this.image = image;
        resize(imageView.getFitWidth(), imageView.getFitHeight());
    }

    /**
     * Called by javaFX on the componenent initialization
     * Gets the image object from the iamgeview
     */
    @FXML
    public void initialize() {
        image = imageView.getImage();
//        resize(425, 300);
    }

    /**
     * Resizes the image view to the given width and height, maintaining the aspect ratio of the original image
     * The image is cropped to fit the target aspect ratio, with the cropping centered on the image
     *
     * @param width the desired width of the image
     * @param height the desired height of the image
     */
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
    }

    /**
     * Apply rounded corners to the image view
     * @param cornerRadius the radius of the rounded corners
     */
    public void applyRoundedCorners(double cornerRadius) {
        // create a rectangle with rounded corners
        Rectangle clip = new Rectangle(
                imageView.getFitWidth(),
                imageView.getFitHeight()
        );
        clip.setArcWidth(cornerRadius);
        clip.setArcHeight(cornerRadius);

        // bind the clip size to the image view size
        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());

        // apply the clip to the image view
        imageView.setClip(clip);
    }

    /**
     * Apply a circular clip to the image
     */
    public void applyCircularClip() {
        // for a circular image, ensure the image is square first
        double size = Math.min(imageView.getFitWidth(), imageView.getFitHeight());
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        // create a circle clip
        Rectangle clip = new Rectangle(size, size);
        clip.setArcWidth(size);
        clip.setArcHeight(size);

        // bind the clip size to the image view size
        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());

        // apply the clip
        imageView.setClip(clip);
    }
}
