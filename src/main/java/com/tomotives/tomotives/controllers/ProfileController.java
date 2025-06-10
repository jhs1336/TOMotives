package com.tomotives.tomotives.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.User;
import com.tomotives.tomotives.services.ToastService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ProfileController {
    @FXML
    private TextField displayNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private VBox friendsBox;

    @FXML
    private ToolbarController toolbarController;

    @FXML
    private void initialize() {
        displayNameTextField.setText(Application.getUser().getDisplayName());
        emailTextField.setText(Application.getUser().getEmail());
        firstNameTextField.setText(Application.getUser().getFirstName());
        lastNameField.setText(Application.getUser().getLastName());
        passwordTextField.setText(Application.getUser().getPassword());

        // add friends to box
    }

    @FXML
    private void saveChanges() {
        try {
            User currentUser = Application.getUser();
            String oldDisplayName = currentUser.getDisplayName();

            // update user object with values from text fields
            currentUser.setDisplayName(displayNameTextField.getText());
            currentUser.setEmail(emailTextField.getText());
            currentUser.setFirstName(firstNameTextField.getText());
            currentUser.setLastName(lastNameField.getText());
            currentUser.setPassword(passwordTextField.getText());

            UserService.editUser(oldDisplayName, currentUser);

            // Show success message
            ToastService.show(Application.getStage(), "Profile Saved", ToastController.ToastType.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void signOut() {
        Application.setUser(null);
        toolbarController.refreshToolbar();
        Application.loadPage("login.fxml");
    }
}
