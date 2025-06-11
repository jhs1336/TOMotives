package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.models.User;
import com.tomotives.tomotives.services.LocationService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class FavoritesAndRecentlyViewed {

    @FXML
    private Label label;
    @FXML
    SmallLocationCollectionController collectionController;

    private User user;

    @FXML
    private void initialize() {
        collectionController.setColumns(5);

        String[] parts = Application.getPage().split("/");
        if (parts.length == 3) {
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

    private void loadUserFavourites(User user) {
        ArrayList<Location> locations = new ArrayList<>();
        for (String locationString : user.getFavourites()) {
            locations.add(LocationService.getLocation(locationString));
        }
        collectionController.loadLocations(locations);
    }
    private void loadUserRecentlyViewed(User user) {
        ArrayList<Location> locations = new ArrayList<>();
        for (String locationString : user.getRecentLocations()) {
            locations.addFirst(LocationService.getLocation(locationString)); // add first so recent locations will be at back of list, and be added last (front of list in ui)
        }
        collectionController.loadLocations(locations);
    }
}