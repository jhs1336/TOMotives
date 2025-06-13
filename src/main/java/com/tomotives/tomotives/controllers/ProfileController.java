/* The ProfileController class is the controller for the profile.fxml page which is the page where a user view their details, and manage friends other connections
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.FriendStatus;
import com.tomotives.tomotives.models.User;
import com.tomotives.tomotives.services.ToastService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    private VBox peopleYouMayKnowBox;

    @FXML
    private Button addUserButton;

    @FXML
    private ToolbarController toolbarController;

    /** Saul
     * Initializes the controller by setting up the UI elements with data from the current user and adding the user's friends to the friendsBox
     * If the user is admin, it sets up the custom admin profile page
     */
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
        loadPeopleYouMayKnow();
    }

    /**Joshua
     * Loads the user's friends and displays them in the friendsBox
     * The friends are sorted by their friendship status friends first, then users requested, then received friend requests.
     * If there are no friends, a label is displayed accordingly
     */
    private void loadFriends() {
        // clear previous data
        friendsBox.getChildren().clear();

        Map<User, FriendStatus> friendListItems = new HashMap<>();
        for (User friend : UserService.getUserList()) {
            friendListItems.put(friend, UserService.getUserFriendshipStatus(Application.getUser(), friend));
        }
        // sort friends based on their ordinal value (puts them in order of the enum, FRIEND, REQUESTED, RECEIVED), and filters out users who are not friends
        Map<User, FriendStatus> sortedFriendListItems = friendListItems.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .filter(entry -> entry.getValue() != FriendStatus.NOT_FRIEND)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                java.util.LinkedHashMap::new
            ));

        friendListItems = sortedFriendListItems;
        if (friendListItems.isEmpty()) {
            Label noFriendsLabel = new Label("No users found");
            friendsBox.getChildren().add(noFriendsLabel);
            return;
        }

        // loop through the friends map
        for (Map.Entry<User, FriendStatus> entry : friendListItems.entrySet()) {
            // switch on the value of friend status
            switch (entry.getValue()) {
                case FRIEND -> {
                    Hyperlink userLink = new Hyperlink(entry.getKey().getDisplayName());
                    userLink.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333333;");
                    addContextMenu(userLink);
                    friendsBox.getChildren().add(userLink);
                }
                case REQUESTED -> {
                    Hyperlink userLink = new Hyperlink(entry.getKey().getDisplayName());
                    userLink.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");
                    addContextMenu(userLink);

                    Label label = new Label("Request Sent");
                    label.setStyle("-fx-text-fill: rgba(0, 0, 0, 0.6);");
                    HBox hBox = new HBox(userLink, label);
                    friendsBox.getChildren().addAll(hBox);
                }
                case RECEIVED -> {
                    Hyperlink userLink = new Hyperlink(entry.getKey().getDisplayName());
                    userLink.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");
                    addContextMenu(userLink);

                    Button acceptButton = new Button("✓");
                    acceptButton.getStyleClass().add("accept-button");
                    acceptButton.setTooltip(new Tooltip("Accept"));
                    acceptButton.setOnAction(e -> {
                        // add as friends
                        UserService.addFriend(displayNameTextField.getText(), userLink.getText());
                        Application.getUser().getFriends().add(userLink.getText());
                        ToastService.show(Application.getStage(), "Friend Added!", ToastController.ToastType.SUCCESS);
                        loadFriends();
                    });
                    Button rejectButton = new Button("✗");
                    rejectButton.getStyleClass().add("deny-button");
                    rejectButton.setTooltip(new Tooltip("Reject"));
                    rejectButton.setOnAction(e -> {
                        //remove from other user's list
                        UserService.removeFriend(userLink.getText(), displayNameTextField.getText());
                        Application.getUser().getFriends().remove(userLink.getText());
                        ToastService.show(Application.getStage(), "Friend Removed!", ToastController.ToastType.ERROR);
                        loadFriends();
                    });

                    HBox hBox = new HBox(userLink, acceptButton, rejectButton);
                    friendsBox.getChildren().addAll(hBox);
                }
                default -> {
                    break;
                }
            }
        }
    }

    /**Joshua
     * Loads people the current user may know based on similar liked categories, and mutual friends
     */
    private void loadPeopleYouMayKnow() {
        // clear previous data
        peopleYouMayKnowBox.getChildren().clear();

        Map<User, Integer> peopleUserMayKnow =  UserService.getPeopleUserMayKnow(Application.getUser());
        if (peopleUserMayKnow.isEmpty()) {
            Label noFriendsLabel = new Label("No users found");
            peopleYouMayKnowBox.getChildren().add(noFriendsLabel);
            return;
        }

        // loop through users map
        for (Map.Entry<User, Integer> entry : peopleUserMayKnow.entrySet()) {
            User user = entry.getKey();
            Hyperlink userLink = new Hyperlink(user.getDisplayName());
            userLink.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");
            addContextMenu(userLink);

            Label label = new Label(entry.getValue() + (entry.getValue() == 1 ? " connection" : " connections")); // connection for singular connections for plural
            label.setStyle("-fx-text-fill: rgba(0, 0, 0, 0.6);");

            Button addFriendButton = new Button("+");
            addFriendButton.getStyleClass().add("add-friend-button");
            addFriendButton.setTooltip(new Tooltip("Add Friend"));
            addFriendButton.setOnAction(e -> {
                // show popup to confirm friend request
                Popup popup = new Popup();
                popup.setAutoHide(true);
                // setup popup structure
                VBox popupContent = new VBox();
                popupContent.setStyle("-fx-border-color: #00a0b0;");
                popupContent.setSpacing(15);
                popupContent.setPadding(new Insets(20));
                popupContent.setMinWidth(400);
                popupContent.setMaxWidth(500);
                Label titleLabel = new Label("Add " + user.getDisplayName() + " as a friend?");
                titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
                titleLabel.getStyleClass().add("popup-title");

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
                cancelButton.setOnAction(ev -> {
                    popup.hide();
                });

                Button sendRequestButton = new Button("Send Request");
                sendRequestButton.styleProperty().set(NORMAL_BUTTON_STYLE);
                sendRequestButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        sendRequestButton.setStyle(HOVER_BUTTON_STYLE);
                    } else {
                        sendRequestButton.setStyle(NORMAL_BUTTON_STYLE);
                    }
                });
                sendRequestButton.setOnAction(ev -> {
                    UserService.addFriend(displayNameTextField.getText(), userLink.getText());
                    Application.getUser().getFriends().add(userLink.getText());
                    ToastService.show(Application.getStage(), "Friend request sent!", ToastController.ToastType.SUCCESS);
                    addFriendButton.setDisable(true);
                    loadFriends();
                    popup.hide();
                });

                buttonBox.getChildren().addAll(cancelButton, sendRequestButton);

                // add all components to the popup content
                popupContent.getChildren().addAll(titleLabel, buttonBox);

                popupContent.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
                // add content to popup
                popup.getContent().add(popupContent);

                // show the popup
                Window window = ((Node) e.getSource()).getScene().getWindow();
                popup.show(window, window.getX() + (window.getWidth() - popupContent.getMinWidth()) / 2, window.getY() + (window.getHeight() - 300) / 2);
            });

            HBox hBox = new HBox(userLink, label, addFriendButton);
            hBox.setSpacing(6);
            peopleYouMayKnowBox.getChildren().addAll(hBox);
        }
    }

    /**Joshua
     * Adds a context menu to the provided {@link Hyperlink} that allows the user to view the user's recently viewed items and favorites
     *
     * @param userLink The {@link Hyperlink} to add the context menu to
     */
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

    /** Joshua
     * Displays a popup window that allows the user to search for friends and send friend requests
     *
     * @param event The mouse event that triggered the method
     */
    @FXML
    private void searchForFriends(MouseEvent event) {
        Popup popup = new Popup();
        popup.setAutoHide(true);
        // setup popup structure
        VBox popupContent = new VBox();
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
            // ensure friend request is for valid user, if not show error toast
            if (friendSearchField.getText().isEmpty()) {
                ToastService.show(Application.getStage(), "Please enter a display name", ToastController.ToastType.ERROR);
                return;
            } else if (UserService.getUserFromDisplayName(friendSearchField.getText()) == null) {
                ToastService.show(Application.getStage(), "User not found", ToastController.ToastType.ERROR);
                return;
            }

            // send friend request
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

    /** Choeying
     * Saves the changes made to the user's profile and shows a success toast
     */
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
            toolbarController.refreshToolbar();

            // Show success message
            ToastService.show(Application.getStage(), "Profile Saved", ToastController.ToastType.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Jessica
     * Signs the current user out of the application and navigates to the login page
     */
    @FXML
    private void signOut() {
        Application.setUser(null);
        toolbarController.refreshToolbar();
        Application.loadPage("login.fxml");
    }

    /** Joshua
     * Displays a popup window to allow the admin to create a new account with specified details
     *
     * @param event The mouse event that triggered the popup.
     */
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

        // add details to be filled out about new user
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
            // check fields that would break other systems if inputted wrong, otherwise let admin have control
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
