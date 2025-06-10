package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import javafx.fxml.FXML;
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
    private void initialize() {
        displayNameTextField.setText(Application.getUser().getDisplayName());
        emailTextField.setText(Application.getUser().getEmail());
        firstNameTextField.setText(Application.getUser().getFirstName());
        lastNameField.setText(Application.getUser().getLastName());
        passwordTextField.setText(Application.getUser().getPassword());

        // add friends to box
    }

}
