/* The SmallLocationController class is the controller for the small-location-display.fxml component which is a display for a single location in the small location display format
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.LocationControllerBase;
import com.tomotives.tomotives.models.Location;
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

    /** Jessica
     * Initializes the object by resizing the resizableImage to the size needed and applying rounded corners
     */
    @FXML
    public void initialize() {
        if (resizableImageController != null) {
            resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
            resizableImageController.applyRoundedCorners(10);
        }
    }

    /**
     * Sets the location data for the UI in the small location component
     *
     * @param location the location object containing the data to be displayed
     */
    public void setLocationData(Location location) {
        setLocationData(location.getName(), location.getImage(), location.getRating(), location.getPrice(), location.getCategories().getFirst(), location.getCategories().get(1));
    }
    /**
     * Sets the location data for the UI in the small location component
     *
     * @param locationName the name of the location
     * @param imageUrl the URL of the location's image
     * @param starRating the rating of the location in stars
     * @param priceRating the price rating of the location
     * @param filter1 the first category filter for the location
     * @param filter2 the second category filter for the location
     */
    public void setLocationData(String locationName, String imageUrl, double starRating, double priceRating, Category filter1, Category filter2) {
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

        // update star and price ratings
        updateStarRating(starRating, starsContainer);
        updatePriceRating(priceRating, priceContainer);
    }
}
