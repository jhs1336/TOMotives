package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SmallLocationController {
    @FXML
    private ImageView resizableImage;
    @FXML
    private ResizableImageController resizableImageController;
    @FXML
    private Label locationNameLabel;
    @FXML
    private HBox starsContainer;
    @FXML
    private HBox priceContainer;
    @FXML
    private Button filter1Button;
    @FXML
    private Button filter2Button;

    // will be used later for data collection
    private String locationName;
    private String imageUrl;
    private int starRating;
    private int priceRating;
    private String filter1;
    private String filter2;

    @FXML
    public void initialize() {
        if (resizableImageController != null) {
            resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
        }
    }

    public void setLocationData(String locationName, String imageUrl, int starRating, int priceRating, String filter1, String filter2) {
        // store the data
        this.locationName = locationName;
        this.imageUrl = imageUrl;
        this.starRating = starRating;
        this.priceRating = priceRating;
        this.filter1 = filter1;
        this.filter2 = filter2;

        // update UI
        if (locationNameLabel != null) {
            locationNameLabel.setText(locationName);
        }
        if (resizableImageController != null && imageUrl != null) {
            resizableImageController.setImage(new Image(getClass().getResourceAsStream("Images/" + imageUrl)));
        }
        if (filter1Button != null && filter1 != null) {
            filter1Button.setText(filter1);
        }
        if (filter2Button != null && filter2 != null) {
            filter2Button.setText(filter2);
        }

        // update star and price ratings
        updateStarRating(starRating);
        updatePriceRating(priceRating);
    }

    private void updateStarRating(int rating) {
        if (starsContainer != null) {
            for (int i = 0; i < starsContainer.getChildren().size(); i++) {
                if (starsContainer.getChildren().get(i) instanceof Label) {
                    Label star = (Label) starsContainer.getChildren().get(i);
                    if (i < rating) {
                        star.getStyleClass().remove("empty-star");
                        star.getStyleClass().add("filled-star");
                    } else {
                        star.getStyleClass().remove("filled-star");
                        star.getStyleClass().add("empty-star");
                    }
                }
            }
        }
    }

    private void updatePriceRating(int rating) {
        if (priceContainer != null) {
            for (int i = 0; i < priceContainer.getChildren().size(); i++) {
                if (priceContainer.getChildren().get(i) instanceof Label) {
                    Label price = (Label) priceContainer.getChildren().get(i);
                    if (i < rating) {
                        price.getStyleClass().remove("empty-price");
                        price.getStyleClass().add("filled-price");
                    } else {
                        price.getStyleClass().remove("filled-price");
                        price.getStyleClass().add("empty-price");
                    }
                }
            }
        }
    }
}
