/* The FavouritesAndRecentlyViewed class is the controller for the page favourites-and-recently-viewed.fxml which can display user favourites and recently viewed locations
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.models.User;
import com.tomotives.tomotives.services.LocationService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class FavouritesAndRecentlyViewed {

    @FXML
    private Label label;
    @FXML
    private SmallLocationCollectionController collectionController;

    private User user;

    /**Joshua
     * Initializes the controller
     * Loads the user's favorites or recently viewed locations based on the current page url formatted as favorites-and-recently-viewed/favourites/user or favourites-and-recently-viewed/recently-viewed/user
     * Updates the label text to display the user's display name and the type of locations being shown.
     */
    @FXML
    private void initialize() {
        // set collection to have 5 columns to display, to take up the screen
        collectionController.setColumns(5);

        // FORMATTED: favourites-and-recently-viewed/favourites or recently-viewed/user
        String[] parts = Application.getPage().split("/");
        if (parts.length == 3) {
            // get user from last term
            user = UserService.getUserFromDisplayName(parts[2]);
            if (parts[1].equals("favourites")) {
                loadUserFavourites(user);
                label.setText(user.getDisplayName() + " favourites");
            } else if (parts[1].equals("recently-viewed")) {
                loadUserRecentlyViewed(user);
                label.setText(user.getDisplayName() + " recently viewed");
            }
        }
    }

    /**Choeying
     * Loads the favourites of the user
     *
     * @param user the user to load the favourites from
     */
    private void loadUserFavourites(User user) {
        ArrayList<Location> locations = new ArrayList<>();
        for (String locationString : user.getFavourites()) {
            locations.add(LocationService.getLocation(locationString));
        }
        collectionController.loadLocations(locations);
    }

    /** Choeying
     *  Loads the recently viewed locations of the user
     *
     * @param user the user to load the recently viewed locations from
     */
    private void loadUserRecentlyViewed(User user) {
        ArrayList<Location> locations = new ArrayList<>();
        for (String locationString : user.getRecentLocations()) {
            locations.addFirst(LocationService.getLocation(locationString)); // add first so recent locations will be at back of list, and be added last (front of list in ui)
        }
        collectionController.loadLocations(locations);
    }
}