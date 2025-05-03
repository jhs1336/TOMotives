package com.tomotives.tomotives;

import javafx.fxml.FXML;
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
        System.out.println("Updating star rating: " + rating);
        if (starsContainer != null) {
            for (int i = 0; i < starsContainer.getChildren().size(); i++) {
                if (starsContainer.getChildren().get(i) instanceof Label && ((Label) starsContainer.getChildren().get(i)).getText().equals("★")) {
                    Label star = (Label) starsContainer.getChildren().get(i);
                    if (i+1 <= rating) {
                        star.getStyleClass().remove("empty-star");
                        star.getStyleClass().remove("half-star");
                        star.getStyleClass().add("filled-star");
                        System.out.println("Filled star at index: " + i);
                    } else {
                        if (rating + 0.5 >= (i+1)) {
                            star.getStyleClass().remove("empty-star");
                            star.getStyleClass().remove("filled-star");
                            star.getStyleClass().add("half-star");
                            System.out.println("Half star at index: " + i);
                        } else {
                            star.getStyleClass().remove("filled-star");
                            star.getStyleClass().remove("half-star");
                            star.getStyleClass().add("empty-star");
                            System.out.println("Empty star at index: " + i);
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
                    if (i+1 <= rating) {
                        price.getStyleClass().remove("empty-price");
                        price.getStyleClass().remove("half-price");
                        price.getStyleClass().add("filled-price");
                    } else {
                        if (rating + 0.5 >= (i+1)) {
                            price.getStyleClass().remove("empty-price");
                            price.getStyleClass().remove("filled-price");
                            price.getStyleClass().add("half-price");
                        } else {
                            price.getStyleClass().remove("filled-price");
                            price.getStyleClass().remove("half-price");
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
