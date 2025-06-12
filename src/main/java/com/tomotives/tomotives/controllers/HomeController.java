package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.models.User;
import com.tomotives.tomotives.services.LocationService;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
            ArrayList<Location> locationList = LocationService.getLocationList();
            ArrayList<Location> sortedLocationList = new ArrayList<>(locationList);
            sortedLocationList.sort((a, b) -> {
                int ratingCompare = Double.compare(Math.round(b.getRating() * 10.0) / 10.0, Math.round(a.getRating() * 10.0) / 10.0);
                return ratingCompare != 0 ? ratingCompare : Double.compare(Math.round(a.getPrice() * 10.0) / 10.0, Math.round(b.getPrice() * 10.0) / 10.0);
            });
            for (int i = 0; i < 8; i++) {
                largeLocationCarouselController.addLocationDisplay(sortedLocationList.get(i));
            }
            if (Application.getUser() != null) {
                ArrayList<Location> recommendedLocations = new ArrayList<>(locationList);
                recommendedLocations.removeIf(location -> Application.getUser().getFavourites().contains(location.getName()) || location.getRating() < 1.5);

                // sort by relevance score (combination of rating and matching categories)
                recommendedLocations.sort((a, b) -> {
                    double scoreA = calculateRelevanceScore(a, Application.getUser());
                    double scoreB = calculateRelevanceScore(b, Application.getUser());
                    return Double.compare(scoreB, scoreA);
                });

                smallLocationCarouselController.setTitle("Recommended for You");
                for (int i = 0; i < Math.min(recommendedLocations.size(), 10); i++) {
                    smallLocationCarouselController.addLocationDisplay(recommendedLocations.get(i));
                }
            } else {
                smallLocationCarouselController.setTitle("Other Locations");
                for (int i = 0; i < Math.min(locationList.size(), 10); i++) {
                    int randomIndex = (int)(Math.random() * locationList.size());
                    smallLocationCarouselController.addLocationDisplay(locationList.get(randomIndex));
                    locationList.remove(randomIndex);
                }
            }
        }

    private double calculateRelevanceScore(Location location, User user) {
        double score = 0;

        // boost score for matching categories
        for (int i = 0; i < user.getLikedCategories().size(); i++) {
            if (location.getCategories().contains(user.getLikedCategories().get(i))) {
                score += 1.5;
            }
        }

        // boost score for sharing categories with user's favorite locations
        for (String favourite : user.getFavourites()) {
            for (Category category : LocationService.getLocation(favourite).getCategories()) {
                if (location.getCategories().contains(category)) {
                    score += .4;
                }
            }
        }

        // boost score for sharing categories with user's recently viewed locations
        for (String recentlyViewed : user.getRecentLocations()) {
            for (Category category : LocationService.getLocation(recentlyViewed).getCategories()) {
                if (location.getCategories().contains(category)) {
                    score += .2;
                }
            }
        }

        // boost score if location is in friends' favorites or recently viewed
        for (String friend : user.getFriends()) {
            User friendUser = UserService.getUserFromDisplayName(friend);
            if (friendUser.getFavourites().contains(location)) {
                score += 1;
            }
            else if (friendUser.getRecentLocations().contains(location)) {
                score += 0.1;
            }
        }

        return score;
    }
}
