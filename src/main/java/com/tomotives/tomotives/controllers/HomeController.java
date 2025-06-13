/* The HomeController class is the controller for the home.fxml page which is the landing page of the application, and displays popular and recommended locations to the user
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

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
    private SmallLocationCarouselController smallLocationCarouselController;
    @FXML
    private LargeLocationCarouselController largeLocationCarouselController;

    /**Joshua
     * Initializes the home controller
     * Firstly sorts the list of all locations by rating and price, and displays the top 8 locations in the large location carousel
     * If a user is logged in, it displays the top 10 locations recommended for the user in the small location carousel.
     * If no user is logged in, it displays 10 random locations in the small location carousel.
     */
    @FXML
    public void initialize() {
        // get locations
        ArrayList<Location> locationList = LocationService.getLocationList();
        ArrayList<Location> sortedLocationList = new ArrayList<>(locationList);
        // sort by rating, and secondary sort by price (lowest wins)
        sortedLocationList.sort((a, b) -> {
            int ratingCompare = Double.compare(Math.round(b.getRating() * 10.0) / 10.0, Math.round(a.getRating() * 10.0) / 10.0);
            return ratingCompare != 0 ? ratingCompare : Double.compare(Math.round(a.getPrice() * 10.0) / 10.0, Math.round(b.getPrice() * 10.0) / 10.0);
        });

        // add top 8 locations to large location carousel
        for (int i = 0; i < 8; i++) {
            largeLocationCarouselController.addLocationDisplay(sortedLocationList.get(i));
        }

        if (Application.getUser() != null) {
            ArrayList<Location> recommendedLocations = new ArrayList<>(locationList);
            // remove locations badly rated as it's unlikely user will want to see them
            recommendedLocations.removeIf(location -> Application.getUser().getFavourites().contains(location.getName()) || location.getRating() < 1.5);

            // sort by relevance score
            recommendedLocations.sort((a, b) -> {
                double scoreA = calculateRelevanceScore(a, Application.getUser());
                double scoreB = calculateRelevanceScore(b, Application.getUser());
                return Double.compare(scoreB, scoreA);
            });

            // add top 10 locations as "Recommended for you"
            smallLocationCarouselController.setTitle("Recommended for You");
            for (int i = 0; i < Math.min(recommendedLocations.size(), 10); i++) {
                smallLocationCarouselController.addLocationDisplay(recommendedLocations.get(i));
            }
        } else {
            // if user is not logged in, suggest 10 random locations as "Other Locations"
            smallLocationCarouselController.setTitle("Other Locations");
            for (int i = 0; i < Math.min(locationList.size(), 10); i++) {
                int randomIndex = (int)(Math.random() * locationList.size());
                smallLocationCarouselController.addLocationDisplay(locationList.get(randomIndex));
                locationList.remove(randomIndex);
            }
        }
    }

    /**Joshua
     * Calculates a relevance score for a given location based on the user argument
     *
     * The score is calculated by:
     * - Boosting the score for matching categories between the location and the users liked categories
     * - Boosting the score for categories shared between the location and the users favorite locations
     * - Boosting the score for categories shared between the location and the users recently viewed locations
     * - Boosting the score if the location is in the user's friends favorites or recently viewed locations
     *
     * @param location The location to calculate the relevance score for
     * @param user The user to calculate the relevance score based off of
     * @return The calculated relevance score
     */
    private double calculateRelevanceScore(Location location, User user) {
        double score = 0;

        // boost score for matching categories
        if (user.getLikedCategories() != null) {
            for (int i = 0; i < user.getLikedCategories().size(); i++) {
                if (location.getCategories().contains(user.getLikedCategories().get(i))) {
                    score += 1.5;
                }
            }
        }

        // boost score for sharing categories with user's favorite locations
        if (user.getFavourites() != null) {
            for (String favourite : user.getFavourites()) {
                for (Category category : LocationService.getLocation(favourite).getCategories()) {
                    if (location.getCategories().contains(category)) {
                        score += .4;
                    }
                }
            }
        }

        // boost score for sharing categories with user's recently viewed locations
        if (user.getRecentLocations() != null) {
            for (String recentlyViewed : user.getRecentLocations()) {
                for (Category category : LocationService.getLocation(recentlyViewed).getCategories()) {
                    if (location.getCategories().contains(category)) {
                        score += .2;
                    }
                }
            }
        }

        // boost score if location is in friends' favorites or recently viewed
        if (user.getFriends() != null) {
            for (String friend : user.getFriends()) {
                User friendUser = UserService.getUserFromDisplayName(friend);
                if (friendUser.getFavourites().contains(location)) {
                    score += 1;
                }
                else if (friendUser.getRecentLocations().contains(location)) {
                    score += 0.1;
                }
            }
        }

        return score;
    }
}
