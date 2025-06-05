package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.services.LocationService;
import com.tomotives.tomotives.models.Location;
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

    /**
     *
     */
    @FXML
    public void initialize() {
        smallLocationCarouselController.setTitle("Popular Locations");
        ArrayList<Location> locationList = LocationService.getLocationList();
        ArrayList<Location> randomLocations = new ArrayList<>(locationList);
        randomLocations.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));

        for (int i = 0; i < 10; i++) {
            smallLocationCarouselController.addLocationDisplay(randomLocations.get(randomLocations.size() - i - 1));
        }
        for (int i = 0; i < 10; i++) {
            largeLocationCarouselController.addLocationDisplay(randomLocations.get(i));
        }
    }
}
