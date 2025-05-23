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

    @FXML
    private void initialize() {
        resizableImageController.setImage(new Image(Application.getResourceAsStream("images/testimage1.jpg")));
        resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
        resizableImageController.applyRoundedCorners(60);
    }

    @FXML
    private void attemptLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (UserService.getUserFromEmail(email) != null) {
            if (UserService.getUserFromEmail(email).getPassword().equals(password)) {
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
    private void showError(String message) {
        ToastService.show(getStage(), message, ToastController.ToastType.ERROR);
    }
}
