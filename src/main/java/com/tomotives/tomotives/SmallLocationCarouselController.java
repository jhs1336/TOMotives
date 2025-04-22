package com.tomotives.tomotives;

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
    private int displayCount = 4; // number of locations to display at once *not how many locations are in the carousel*
    private List<Pane> locationDisplays = new ArrayList<>();

    public void setDisplayCount(int count) {
        this.displayCount = Math.min(count, 4); // max display at once is 4
        updateCarousel();
    }

    public void setTitle(String title) {
        if (carouselTitle != null) {
            carouselTitle.setText(title);
        }
    }

    public void addLocationDisplay(String locationName, String imageUrl, int starRating, int priceRating, String filter1, String filter2) {
        try {
            // get fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("small-location-display.fxml"));
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

    @FXML
    private void goToPrevious() {
        if (locationDisplays.isEmpty()) {
            return;
        }
        currentIndex--;
        updateCarousel();
    }

    @FXML
    private void goToNext() {
        if (locationDisplays.isEmpty()) {
            return;
        }
        currentIndex++;
        updateCarousel();
    }

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
