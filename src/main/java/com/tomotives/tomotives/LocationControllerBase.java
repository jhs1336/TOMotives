package com.tomotives.tomotives;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LocationControllerBase {

    /**Joshua
     * Updates the visual representation of the star rating for a location
     *
     * @param rating The rating value to display, which can be a decimal value between 0 and 5
     * @param starsContainer The HBox container where the star icons are displayed
     */
    protected void updateStarRating(double rating, HBox starsContainer) {
        if (starsContainer != null) {
            for (int i = 0; i < starsContainer.getChildren().size(); i++) {
                if (starsContainer.getChildren().get(i) instanceof Label && ((Label) starsContainer.getChildren().get(i)).getText().equals("★")) {
                    Label star = (Label) starsContainer.getChildren().get(i);
                    star.getStyleClass().removeAll("filled-star", "half-star", "empty-star");
                    if (i+1 <= rating) {
                        star.getStyleClass().add("filled-star");
                    } else {
                        if (rating + 0.5 >= (i+1)) {
                            star.getStyleClass().add("half-star");
                        } else {
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
     * @param priceContainer The HBox container to update the price rating for
     */
    protected void updatePriceRating(double rating, HBox priceContainer) {
        if (priceContainer != null) {
            for (int i = 0; i < priceContainer.getChildren().size(); i++) {
                if (priceContainer.getChildren().get(i) instanceof Label && ((Label) priceContainer.getChildren().get(i)).getText().equals("$")) {
                    Label price = (Label) priceContainer.getChildren().get(i);
                    price.getStyleClass().removeAll("filled-price", "half-price", "empty-price");
                    if (i+1 <= rating) {
                        price.getStyleClass().add("filled-price");
                    } else {
                        if (rating + 0.5 >= (i+1)) {
                            price.getStyleClass().add("half-price");
                        } else {
                            price.getStyleClass().add("empty-price");
                        }
                    }
                }
            }
        }
    }

    /**Jessica
     * handles the click event of the filter buttons
     * @param event
     */
    @FXML
    protected void onFilterClicked(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Application.loadPage("search-result-page.fxml", "search-result-page/filter/" + clickedButton.getText());
    }

    /**Joshua
     * opens the location detail page
     * @param locationName the name of the location to open
     */
    @FXML
    protected void openLocationDetailPage(String locationName) {
        Application.loadPage("location-detail-display.fxml", "location-detail-display/" + locationName);
    }
}
