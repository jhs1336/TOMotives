package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.services.ToastService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.tomotives.tomotives.Application.getStage;

public class LoginController {
    @FXML
    private ImageView resizableImage;
    @FXML
    private ResizableImageController resizableImageController;

    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;

    @FXML
    private void initialize() {
        resizableImageController.setImage(new Image(getClass().getResourceAsStream("images/testimage1.jpg")));
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
                return;
            }
        }

        showError("Invalid email or password");
    }

    private void showError(String message) {
        ToastService.show(getStage(), message, ToastController.ToastType.ERROR);
    }
}
