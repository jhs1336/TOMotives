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

    @FXML
    public void initialize() {
        if (resizableImageController != null) {
            resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
            resizableImageController.applyRoundedCorners(10);
        }
    }

    /**
     *
     * @param locationName
     * @param imageUrl
     * @param starRating
     * @param priceRating
     * @param filter1
     * @param filter2
     */
    public void setLocationData(String locationName, String imageUrl, double starRating, double priceRating, Category filter1, Category filter2) {
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
