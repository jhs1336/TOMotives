package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SmallLocationController {
    @FXML
    private ImageView resizableImage;
    @FXML
    private ResizableImageController resizableImageController;
    @FXML
    private Hyperlink locationNameLabel;
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
    private double starRating;
    private double priceRating;
    private Category filter1;
    private Category filter2;

    @FXML
    public void initialize() {
        if (resizableImageController != null) {
            resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
            resizableImageController.applyRoundedCorners(10);
        }
    }

    public void setLocationData(String locationName, String imageUrl, double starRating, double priceRating, Category filter1, Category filter2) {
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
            Tooltip tooltip = new Tooltip(locationName);
            tooltip.setStyle("-fx-font-size: 10px;");
            locationNameLabel.setTooltip(tooltip);
        }
        if (resizableImageController != null && imageUrl != null) {
            resizableImageController.setImage(new Image(getClass().getResourceAsStream("images/" + imageUrl)));
            resizableImageController.applyRoundedCorners(10);
        }
        if (filter1Button != null && filter1 != null) {
            filter1Button.setText(filter1.getName());
            filter1Button.setTooltip(new Tooltip(filter1.getName()));
        }
        if (filter2Button != null && filter2 != null) {
            filter2Button.setText(filter2.getName());
            filter2Button.setTooltip(new Tooltip(filter2.getName()));
        }

        // update star and price ratings
        updateStarRating(starRating);
        updatePriceRating(priceRating);
    }

    /**Joshua
     * Updates the visual representation of the star rating for a location
     *
     * @param rating The rating value to display, which can be a decimal value between 0 and 5
     */
    private void updateStarRating(double rating) {
        if (starsContainer != null) {
            for (int i = 0; i < starsContainer.getChildren().size(); i++) {
                if (starsContainer.getChildren().get(i) instanceof Label) {
                    Label star = (Label) starsContainer.getChildren().get(i);
                    if (i+1 < rating) {
                        star.getStyleClass().remove("empty-star");
                        star.getStyleClass().add("filled-star");
                    } else {
                        if (rating + 0.5 > (i+1)) {
                            star.getStyleClass().remove("empty-star");
                            star.getStyleClass().remove("filled-star");
                            star.getStyleClass().add("half-star");
                        } else {
                            star.getStyleClass().remove("filled-star");
                            star.getStyleClass().add("empty-star");
                        }
                    }
                }
            }
        }
    }

    /**Joshua
     * Updates the visual representation of the price rating for a location
     *
     * @param rating The rating value to display, which can be a decimal value between 0 and the 5
     */
        private void updatePriceRating(double rating) {
        if (priceContainer != null) {
            for (int i = 0; i < priceContainer.getChildren().size(); i++) {
                if (priceContainer.getChildren().get(i) instanceof Label) {
                    Label price = (Label) priceContainer.getChildren().get(i);
                    if (i+1 < rating) {
                        price.getStyleClass().remove("empty-price");
                        price.getStyleClass().add("filled-price");
                    } else {
                        if (rating + 0.5 > (i+1)) {
                            price.getStyleClass().remove("empty-price");
                            price.getStyleClass().remove("filled-price");
                            price.getStyleClass().add("half-price");
                        } else {
                            price.getStyleClass().remove("filled-price");
                            price.getStyleClass().add("empty-price");
                        }
                    }
                }
            }
        }
    }

    @FXML
    public void openLocationDetailPage() {
        //TOMotivesApplication.loadPage();
    }
}
