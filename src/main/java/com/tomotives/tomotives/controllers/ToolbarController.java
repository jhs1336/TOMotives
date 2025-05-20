package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
    private Button loginButton;

    @FXML
    private Button profileButton;
    @FXML
    private void initialize() {
        //set up listeners
        homeButton.setOnAction(event -> handleHomeButton());
        browseButton.setOnAction(event -> handleBrowseButton());
        favouritesButton.setOnAction(event -> handleFavouritesButton());
        signupButton.setOnAction(event -> handleSignupButton());
        loginButton.setOnAction(event -> handleLoginButton());
        profileButton.setOnAction(event -> handleProfileButton());
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
    private void handleLoginButton() {
        Application.loadPage("login.fxml");
    }
}
