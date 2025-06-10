package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.LocationControllerBase;
import com.tomotives.tomotives.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class LargeLocationController extends LocationControllerBase {
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
    @FXML
    private Button filter3Button;
    @FXML
    private Button filter4Button;

    @FXML
    private Label priceRatingLabel;
    @FXML
    private Label starRatingLabel;

    @FXML
    public void initialize() {
        resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
        resizableImageController.applyRoundedCorners(10);
    }

    public void setLocationData(String locationName, String imageUrl, double starRating, double priceRating, Category filter1, Category filter2, Category filter3, Category filter4) {
        // update UI
        if (locationNameLabel != null) {
            locationNameLabel.setText(locationName);
            locationNameLabel.setOnAction(event -> openLocationDetailPage(locationName));
            Tooltip tooltip = new Tooltip(locationName);
            tooltip.setStyle("-fx-font-size: 10px;");
            locationNameLabel.setTooltip(tooltip);
        }
        if (resizableImageController != null && imageUrl != null) {
            resizableImageController.setImage(new Image(Application.getResourceAsStream("images/" + imageUrl)));
            resizableImageController.applyRoundedCorners(10);
        }
        if (filter1Button != null && filter1 != null) {
            filter1Button.setText(String.valueOf(filter1));
            filter1Button.setTooltip(new Tooltip(String.valueOf(filter1)));
        }
        if (filter2Button != null && filter2 != null) {
            filter2Button.setText(String.valueOf(filter2));
            filter2Button.setTooltip(new Tooltip(String.valueOf(filter2)));
        }
        if (filter3Button != null && filter3 != null) {
            filter3Button.setText(String.valueOf(filter3));
            filter3Button.setTooltip(new Tooltip(String.valueOf(filter3)));
        }
        if (filter4Button != null && filter4 != null) {
            filter4Button.setText(String.valueOf(filter4));
            filter4Button.setTooltip(new Tooltip(String.valueOf(filter4)));
        }

        // update star and price ratings
        updateStarRating(starRating, starsContainer);
        updatePriceRating(priceRating, priceContainer);
    }

    /**
     * Saul
     * @param rating The rating value to display, which can be a decimal value between 0 and the 5
     * @param priceContainer The HBox container to update the price rating for
     */
    @Override
    protected void updatePriceRating(double rating, HBox priceContainer) {
        super.updatePriceRating(rating, priceContainer);
        priceRatingLabel.setText(String.format("%.1f", rating));
    }

    /**
     * Saul
     * @param rating The rating value to display, which can be a decimal value between 0 and 5
     * @param starsContainer The HBox container where the star icons are displayed
     */
    @Override
    protected void updateStarRating(double rating, HBox starsContainer) {
        super.updateStarRating(rating, starsContainer);
        starRatingLabel.setText(String.format("%.1f", rating));
    }
}
