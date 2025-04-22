package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class LoginController {
    @FXML
    private ImageView resizableImage;
    @FXML
    private ResizableImageController resizableImageController;

    @FXML
    private void initialize() {
        resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
        resizableImageController.applyRoundedCorners(60);
    }
}
