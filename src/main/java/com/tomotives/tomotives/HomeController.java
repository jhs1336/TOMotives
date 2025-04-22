package com.tomotives.tomotives;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class HomeController {
    @FXML
    private AnchorPane smallLocationCarousel;
    @FXML
    private SmallLocationCarouselController smallLocationCarouselController;

    @FXML
    public void initialize() {
        System.out.println("HomeController initialized");
        smallLocationCarouselController.setTitle("Popular Locations");
        smallLocationCarouselController.addLocationDisplay("Location 1", "testimage2.jpg", 4, 5, "Filter 1", "Filter 2");
        smallLocationCarouselController.addLocationDisplay("Location 5", "testimage3.jpg", 5, 3, "yes 4", "no 2");
        smallLocationCarouselController.addLocationDisplay("Location 3", "testimage2.jpg", 5, 5, "f", "e");
        smallLocationCarouselController.addLocationDisplay("Location 4", "testimage1.jpg", 1, 4, "saul", "mesbur");
        smallLocationCarouselController.addLocationDisplay("Location 2", "testimage3.jpg", 4, 2, "eee", "aaa");
    }

}
