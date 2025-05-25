package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;

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
    @FXML
    private void initialize() {
        //set up listeners
        homeButton.setOnAction(event -> handleHomeButton());
        browseButton.setOnAction(event -> handleBrowseButton());
        favouritesButton.setOnAction(event -> handleFavouritesButton());
        signupButton.setOnAction(event -> handleSignupButton());
        loginAndProfileButton.setOnAction(event -> handleLoginAndProfileButtonClick());

        refreshToolbar();
    }

    public void refreshToolbar() {
        boolean userLoggedIn = Application.getUser() != null;
        signupButton.setManaged(!userLoggedIn);
        signupButton.setVisible(!userLoggedIn);
        divider.setManaged(!userLoggedIn);
        divider.setVisible(!userLoggedIn);
        loginAndProfileButton.setText(!userLoggedIn ? "Login" : Application.getUser().getDisplayName());
    }

    /**Joshua
     * Handles the click event of the home button by loading the home page.
     */
    private void handleHomeButton() {
        Application.loadPage("home.fxml");
    }

    private void handleBrowseButton() {
    }

    private void handleFavouritesButton() {

    }

    private void handleProfileButton() {
           // TOMotivesApplication.loadPage("profile.fxml");
    }

    /**Joshua
     * Handles the click event of the signup button by loading the sign-up page.
     */
    private void handleSignupButton() {
        Application.loadPage("sign-up.fxml");
    }
    /**Joshua
     * Handles the login button click event by loading the login page.
     */
    private void handleLoginAndProfileButtonClick() {
        if (Application.getUser() == null) {
            Application.loadPage("login.fxml");
        } else {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem profileMenuItem = new MenuItem("Profile");
            MenuItem logoutMenuItem = new MenuItem("Log Out");
            profileMenuItem.setStyle("-fx-font-size: 14px;");
            logoutMenuItem.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            contextMenu.setStyle("-fx-padding: 5px; -fx-border-color: #00a0b0; -fx-background-radius: 5px; -fx-border-radius: 5px;");


//            profileMenuItem.setOnAction(e -> Application.loadPage("profile.fxml"));
            logoutMenuItem.setOnAction(e -> {
                Application.setUser(null);
                Application.loadPage("login.fxml");
                refreshToolbar();
            });

            contextMenu.getItems().addAll(profileMenuItem, logoutMenuItem);
            contextMenu.show(loginAndProfileButton, Side.BOTTOM, 0, 0);
        }
    }
}
