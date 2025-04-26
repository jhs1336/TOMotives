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
    public void initialize() {
        smallLocationCarouselController.setTitle("Popular Locations");
        ArrayList<Location> locationList = LocationDataManager.getLocationList();
        for (Location location : locationList) {
            smallLocationCarouselController.addLocationDisplay(location);
        }
    }

}
