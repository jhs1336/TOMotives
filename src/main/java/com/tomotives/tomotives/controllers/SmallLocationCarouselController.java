package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.models.Location;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmallLocationCarouselController {
    @FXML
    private AnchorPane carouselRoot;
    @FXML
    private HBox locationsContainer;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label carouselTitle;

    private int currentIndex;
    private int displayCount = 5; // number of locations to display at once *not how many locations are in the carousel*
    private List<Pane> locationDisplays = new ArrayList<>();

    /**Joshua
     * Sets the number of locations to show in the carousel at once
     * Max is 5. Will be set to high if given a number greater
     *
     * @param count the number of locations to show
     */
    public void setDisplayCount(int count) {
        this.displayCount = Math.min(count, 5); // max display at once is 5
        updateCarousel();
    }

    /**Joshua
     * Sets the title of the carousel
     *
     * @param title the new title to set for the carousel
     */
    public void setTitle(String title) {
        if (carouselTitle != null) {
            carouselTitle.setText(title);
        }
    }

    public void addLocationDisplay(String locationName, String imageUrl, double starRating, double priceRating, Category filter1, Category filter2) {
        try {
            // get fxml
            FXMLLoader loader = new FXMLLoader(Application.getResource("small-location-display.fxml"));
            Pane locationDisplay = loader.load();
            SmallLocationController controller = loader.getController();

            // configure the location to display data given
            controller.setLocationData(locationName, imageUrl, starRating, priceRating, filter1, filter2);

            // add to carousel and update
            locationDisplays.add(locationDisplay);
            updateCarousel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addLocationDisplay(Location location) {
        addLocationDisplay(location.getName(), location.getImage(), location.getRating(), location.getPrice(), location.getCategories().getFirst(), location.getCategories().get(1));
    }

    /**Joshua
     * Navigates the carousel back one location (shifts one to left)
     */
    @FXML
    private void goToPrevious() {
        if (locationDisplays.isEmpty()) {
            return;
        }
        currentIndex--;
        // handle wrap-around for negative index
        if (currentIndex < 0) {
            currentIndex = locationDisplays.size() - 1;
        }
        updateCarousel();
    }


    /**Joshua
     * Navigates the carousel forward one location (shifts one to the right)
     */
    @FXML
    private void goToNext() {
        if (locationDisplays.isEmpty()) {
            return;
        }
        if (currentIndex >= locationDisplays.size() - 1) {
            currentIndex = 0;
        } else currentIndex++;
        updateCarousel();
    }

    /**Joshua
     * Updates the carousel to show the appropriate locations based on the current index
     * If there are fewer location displays than the display count, all displays are shown
     * Otherwise, the displays starting from the current index are shown, up to the display count
     */
    private void updateCarousel() {
        if (locationsContainer != null && !locationDisplays.isEmpty()) {
            locationsContainer.getChildren().clear();

            // if there are fewer items than display count, show all items
            if (locationDisplays.size() <= displayCount) {
                locationsContainer.getChildren().addAll(locationDisplays);
                return;
            }

            // otherwise show the items starting from currentIndex
            int itemsToShow = Math.min(displayCount, locationDisplays.size());
            for (int i = 0; i < itemsToShow; i++) {
                int index = (currentIndex + i) % locationDisplays.size();
                locationsContainer.getChildren().add(locationDisplays.get(index));
            }
        }
    }
}
