package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.LocationControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SmallLocationController extends LocationControllerBase {
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
            resizableImageController.setImage(new Image(Application.getResourceAsStream("images/" + imageUrl)));
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
        updateStarRating(starRating, starsContainer);
        updatePriceRating(priceRating, priceContainer);
    }
}
