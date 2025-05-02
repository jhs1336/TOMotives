package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LargeLocationController {
    @FXML
    private ImageView resizableImage;
    @FXML
    private ResizableImageController resizableImageController;
    @FXML
    public ResizableImageController smallImage1Controller;
    @FXML
    public ImageView smallImage1;
    @FXML
    public ResizableImageController smallImage2Controller;
    @FXML
    public ImageView smallImage2;
    @FXML
    public ResizableImageController smallImage3Controller;
    @FXML
    public ImageView smallImage3;
    @FXML
    public ResizableImageController smallImage4Controller;
    @FXML
    public ImageView smallImage4;
    @FXML
    public void initialize() {
        resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
        resizableImageController.applyRoundedCorners(10);

        System.out.println(smallImage1.getFitWidth() + " " + smallImage1.getFitHeight());
        smallImage1Controller.setImage(new Image(getClass().getResourceAsStream("images/testimage1.jpg")));
        smallImage1Controller.resize(smallImage1.getFitWidth(), smallImage1.getFitHeight());
        smallImage1Controller.applyRoundedCorners(10);
        smallImage2Controller.setImage(new Image(getClass().getResourceAsStream("images/testimage2.jpg")));
        smallImage2Controller.resize(smallImage2.getFitWidth(), smallImage2.getFitHeight());
        smallImage2Controller.applyRoundedCorners(10);
        smallImage3Controller.setImage(new Image(getClass().getResourceAsStream("images/testimage3.jpg")));
        smallImage3Controller.resize(smallImage3.getFitWidth(), smallImage3.getFitHeight());
        smallImage3Controller.applyRoundedCorners(10);
        smallImage4Controller.setImage(new Image(getClass().getResourceAsStream("images/testimage1.jpg")));
        smallImage4Controller.resize(smallImage4.getFitWidth(), smallImage4.getFitHeight());
        smallImage4Controller.applyRoundedCorners(10);
    }
}
