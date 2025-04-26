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
        smallLocationCarouselController.setTitle("Popular Locations");
        smallLocationCarouselController.addLocationDisplay(LocationDataManager.getLocation());
        smallLocationCarouselController.addLocationDisplay("Location 1", "testimage2.jpg", 4, 5, Category.Bars, Category.Cafe);
        smallLocationCarouselController.addLocationDisplay("Location 5", "testimage3.jpg", 5, 3, Category.Educational, Category.Hiking);
        smallLocationCarouselController.addLocationDisplay("Location 3", "testimage2.jpg", 5, 5, Category.DateSpot, Category.Lunch);
//        smallLocationCarouselController.addLocationDisplay("Location 4", "testimage1.jpg", 1, 4, "saul", "mesbur");
//        smallLocationCarouselController.addLocationDisplay("Location 2", "testimage3.jpg", 4, 2, "eee", "aaa");
    }

}
