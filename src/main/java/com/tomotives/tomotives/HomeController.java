package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class HomeController {
    @FXML
    private AnchorPane smallLocationCarousel;
    @FXML
    private SmallLocationCarouselController smallLocationCarouselController;
    @FXML
    private LargeLocationCarouselController largeLocationCarouselController;

    @FXML
    public void initialize() {
        smallLocationCarouselController.setTitle("Popular Locations");
        ArrayList<Location> locationList = LocationDataManager.getLocationList();
        ArrayList<Location> randomLocations = new ArrayList<>(locationList);
        java.util.Collections.shuffle(randomLocations);
        for (int i = 0; i < 10; i++) {
            smallLocationCarouselController.addLocationDisplay(randomLocations.get(i));
        }
        for (int i = 0; i < 10; i++) {
            largeLocationCarouselController.addLocationDisplay(randomLocations.get(i));
        }
        largeLocationCarouselController.updateCarousel();

//        for (Location location : locationList) {
//            smallLocationCarouselController.addLocationDisplay(location);
//        }
    }
}
