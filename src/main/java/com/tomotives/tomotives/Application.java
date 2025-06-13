/* The Application class is the main class for the application, which has many helper methods used by other classes like loading resources and pages
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives;

import com.tomotives.tomotives.models.User;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Application extends javafx.application.Application {
    private static Scene scene;
    private static Stage stage;
    private static User user;
    private static String page;

    public static final String NORMAL_BUTTON_STYLE = "-fx-background-color: #00a0b0; -fx-text-fill: white; -fx-background-radius: 25px; -fx-font-size: 14px;";
    public static final String HOVER_BUTTON_STYLE = "-fx-background-color: #008b9c; -fx-text-fill: white; -fx-background-radius: 25px; -fx-font-size: 14px;";


    /** Choeying
     * Gets the stage of the application
     *
     * @return the stage of the application
     */
    public static Stage getStage() {
        return stage; // cannot be a copy as operations will need to be performed on the stage for components from javafx, etc.
    }

    /** Jessica
     * gets the page of the application
     * @return the page of the application
     */
    public static String getPage() {
        return page;
    }

    /**Emmett
     * gets the user of the application
     * @return the user of the application
     */
    public static User getUser() {
        return user; // also cannot be a copy, same logic as above
    }

    /**Emmett
     * sets the user of the application
     * @param user the user of the application
     */
    public static void setUser(User user) {
        Application.user = user;
    }

    /** Joshua
     * gets the resource as a stream
     * @param resourceName the name of the resource
     * @return the resource as a stream
     */
    public static InputStream getResourceAsStream(String resourceName) {
        return Application.class.getResourceAsStream(resourceName);
    }

    /**Joshua
     * gets the resource as a URL
     * @param resourceName the name of the resource
     * @return the resource as a URL
     */
    public static URL getResource(String resourceName) {
        return Application.class.getResource(resourceName);
    }

    /**Joshua
     * Initializes the JavaFX application and sets up the main stage
     * This method is called when the application is launched
     * It sets the title of the stage, disables resizing, and loads the home page
     */
    @Override
    public void start(Stage stage) {
        // set up options
        this.stage = stage;
        stage.setTitle("TOMotives");
        stage.setResizable(false); // fixed window size

        loadPage("home.fxml");
    }

    /**Joshua
     * Loads a page from the specified FXML file and sets it as the scene for the application
     *
     * @param url The path to the FXML file to load, relative to the application's resources
     * @param pageName The name of the page loaded
     */
    public static void loadPage(String url, String pageName) {
        try {
            page = pageName;
            FXMLLoader loader = new FXMLLoader(Application.class.getResource(url));
            // DIMENSIONS: 1280 x 720
            scene = new Scene(loader.load(), 1280, 720);
            // global stylesheet applied to all pages
            scene.getStylesheets().add(getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Joshua
     * Loads the page with the specified URL and sets the page name to the file name without the extension
     * @param url The URL of the page to load
     */
    public static void loadPage(String url) {
        loadPage(url, url.substring(0, url.indexOf('.')));
    }

    /**Joshua
     * shows the popup to suggest users to login or sign up
     * @param title the title of the popup
     * @param subtitle the subtitle of the popup
     * @param pagePath the path to the page to load after logging in or signing up
     */
    public static void showLoginOrSignupPopup(String title, String subtitle, String pagePath) {
        Popup popup = new Popup();
        popup.setAutoHide(true);
        // setup popup structure
        VBox popupContent = new VBox();
        popupContent.getStyleClass().add("review-popup");
        popupContent.setSpacing(15);
        popupContent.setPadding(new Insets(20));
        popupContent.setMinWidth(400);
        popupContent.setMaxWidth(500);
        popupContent.setOpacity(0);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.getStyleClass().add("popup-title");

        Label messageLabel = new Label(subtitle);
        messageLabel.setStyle("-fx-font-size: 16px;");
        messageLabel.setWrapText(true);

        Button loginButton = new Button("Login");
        loginButton.setStyle(NORMAL_BUTTON_STYLE);
        // add hover listener to change between normal and hover styles
        loginButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loginButton.setStyle(HOVER_BUTTON_STYLE);
            } else {
                loginButton.setStyle(NORMAL_BUTTON_STYLE);
            }
        });
        loginButton.setOnAction(e -> {
            popup.hide();
            Application.loadPage("login.fxml", "login" + pagePath); // pass in pagePath as the location page to be loaded after login
        });

        Button signupButton = new Button("Sign Up");
        signupButton.setStyle(NORMAL_BUTTON_STYLE);
        signupButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                signupButton.setStyle(HOVER_BUTTON_STYLE);
            } else {
                signupButton.setStyle(NORMAL_BUTTON_STYLE);
            }
        });
        signupButton.setOnAction(e -> {
            popup.hide();
            Application.loadPage("sign-up.fxml", "sign-up" + pagePath); // pass in pagePath as the location page to be loaded after sign up
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle(NORMAL_BUTTON_STYLE);
        cancelButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                cancelButton.setStyle(HOVER_BUTTON_STYLE);
            } else {
                cancelButton.setStyle(NORMAL_BUTTON_STYLE);
            }
        });
        cancelButton.setOnAction(e -> popup.hide());

        HBox leftButtonBox = new HBox(10);
        leftButtonBox.setAlignment(Pos.CENTER_LEFT);
        leftButtonBox.getChildren().addAll(loginButton, signupButton);

        HBox rightButtonBox = new HBox();
        rightButtonBox.setAlignment(Pos.CENTER_RIGHT);
        rightButtonBox.getChildren().add(cancelButton);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(leftButtonBox, Priority.ALWAYS);
        buttonBox.getChildren().addAll(leftButtonBox, rightButtonBox);

        popupContent.getChildren().addAll(titleLabel, messageLabel, buttonBox);

        popupContent.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
        // add content to popup
        popup.getContent().add(popupContent);

        // show the popup
        Window window = scene.getWindow();
        popup.show(window, window.getX() + (window.getWidth() - popupContent.getMinWidth()) / 2, window.getY() + (window.getHeight() - 300) / 2);

        // animation for popup appearance
        Timeline fadeIn = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(popupContent.opacityProperty(), 0)
            ),
            new KeyFrame(Duration.millis(200),
                    new KeyValue(popupContent.opacityProperty(), 1)
            )
        );
        fadeIn.play();
    }

    /** Joshua
     * shows the popup to suggest users to login or sign up with the page path passed in to be loaded after logging in or signing up
     * @param pagePath the path to the page to load after logging in or signing up
     */
    public static void showLoginOrSignupPopup(String pagePath) {
        showLoginOrSignupPopup("Login or Sign Up", "Please login or sign up to continue", pagePath);
    }

    /** Joshua
     * shows the popup to suggest users to login or sign up, with no page path to be loaded after logging in or signing up
     */
    public static void showLoginOrSignupPopup() {
        showLoginOrSignupPopup("");
    }

    /** Joshua
     * starts the program
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
