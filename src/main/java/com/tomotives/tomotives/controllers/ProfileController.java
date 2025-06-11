package com.tomotives.tomotives.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.FriendStatus;
import com.tomotives.tomotives.models.User;
import com.tomotives.tomotives.services.LocationService;
import com.tomotives.tomotives.services.ToastService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.io.IOException;

import static com.tomotives.tomotives.Application.*;

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
    private Button addUserButton;

    @FXML
    private ToolbarController toolbarController;

    @FXML
    private void initialize() {
        if (Application.getUser().getDisplayName().equals("Admin")) {
            addUserButton.setManaged(true);
            firstNameTextField.getParent().getChildrenUnmodifiable().forEach(node -> node.setManaged(false));
            lastNameField.getParent().getChildrenUnmodifiable().forEach(node -> node.setManaged(false));
            firstNameTextField.getParent().getParent().setManaged(false);
        }

        displayNameTextField.setText(Application.getUser().getDisplayName());
        emailTextField.setText(Application.getUser().getEmail());
        firstNameTextField.setText(Application.getUser().getFirstName());
        lastNameField.setText(Application.getUser().getLastName());
        passwordTextField.setText(Application.getUser().getPassword());

        loadFriends();
    }

    private void loadFriends() {
        friendsBox.getChildren().clear();

        for (User friend : UserService.getUserList()) {
            switch (UserService.getUserFriendshipStatus(Application.getUser(), friend)) {
                case FRIEND -> {
                    Hyperlink userLink = new Hyperlink(friend.getDisplayName());
                    userLink.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333333;");
                    addContextMenu(userLink);
                    friendsBox.getChildren().add(userLink);
                }
                case REQUESTED -> {
                    Hyperlink userLink = new Hyperlink(friend.getDisplayName());
                    userLink.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");
                    addContextMenu(userLink);
                    Label label = new Label("Request Sent");
                    label.setStyle("-fx-text-fill: rgba(0, 0, 0, 0.6);");
                    HBox hBox = new HBox(userLink, label);
                    friendsBox.getChildren().addAll(hBox);
                }
                case RECEIVED -> {
                    Hyperlink userLink = new Hyperlink(friend.getDisplayName());
                    userLink.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");
                    addContextMenu(userLink);
                    Button acceptButton = new Button("✓");
                    acceptButton.getStyleClass().add("accept-button");
                    acceptButton.setTooltip(new Tooltip("Accept"));
                    Button rejectButton = new Button("✗");
                    rejectButton.getStyleClass().add("deny-button");
                    rejectButton.setTooltip(new Tooltip("Reject"));
                    HBox hBox = new HBox(userLink, acceptButton, rejectButton);
                    friendsBox.getChildren().addAll(hBox);
                }
                default -> {
                    break;
                }
            }
        }
    }

    private void addContextMenu(Hyperlink userLink) {
        userLink.setOnAction(event -> {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem recentlyViewedMenuItem = new MenuItem("View Recent");
            MenuItem favouritesMenuItem = new MenuItem("View Favourites");
            recentlyViewedMenuItem.getStyleClass().add("button");
            favouritesMenuItem.getStyleClass().add("button");
            recentlyViewedMenuItem.setStyle("-fx-font-size: 14px;");
            favouritesMenuItem.setStyle("-fx-font-size: 14px;");
            contextMenu.setStyle("-fx-padding: 5px; -fx-border-color: #00a0b0; -fx-background-radius: 5px; -fx-border-radius: 5px;");

            recentlyViewedMenuItem.setOnAction(e -> Application.loadPage("favourites-and-recently-viewed.fxml", "favourites-and-recently-viewed/recently-viewed/" + userLink.getText()));
            favouritesMenuItem.setOnAction(e -> Application.loadPage("favourites-and-recently-viewed.fxml", "favourites-and-recently-viewed/favourites/" + userLink.getText()));

            contextMenu.getItems().addAll(recentlyViewedMenuItem, favouritesMenuItem);
            contextMenu.show(userLink, Side.BOTTOM, 0, 0);
        });
    }

    @FXML
    private void searchForFriends(MouseEvent event) {
        Popup popup = new Popup();
        popup.setAutoHide(true);
        // setup popup structure
        VBox popupContent = new VBox();
        popupContent.getStyleClass().add("review-popup");
        popupContent.setStyle("-fx-border-color: #00a0b0;");
        popupContent.setSpacing(15);
        popupContent.setPadding(new Insets(20));
        popupContent.setMinWidth(400);
        popupContent.setMaxWidth(500);

        Label titleLabel = new Label("Friend Search");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.getStyleClass().add("popup-title");

        VBox contentBox = new VBox();
        contentBox.setSpacing(10);
        TextField friendSearchField = new TextField();
        friendSearchField.setPromptText("Search a display name");
        contentBox.getChildren().add(friendSearchField);

        // buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button cancelButton = new Button("Cancel");
        cancelButton.styleProperty().set(NORMAL_BUTTON_STYLE);
        cancelButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                cancelButton.setStyle(HOVER_BUTTON_STYLE);
            } else {
                cancelButton.setStyle(NORMAL_BUTTON_STYLE);
            }
        });
        cancelButton.setOnAction(e -> {
            popup.hide();
        });
        cancelButton.setOnAction(e -> popup.hide());

        Button sendRequestButton = new Button("Send Request");
        sendRequestButton.styleProperty().set(NORMAL_BUTTON_STYLE);
        sendRequestButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sendRequestButton.setStyle(HOVER_BUTTON_STYLE);
            } else {
                sendRequestButton.setStyle(NORMAL_BUTTON_STYLE);
            }
        });
        sendRequestButton.setOnAction(e -> {
            if (friendSearchField.getText().isEmpty()) {
                ToastService.show(Application.getStage(), "Please enter a display name", ToastController.ToastType.ERROR);
                return;
            } else if (UserService.getUserFromDisplayName(friendSearchField.getText()) == null) {
                ToastService.show(Application.getStage(), "User not found", ToastController.ToastType.ERROR);
                return;
            }
            UserService.addFriend(displayNameTextField.getText(), friendSearchField.getText());
            Application.getUser().getFriends().add(friendSearchField.getText());
            ToastService.show(Application.getStage(), "Friend request sent!", ToastController.ToastType.SUCCESS);
            loadFriends();
            popup.hide();
        });

        buttonBox.getChildren().addAll(cancelButton, sendRequestButton);

        // add all components to the popup content
        popupContent.getChildren().addAll(titleLabel, contentBox, buttonBox);

        popupContent.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
        // add content to popup
        popup.getContent().add(popupContent);

        // show the popup
        Window window = ((Node) event.getSource()).getScene().getWindow();
        popup.show(window, window.getX() + (window.getWidth() - popupContent.getMinWidth()) / 2, window.getY() + (window.getHeight() - 300) / 2);
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

    @FXML
    private void addUser(MouseEvent event) {
        Popup popup = new Popup();
        popup.setAutoHide(true);
        // setup popup structure
        VBox popupContent = new VBox();
        popupContent.getStyleClass().add("review-popup");
        popupContent.setStyle("-fx-border-color: #00a0b0;");
        popupContent.setSpacing(15);
        popupContent.setPadding(new Insets(20));
        popupContent.setMinWidth(400);
        popupContent.setMaxWidth(500);

        Label titleLabel = new Label("Add User");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.getStyleClass().add("popup-title");

        VBox contentBox = new VBox();
        contentBox.setSpacing(10);
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField displayNameField = new TextField();
        displayNameField.setPromptText("Display Name");
        contentBox.getChildren().addAll(emailField, passwordField, firstNameField, lastNameField, displayNameField);

        // buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button cancelButton = new Button("Cancel");
        cancelButton.styleProperty().set(NORMAL_BUTTON_STYLE);
        cancelButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                cancelButton.setStyle(HOVER_BUTTON_STYLE);
            } else {
                cancelButton.setStyle(NORMAL_BUTTON_STYLE);
            }
        });
        cancelButton.setOnAction(e -> {
            popup.hide();
        });
        cancelButton.setOnAction(e -> popup.hide());

        Button createAccountButton = new Button("Create Account");
        createAccountButton.styleProperty().set(NORMAL_BUTTON_STYLE);
        createAccountButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                createAccountButton.setStyle(HOVER_BUTTON_STYLE);
            } else {
                createAccountButton.setStyle(NORMAL_BUTTON_STYLE);
            }
        });
        createAccountButton.setOnAction(e -> {
            if (UserService.getUserFromEmail(emailField.getText()) != null) {
                ToastService.show(Application.getStage(), "Email already in use", ToastController.ToastType.ERROR);
                return;
            }
            if (UserService.getUserFromDisplayName(displayNameField.getText()) != null) {
                ToastService.show(Application.getStage(), "Display name already in use", ToastController.ToastType.ERROR);
                return;
            }
            User user = new User(emailField.getText(), firstNameField.getText(), lastNameField.getText(),  passwordField.getText(), displayNameField.getText());
            UserService.addUser(user);
            ToastService.show(Application.getStage(), "Account Created", ToastController.ToastType.SUCCESS);
            popup.hide();
        });

        buttonBox.getChildren().addAll(cancelButton, createAccountButton);

        // add all components to the popup content
        popupContent.getChildren().addAll(titleLabel, contentBox, buttonBox);

        popupContent.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
        // add content to popup
        popup.getContent().add(popupContent);

        // show the popup
        Window window = ((Node) event.getSource()).getScene().getWindow();
        popup.show(window, window.getX() + (window.getWidth() - popupContent.getMinWidth()) / 2, window.getY() + (window.getHeight() - 300) / 2);
    }
}
