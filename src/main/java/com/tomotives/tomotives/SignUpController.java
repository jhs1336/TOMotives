package com.tomotives.tomotives;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField displayNameField;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button browseButton;

    @FXML
    private Button favouritesButton;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView carouselImage;

    private List<Image> carouselImages;
    private int currentImageIndex = 0;
    private Timeline carouselTimeline;

    @FXML
    private void initialize() {
        // set up button handlers
        createAccountButton.setOnAction(event -> handleCreateAccount());
        homeButton.setOnAction(event -> handleHomeButton());
        browseButton.setOnAction(event -> handleBrowseButton());
        favouritesButton.setOnAction(event -> handleFavouritesButton());

        // set up carousel
        initializeCarousel();
        startCarousel();
    }

    private void initializeCarousel() {
        carouselImages = new ArrayList<>();

        try {
            carouselImages.add(new Image(getClass().getResourceAsStream("testimage1.jpg")));
            carouselImages.add(new Image(getClass().getResourceAsStream("testimage2.jpg")));
            carouselImages.add(new Image(getClass().getResourceAsStream("testimage3.jpg")));
        } catch (Exception e) {
            System.err.println("Error loading carousel images: " + e.getMessage());
        }
    }

    private void startCarousel() {
        carouselTimeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    currentImageIndex = (currentImageIndex + 1) % carouselImages.size();
                    carouselImage.setImage(carouselImages.get(currentImageIndex));
                })
        );
        carouselTimeline.setCycleCount(Timeline.INDEFINITE);
        carouselTimeline.play();
    }

    private void handleCreateAccount() {
        boolean isValid = validateInputs();

        if (isValid) {
            // will take the account to the quiz to see their preferences
        }
    }

    private boolean validateInputs() {
        if (emailField.getText().isEmpty() || Pattern.compile("^(.+)@(\\S+)$").matcher(emailField.getText()).matches()) {
            showError("Please enter a valid email address.");
            return false;
        }

        if (passwordField.getText().isEmpty() || passwordField.getText().length() < 8) {
            showError("Password must be at least 8 characters.");
            return false;
        }

        if (firstNameField.getText().isEmpty()) {
            showError("First name is required.");
            return false;
        } else if (firstNameField.getText().matches(".*\\d.*")) showError("First Name cannot contain numbers.");

        if (lastNameField.getText().isEmpty()) {
            showError("Last name is required.");
            return false;
        } else if (lastNameField.getText().matches(".*\\d.*")) showError("Last Name cannot contain numbers.");

        if(displayNameField.getText().isEmpty()) {
            showError("Display name is required");
        }

        return true;
    }

    private void showError(String message) {
        // popup toast for error
    }

    private void handleHomeButton() {

    }

    private void handleBrowseButton() {

    }

    private void handleFavouritesButton() {

    }
}