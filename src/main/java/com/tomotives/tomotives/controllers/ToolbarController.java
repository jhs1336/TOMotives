/* The ToolbarController class is the controller for the toolbar.fxml component which is the top bar of the entire application
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 13, 2025
 */

package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.services.LocationService;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class ToolbarController {
    @FXML
    private Button homeButton;

    @FXML
    private Button browseButton;

    @FXML
    private Button favouritesButton;

    @FXML
    private TextField searchField;

    @FXML
    private Button signupButton;

    @FXML
    private Button loginAndProfileButton;
    @FXML
    private Label divider;

    /** Joshua
     * Initializes the controller by binding all action listeners for buttons, and setting search functionality
     */
    @FXML
    private void initialize() {
        //set up listeners
        homeButton.setOnAction(event -> handleHomeButton());
        browseButton.setOnAction(event -> handleBrowseButton());
        favouritesButton.setOnAction(event -> handleFavouritesButton());
        signupButton.setOnAction(event -> handleSignupButton());
        loginAndProfileButton.setOnAction(event -> handleLoginAndProfileButtonClick());

        refreshToolbar();

        // set up search field autocomplete
        ContextMenu searchContextMenu = new ContextMenu();
        searchContextMenu.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);");
        searchField.setContextMenu(searchContextMenu);

        // add listener for each time the text is changed
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                searchContextMenu.hide();
                return;
            }
            // if there is text, clear current items
            searchContextMenu.getItems().clear();
            ArrayList<String> locations = LocationService.getLocationNamesList();

            for (String location : locations) {
                // if location contains the current text, add as a menu item
                if (location.toLowerCase().contains(newValue.toLowerCase())) {
                    MenuItem item = new MenuItem(location);
                    item.setStyle("-fx-padding: 8; -fx-font-size: 14px; -fx-border-radius: 20;");
                    item.getStyleClass().add("button");
                    item.setOnAction(event -> {
                        Application.loadPage("location-detail-display.fxml", "location-detail-display/" + location);
                        searchContextMenu.hide();
                    });
                    // restrict amount of items to 15 (they will go off screen if there are more)
                    if (searchContextMenu.getItems().size() < 15) searchContextMenu.getItems().add(item);
                }
            }

            if (!searchContextMenu.getItems().isEmpty() && searchField.isFocused()) {
                searchContextMenu.show(searchField, Side.BOTTOM, 0, 0);
            } else {
                searchContextMenu.hide();
            }
        });

        searchField.setOnKeyReleased(event -> {
            if (event.getCode() != KeyCode.ENTER) return;
            if (searchField.getText().isEmpty()) {
                searchContextMenu.hide();
                return;
            }
            searchField.deselect();
            Application.loadPage("search-result-page.fxml", "search-result-page/search/" + searchField.getText());
        });

        searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                searchContextMenu.hide();
            }
        });
    }

    /** Joshua
     * Refreshes the toolbar by updating the visibility and text of the login and profile button based on whether the user is logged in
     */
    public void refreshToolbar() {
        boolean userLoggedIn = Application.getUser() != null;
        signupButton.setManaged(!userLoggedIn);
        signupButton.setVisible(!userLoggedIn);
        divider.setManaged(!userLoggedIn);
        divider.setVisible(!userLoggedIn);
        loginAndProfileButton.setText(!userLoggedIn ? "Login" : Application.getUser().getDisplayName());
    }

    /**Choeying
     * Handles the click event of the home button by loading the home page.
     */
    private void handleHomeButton() {
        Application.loadPage("home.fxml");
    }

    /** Choeying
     * Handles the click event of the browse button by loading the browse page.
     */
    private void handleBrowseButton() {
        Application.loadPage("browse.fxml");
    }

    /**Jessica
     * If the user is not logged in, shows the popup to login.
     * Otherwise, loads the favourites page with the information needed
     */
    private void handleFavouritesButton() {
        if (Application.getUser() == null) {
            Application.showLoginOrSignupPopup();
        } else {
            Application.loadPage("favourites-and-recently-viewed.fxml", "favourites-and-recently-viewed/favourites/" + Application.getUser().getDisplayName()); // Joshua did this
        }
    }

    /**Jessica
     * Handles the click event of the signup button by loading the sign-up page.
     */
    private void handleSignupButton() {
        Application.loadPage("sign-up.fxml");
    }

    /**Joshua
     * Handles the login or profile button click event
     * If the user is not logged in, loads the login page.
     * Otherwise, shows a context menu with profile, recently viewed, and logout options
     */
    private void handleLoginAndProfileButtonClick() {
        if (Application.getUser() == null) {
            // user is not logged in, load login page
            Application.loadPage("login.fxml");
        } else {
            // user is logged in, show context menu about their account
            ContextMenu contextMenu = new ContextMenu();
            MenuItem profileMenuItem = new MenuItem("Profile");
            MenuItem recentlyViewedMenuItem = new MenuItem("Recent");
            MenuItem logoutMenuItem = new MenuItem("Log Out");

            profileMenuItem.getStyleClass().add("button");
            recentlyViewedMenuItem.getStyleClass().add("button");
            logoutMenuItem.getStyleClass().add("button");

            profileMenuItem.setStyle("-fx-font-size: 14px;");
            recentlyViewedMenuItem.setStyle("-fx-font-size: 14px;");
            logoutMenuItem.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            contextMenu.setStyle("-fx-padding: 5px; -fx-border-color: #00a0b0; -fx-background-radius: 5px; -fx-border-radius: 5px;");

            profileMenuItem.setOnAction(e -> Application.loadPage("profile.fxml"));
            recentlyViewedMenuItem.setOnAction(e -> Application.loadPage("favourites-and-recently-viewed.fxml", "favourites-and-recently-viewed/recently-viewed/" + Application.getUser().getDisplayName()));
            logoutMenuItem.setOnAction(e -> {
                Application.setUser(null);
                refreshToolbar();
                if (Application.getPage().equals("profile")) Application.loadPage("home.fxml"); // if no other page set to be added from URL, load home page
            });

            contextMenu.getItems().addAll(profileMenuItem, recentlyViewedMenuItem, logoutMenuItem);
            contextMenu.show(loginAndProfileButton, Side.BOTTOM, 0, 0);
        }
    }
}
