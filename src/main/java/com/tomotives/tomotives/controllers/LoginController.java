/* The LoginController class is the controller for the login.fxml page which is the page where a user can login to their account
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.User;
import com.tomotives.tomotives.services.ToastService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import static com.tomotives.tomotives.Application.getStage;

public class LoginController {
    @FXML
    private ImageView resizableImage;
    @FXML
    private ResizableImageController resizableImageController;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private ToolbarController toolbarController;

    /**Choeying
     * Initializes the login page by setting the proper size for the image and applying rounded corners to it
     */
    @FXML
    private void initialize() {
        resizableImageController.setImage(new Image(Application.getResourceAsStream("images/cntower1.jpg")));
        resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
        resizableImageController.applyRoundedCorners(60);
    }

    /**Joshua
     * Attempts to log in the user with the provided email and password
     * If the login is successful, it shows a success toast, sets the current user, refreshes the toolbar, and navigates to the appropriate page (if a url argument was provided)
     * If the login is unsuccessful, it shows an error toast
     */
    @FXML
    private void attemptLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User user = UserService.getUserFromEmail(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                ToastService.show(getStage(), "Login successful", ToastController.ToastType.SUCCESS);
                Application.setUser(UserService.getUserFromEmail(email));
                toolbarController.refreshToolbar();
                // check if we're on the login page with no additional params, (no page specified to route to after login)
                if (Application.getPage().equals("login")) Application.loadPage("home.fxml");
                else if (Application.getPage().split("/")[1].equals("location")) Application.loadPage("location-detail-display.fxml", "location-detail-display/" + Application.getPage().split("/")[2]);
                return;
            }
        }

        showError("Invalid email or password");
    }

    /** Emmett
     * Displays an error toast message with the provided message text
     *
     * @param message The error message to display
     */
    private void showError(String message) {
        ToastService.show(getStage(), message, ToastController.ToastType.ERROR);
    }
}
