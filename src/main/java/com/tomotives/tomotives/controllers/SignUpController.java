/* The SignUpController class is the controller for the sign-up.fxml page which is the page where users can create accounts
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.User;
import com.tomotives.tomotives.services.ToastService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.regex.Pattern;

import static com.tomotives.tomotives.Application.getStage;

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
    private ImageView resizableImage;
    @FXML
    private ResizableImageController resizableImageController;

    @FXML
    private ToolbarController toolbarController;

    /** Jessica
     * Initializes the sign-up page by setting the proper size for the image and applying rounded corners to it, and making the create account button call the method
     */
    @FXML
    private void initialize() {
        resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
        resizableImageController.applyRoundedCorners(60);

        // set up button handlers
        createAccountButton.setOnAction(event -> handleCreateAccount()); // done by Joshua
    }

    /**Joshua
     * Handles the creation of a new user account
     * If all information provided is valid, creates a new user, sets the current user, refreshes the toolbar, and navigates to the appropriate page (if a url argument was provided)
     */
    private void handleCreateAccount() {
        boolean isValid = validateInputs();

        if (isValid) {
            User user = new User(emailField.getText(), firstNameField.getText(), lastNameField.getText(),  passwordField.getText(), displayNameField.getText());
            UserService.addUser(user);

            Application.setUser(user);
            toolbarController.refreshToolbar();

            // check if we're on the signup page with no additional params, (no page specified to route to after signing up)
            if (Application.getPage().equals("sign-up")) Application.loadPage("intro-survey.fxml");
            else if (Application.getPage().split("/")[1].equals("location")) Application.loadPage("location-detail-display.fxml", "location-detail-display/" + Application.getPage().split("/")[2]);
            ToastService.show(getStage(), "Created Account", ToastController.ToastType.SUCCESS);
        }
    }

    /**Saul
     * Validates the user input fields for the sign-up process
     * Checks that the email is valid, the password is at least 8 characters, the first and last names are not empty and do not contain numbers, the display name is not empty, and that the email and display name are not already in use.
     * If any of the validations fail, an error message is displayed in a toast
     *
     * @return true if all validations pass, false otherwise
     */
    private boolean validateInputs() {
        if (emailField.getText().isEmpty() || !Pattern.compile("^(.+)@(\\S+)$").matcher(emailField.getText()).matches()) { // checks if email is valid with an @
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
        } else if (firstNameField.getText().matches(".*\\d.*")) { // checks if first name contains numbers
            showError("First Name cannot contain numbers.");
            return false;
        }

        if (lastNameField.getText().isEmpty()) {
            showError("Last name is required.");
            return false;
        } else if (lastNameField.getText().matches(".*\\d.*")) { // checks if last name contains numbers
            showError("Last Name cannot contain numbers.");
            return false;
        }

        if (displayNameField.getText().isEmpty()) {
            showError("Display name is required");
            return false;
        }

        if (UserService.getUserFromEmail(emailField.getText()) != null) {
            showError("Email already in use.");
            return false;
        }
        if (UserService.getUserFromDisplayName(displayNameField.getText()) != null) {
            showError("Display name already in use.");
            return false;
        }

        return true;
    }

    /**Emmett
     * Displays an error message
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        ToastService.show(getStage(), message, ToastController.ToastType.ERROR);
    }
}