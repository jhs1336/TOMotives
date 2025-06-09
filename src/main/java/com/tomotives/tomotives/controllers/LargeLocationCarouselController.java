package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.models.Location;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LargeLocationCarouselController {
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

    @FXML
    public AnchorPane largeLocationDisplay;
    @FXML
    public LargeLocationController largeLocationDisplayController;

    private int currentIndex;
    private List<Map<String, Object>> locationDisplays = new ArrayList<>();

    /**Joshua
     * Navigates the carousel back one location (shifts one to left)
     */
    @FXML
    private void goToPrevious() {
        if (locationDisplays.isEmpty()) {
            return;
        }
        currentIndex--;
        // Handle wrap-around for negative index
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

    public void addLocationDisplay(String locationName, String imageUrl, double starRating, double priceRating, Category filter1, Category filter2, Category filter3, Category filter4) {
        // configure the location to display data given
        Map<String, Object> locationData = Map.of(
            "name", locationName,
            "image", imageUrl,
            "rating", starRating,
            "price", priceRating,
            "filter1", filter1,
            "filter2", filter2,
            "filter3", filter3,
            "filter4", filter4
        );
        locationDisplays.add(locationData);
        updateCarousel();
    }

    public void addLocationDisplay(Location location) {
        addLocationDisplay(location.getName(), location.getImage(), location.getRating(), location.getPrice(),
            !location.getCategories().isEmpty() ? location.getCategories().getFirst() : null,
            location.getCategories().size() > 1 ? location.getCategories().get(1) : null,
            location.getCategories().size() > 2 ? location.getCategories().get(2) : null,
            location.getCategories().size() > 3 ? location.getCategories().get(3) : null
        );
    }

    /**Joshua
     * Updates the carousel to display the current location based on the current index
     */
    public void updateCarousel() {
        Map<String, Object> location = locationDisplays.get(currentIndex);
        largeLocationDisplayController.setLocationData(location.get("name").toString(), location.get("image").toString(), (double) location.get("rating"), (double) location.get("price"), (Category) location.get("filter1"), (Category) location.get("filter2"), (Category) location.get("filter3"), (Category) location.get("filter4"));
    }
}
