package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class LargeLocationController {
    @FXML
    private ImageView resizableImage;
    @FXML
    private ResizableImageController resizableImageController;
    @FXML
    public void initialize() {
        System.out.println("initialize" + resizableImage.getFitWidth() + " " + resizableImage.getFitHeight());
        resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
    }
}
