package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ProfileController {
    @FXML
    TextField displayNameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameField;
    @FXML
    PasswordField passwordTextField;

    @FXML
    private void initialize() {
        displayNameTextField.setText(Application.getUser().getDisplayName());
        emailTextField.setText(Application.getUser().getEmail());
        firstNameTextField.setText(Application.getUser().getFirstName());
        lastNameField.setText(Application.getUser().getLastName());
        passwordTextField.setText(Application.getUser().getPassword());


    }
}
