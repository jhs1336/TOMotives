package com.tomotives.tomotives;

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
    public Button loginButton;

    @FXML
    public void initialize() {
        //set up listeners
        homeButton.setOnAction(event -> handleHomeButton());
        browseButton.setOnAction(event -> handleBrowseButton());
        favouritesButton.setOnAction(event -> handleFavouritesButton());
        signupButton.setOnAction(event -> handleSignupButton());
        loginButton.setOnAction(event -> handleLoginButton());
    }

    private void handleHomeButton() {
        TOMotivesApplication.loadPage("home.fxml");
    }

    private void handleBrowseButton() {

    }

    private void handleFavouritesButton() {

    }

    private void handleSignupButton() {
        TOMotivesApplication.loadPage("sign-up.fxml");
    }
    private void handleLoginButton() {

    }
}
