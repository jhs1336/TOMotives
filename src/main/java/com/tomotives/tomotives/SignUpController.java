package com.tomotives.tomotives;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.tomotives.tomotives.TOMotivesApplication.getStage;

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
    private void initialize() {
        resizableImageController.resize(resizableImage.getFitWidth(), resizableImage.getFitHeight());
        resizableImageController.applyRoundedCorners(60);

        // set up button handlers
        createAccountButton.setOnAction(event -> handleCreateAccount());
    }

    private void handleCreateAccount() {
        boolean isValid = validateInputs();

        if (isValid) {
            Gson gson = new Gson();
            JsonObject userJson = new JsonObject();
            userJson.addProperty("email", emailField.getText());
            userJson.addProperty("password", passwordField.getText());
            userJson.addProperty("firstName", firstNameField.getText());
            userJson.addProperty("lastName", lastNameField.getText());
            userJson.addProperty("displayName", displayNameField.getText());
            userJson.add("favorites", new JsonArray());
            userJson.add("recentlyViewed", new JsonArray());
            userJson.add("friends", new JsonArray());

            String json = gson.toJson(userJson);
            try {
                JsonArray usersJson;
                File usersFile = new File(getClass().getResource("/com/tomotives/tomotives/data/users.json").getFile());
                if (usersFile.exists()) {
                    Reader reader = new FileReader(usersFile);
                    usersJson = gson.fromJson(reader, JsonArray.class);
                    System.out.println("Users JSON: " + usersJson);
                    reader.close();
                } else {
                    usersJson = new JsonArray();
                }

                usersJson.add(gson.fromJson(json, JsonElement.class));

                FileWriter writer = new FileWriter(usersFile);
                System.out.println("Writing to file: " + usersJson);
                gson.toJson(usersJson, writer);
                writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // will take the account to the quiz to see their preferences
            ToastService.show(getStage(), "Created Account", ToastController.ToastType.SUCCESS);
        }
    }

    private boolean validateInputs() {
        if (emailField.getText().isEmpty() || !Pattern.compile("^(.+)@(\\S+)$").matcher(emailField.getText()).matches()) {
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
        } else if (firstNameField.getText().matches(".*\\d.*")) {
            showError("First Name cannot contain numbers.");
            return false;
        }

        if (lastNameField.getText().isEmpty()) {
            showError("Last name is required.");
            return false;
        } else if (lastNameField.getText().matches(".*\\d.*")) {
            showError("Last Name cannot contain numbers.");
            return false;
        }

        if(displayNameField.getText().isEmpty()) {
            showError("Display name is required");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        ToastService.show(getStage(), message, ToastController.ToastType.ERROR);
    }
}