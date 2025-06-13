/* The LocationDetailController class is the controller for the location-detail-display.fxml page which displays all the details of a location, such as the name, description, image, ratings, and reviews. Extends LocationControllerBase to inherit everything needed for managing Locations
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.LocationControllerBase;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.models.Review;
import com.tomotives.tomotives.services.LocationService;
import com.tomotives.tomotives.services.ToastService;
import com.tomotives.tomotives.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.tomotives.tomotives.Application.HOVER_BUTTON_STYLE;
import static com.tomotives.tomotives.Application.NORMAL_BUTTON_STYLE;

public class LocationDetailController extends LocationControllerBase {
    // color strings for the heart (if the location is favorited or not)
    private final String FAVOURITED_COLOR_STYLE = "-fx-text-fill: #ED4956;";
    private final String NOT_FAVOURITED_COLOR_STYLE = "-fx-text-fill: #CCCCCC;";

    @FXML
    private ImageView locationImage;
    @FXML
    private ResizableImageController locationImageController;

    @FXML
    private Label locationName;
    @FXML
    private Label heartLabel;

    @FXML
    private HBox priceContainer;
    @FXML
    private Label priceRatingLabel;
    @FXML
    private HBox starsContainer;
    @FXML
    private Label starRatingLabel;

    @FXML
    private Label descriptionText;

    @FXML
    private VBox reviewsContainer;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox filterHBox;

    private Location currentLocation;

    /**Joshua
     * Initializes the LocationDetailController
     * - Resizes the location image to the needed size and applies rounded corners
     * - Loads the location data to all UI components
     * - Sets the heart label style based on whether the current location is in the user's favorites
     * - Adds click event handlers to the heart label to allow the user to add/remove the location from their favorites.
     * - Adds the current location to the user's recent locations if the user is logged in.
     */
    @FXML
    private void initialize() {
        locationImageController.resize(locationImage.getFitWidth(), locationImage.getFitHeight());
        locationImageController.applyRoundedCorners(10);
        loadLocationData();

        // if the user is logged in, check if they have favorited the current location, and change the heart color accordingly
        if (Application.getUser() == null || !Application.getUser().getFavourites().contains(currentLocation.getName())) heartLabel.setStyle(NOT_FAVOURITED_COLOR_STYLE);
        else heartLabel.setStyle(FAVOURITED_COLOR_STYLE);

        heartLabel.setOnMouseClicked(event -> {
            // if user is not logged in, show login or signup popup
            if (Application.getUser() == null) Application.showLoginOrSignupPopup("/location/" + locationName.getText());
            else {
                // if user already has favorited the location, remove it from their favorites and change the heart color
                if (Application.getUser().getFavourites().contains(currentLocation.getName())) {
                    Application.getUser().getFavourites().remove(currentLocation.getName());
                    heartLabel.setStyle(NOT_FAVOURITED_COLOR_STYLE);
                } else {
                    // if user has not favorited the location, add it to their favorites and change the heart color
                    Application.getUser().getFavourites().add(currentLocation.getName());
                    heartLabel.setStyle(FAVOURITED_COLOR_STYLE);
                }
                UserService.addOrRemoveUserFavourite(Application.getUser().getDisplayName(), currentLocation.getName());
            }

        });
        heartLabel.setOnMouseExited(event -> {
            if (Application.getUser() == null || !Application.getUser().getFavourites().contains(currentLocation.getName())) heartLabel.setStyle(NOT_FAVOURITED_COLOR_STYLE);
            else heartLabel.setStyle(FAVOURITED_COLOR_STYLE);
        });
        heartLabel.setOnMouseMoved(event -> {
            if (Application.getUser() == null || !Application.getUser().getFavourites().contains(currentLocation.getName())) heartLabel.setStyle(FAVOURITED_COLOR_STYLE);
            else heartLabel.setStyle(NOT_FAVOURITED_COLOR_STYLE);
        });

        // if user is logged in, add the current location to their recent locations
        if (Application.getUser() != null) {
            UserService.addRecentLocationToUser(Application.getUser().getDisplayName(), currentLocation.getName());
        }
    }

    /** Jessica
     * Loads the location data for the current location displayed on the page.
     * Gets the current location from the page URL (e.g. "location-detail-display/High Park")
     */
    private void loadLocationData() {
        // get the location from the page url (ex location-detail-display/High Park)
        currentLocation = LocationService.getLocation(Application.getPage().substring(Application.getPage().indexOf("/") + 1));
        loadLocationData(currentLocation);
    }


    /** Saul
     * Loads the location data for the specified location and updates the UI
     *
     * @param location The location object to load data for.
     */
    private void loadLocationData(Location location) {
        locationName.setText(location.getName());
        locationImageController.setImage(new Image(Application.getResourceAsStream("images/" + location.getImage())));
        locationImageController.resize(locationImage.getFitWidth(), locationImage.getFitHeight());
        starRatingLabel.setText(String.format("%.1f", location.getRating()));
        priceRatingLabel.setText(String.format("%.1f", location.getPrice()));
        descriptionText.setText(location.getDescription());

        // for each filter button, set the text to the category name
        for (int i = 0; i < filterHBox.getChildren().size(); i++) {
            Node child = filterHBox.getChildren().get(i);
            if (child instanceof Button) {
                ((Button) child).setText(String.valueOf(location.getCategories().get(i)));
            }
        }

        // update the ratings and load reviews
        updateStarRating(location.getRating(), starsContainer);
        updatePriceRating(location.getPrice(), priceContainer);
        loadReviews(location.getReviews());
    }

    /**Joshua
     * Loads and displays the reviews for the current location
     * Reviews are sorted by date (newest first)
     *
     * @param reviews The list of reviews to display.
     */
    private void loadReviews(List<Review> reviews) {
        // clear existing reviews
        reviewsContainer.getChildren().clear();

        // sort reviews by date (newest first so they will be at top of review list)
        reviews.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));

        for (Review review : reviews) {
            VBox reviewBox = createReviewBox(review);
            reviewsContainer.getChildren().add(reviewBox);
        }
    }

    /**Joshua
     * Creates a review box component for displaying a review
     * Includes reviewer name, date, ratings, option to edit depending on if the current user created the review, and review text
     *
     * @param review The review to display
     * @return A VBox containing the created component
     */
    private VBox createReviewBox(Review review) {
        VBox reviewBox = new VBox();
        reviewBox.getStyleClass().add("review-item");
        reviewBox.setSpacing(5);

        // review header
        HBox headerBox = new HBox();
        headerBox.setSpacing(10);

        Hyperlink authorLabel = new Hyperlink(review.getUser());
        authorLabel.getStyleClass().add("review-author");

        authorLabel.setOnAction(event -> {
            ContextMenu contextMenu = new ContextMenu();

            MenuItem addFriendMenuItem = new MenuItem("Add Friend");
            addFriendMenuItem.setDisable(Application.getUser() == null); // disabled if no user logged in
            MenuItem recentlyViewedMenuItem = new MenuItem("View Recent");
            MenuItem favouritesMenuItem = new MenuItem("View Favourites");

            // add button style class
            addFriendMenuItem.getStyleClass().add(Application.getUser() == null ? null : "button");
            recentlyViewedMenuItem.getStyleClass().add("button");
            favouritesMenuItem.getStyleClass().add("button");
            // apply styles
            addFriendMenuItem.setStyle("-fx-font-size: 14px;");
            recentlyViewedMenuItem.setStyle("-fx-font-size: 14px;");
            favouritesMenuItem.setStyle("-fx-font-size: 14px;");
            // make context menu look ok
            contextMenu.setStyle("-fx-padding: 5px; -fx-border-color: #00a0b0; -fx-background-radius: 5px; -fx-border-radius: 5px;");

            addFriendMenuItem.setOnAction(e -> {
                // add friend in user service and the application user
                UserService.addFriend(Application.getUser().getDisplayName(), authorLabel.getText());
                Application.getUser().getFriends().add(authorLabel.getText());
                ToastService.show(Application.getStage(), "Friend request sent!", ToastController.ToastType.SUCCESS);
            });
            // load pages respectively when clicked
            recentlyViewedMenuItem.setOnAction(e -> Application.loadPage("favourites-and-recently-viewed.fxml", "favourites-and-recently-viewed/recently-viewed/" + authorLabel.getText()));
            favouritesMenuItem.setOnAction(e -> Application.loadPage("favourites-and-recently-viewed.fxml", "favourites-and-recently-viewed/favourites/" + authorLabel.getText()));

            // add everything to menu
            contextMenu.getItems().addAll(addFriendMenuItem, recentlyViewedMenuItem, favouritesMenuItem);
            contextMenu.show(authorLabel, Side.BOTTOM, 0, 0);
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        Label dateLabel = new Label(dateFormat.format(review.getDate()));
        dateLabel.getStyleClass().add("review-date");

        // stars
        HBox starRatingBox = new HBox();
        starRatingBox.setSpacing(2);
        for (int i = 1; i <= 5; i++) {
            Label star = new Label("★");
            if (i <= review.getRating()) {
                star.getStyleClass().add("filled-star");
            } else if (i - 0.5 == review.getRating()) {
                star.getStyleClass().add("half-star");
            } else {
                star.getStyleClass().add("empty-star");
            }
            starRatingBox.getChildren().add(star);
        }

        // price rating
        HBox priceRatingBox = new HBox();
        priceRatingBox.setSpacing(2);
        for (int i = 1; i <= 5; i++) {
            Label price = new Label("$");
            if (i <= review.getPriceRating()) {
                price.getStyleClass().add("filled-price");
            } else if (i - 0.5 == review.getPriceRating()) {
                price.getStyleClass().add("half-price");
            } else {
                price.getStyleClass().add("empty-price");
            }
            priceRatingBox.getChildren().add(price);
        }

        // add edit icon if review belongs to current user
        if (Application.getUser() != null && review.getUser().equals(Application.getUser().getDisplayName())) {
            Label editIcon = new Label("✎");
            editIcon.setStyle("-fx-cursor: hand;");
            editIcon.setOnMouseClicked(e -> {
                handleAddReviewButtonClick(e, review.getDescription(), review.getRating(), review.getPriceRating(), true);
            });
            headerBox.getChildren().addAll(authorLabel, dateLabel, starRatingBox, priceRatingBox, editIcon);
        } else {
            HBox.setHgrow(authorLabel, Priority.ALWAYS);
            headerBox.getChildren().addAll(authorLabel, dateLabel, starRatingBox, priceRatingBox);
        }

        Label contentLabel = new Label(review.getDescription());
        contentLabel.getStyleClass().add("review-content");
        contentLabel.setWrapText(true);

        reviewBox.getChildren().addAll(headerBox, contentLabel);
        return reviewBox;
    }

    /**
     * Handles the click event for the add review, displaying a popup for the user to enter a new review
     *
     * @param event The mouse event triggered from the mouse click
     */
    @FXML
    private void handleAddReviewButtonClick(MouseEvent event) {
        handleAddReviewButtonClick(event, "", 0, 0, false);
    }

    /** Joshua
     * Handles the click event for the add review, displaying a popup for the user to enter a new review or edit an existing review
     *
     * @param event The mouse event triggered from the mouse click
     * @param description The initial description of the review, if editing an existing review
     * @param rating The initial rating of the review, if editing an existing review
     * @param priceRating The initial price rating of the review, if editing an existing review
     * @param removeExistingReview Whether the user is editing an existing review (true) or adding a new review (false)
     */
    @FXML
    private void handleAddReviewButtonClick(MouseEvent event, String description, double rating, double priceRating, boolean removeExistingReview) {
        // if no user is logged in
        if (Application.getUser() == null) {
            Application.showLoginOrSignupPopup("Login to Add Reviews", "You need to be logged in to add reviews", "/location/" + locationName.getText());
            return;
        }

        Popup popup = new Popup();
        popup.setAutoHide(true);
        // setup popup structure
        VBox popupContent = new VBox();
        popupContent.getStyleClass().add("review-popup");
        popupContent.setSpacing(15);
        popupContent.setPadding(new Insets(20));
        popupContent.setMinWidth(400);
        popupContent.setMaxWidth(500);

        // if removing an existing review, it means we are editing an existing review
        Label titleLabel = new Label(removeExistingReview ? "Edit Review" : "Add Review");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.getStyleClass().add("popup-title");

        // boxes for stars
        VBox starRatingBox = new VBox();
        starRatingBox.setSpacing(5);
        HBox starsBox = new HBox();
        starsBox.setSpacing(5);
        starsBox.setAlignment(Pos.CENTER_LEFT);

        // price rating boxes
        VBox priceRatingBox = new VBox();
        priceRatingBox.setSpacing(5);
        HBox pricesBox = new HBox();
        pricesBox.setSpacing(10);
        pricesBox.setAlignment(Pos.CENTER_LEFT);

        // final one element array to be used in lambda expressions - represents the value of stars selected
        final double[] selectedStarRating = {0}; // default is 0 stars
        final double[] selectedPriceRating = {0};
        Label[] starLabels = new Label[5];
        Label[] priceLabels = new Label[5];

        for (int i = 0; i < starLabels.length; i++) {
            final int value = i + 1;
            // add styles
            starLabels[i] = new Label("★");
            starLabels[i].getStyleClass().addAll("clickable-star", "empty-star");
            starLabels[i].setStyle("-fx-font-size: 24px; -fx-cursor: hand;");
            priceLabels[i] = new Label("$");
            priceLabels[i].getStyleClass().addAll("clickable-price", "empty-price");
            priceLabels[i].setStyle("-fx-font-size: 24px; -fx-cursor: hand;");

            // set click listeners to set selected value and update displays
            starLabels[i].setOnMouseClicked(e -> {
                // check if click is on left half of star
                if (e.getX() < starLabels[value-1].getWidth() / 2) {
                    selectedStarRating[0] = value - 0.5;
                } else {
                    selectedStarRating[0] = value;
                }
                updateClickableStars(starLabels, selectedStarRating[0]);
            });
            priceLabels[i].setOnMouseClicked(e -> {
                // check if click is on left half of price label
                if (e.getX() < priceLabels[value-1].getWidth() / 2) {
                    selectedPriceRating[0] = value - 0.5;
                } else {
                    selectedPriceRating[0] = value;
                }
                updateClickablePrices(priceLabels, selectedPriceRating[0]);
            });

            // set hover listeners so display can be updated depending on which item is hovered
            starLabels[i].setOnMouseMoved(e -> {
                double hoverValue = value;
                // check if hover is on left half of star
                if (e.getX() < starLabels[value-1].getWidth() / 2) {
                    hoverValue = value - 0.5;
                }
                updateClickableStars(starLabels, hoverValue);
            });
            priceLabels[i].setOnMouseMoved(e -> {
                double hoverValue = value;
                // check if hover is on left half of price label
                if (e.getX() < priceLabels[value-1].getWidth() / 2) {
                    hoverValue = value - 0.5;
                }
                updateClickablePrices(priceLabels, hoverValue);
            });
            starsBox.getChildren().add(starLabels[i]);
            pricesBox.getChildren().add(priceLabels[i]);
        }

        // Set action listeners for when mouse exits the container, clickable display resets to selected value
        starsBox.setOnMouseExited(e -> {
            updateClickableStars(starLabels, selectedStarRating[0]);
        });
        starRatingBox.getChildren().add(starsBox);
        pricesBox.setOnMouseExited(e -> {
            // when mouse exits the container, restore to selected value
            updateClickablePrices(priceLabels, selectedPriceRating[0]);
        });
        priceRatingBox.getChildren().add(pricesBox);


        // review text
        TextArea reviewTextArea = new TextArea();
        reviewTextArea.setWrapText(true);
        reviewTextArea.setPrefRowCount(5);
        reviewTextArea.setPromptText("Write your review here...");

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

        Button submitButton = new Button("Submit Review");
        submitButton.styleProperty().set(NORMAL_BUTTON_STYLE);
        submitButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                submitButton.setStyle(HOVER_BUTTON_STYLE);
            } else {
                submitButton.setStyle(NORMAL_BUTTON_STYLE);
            }
        });
        submitButton.setOnAction(e -> {
            // validate input
            if (reviewTextArea.getText().isEmpty()) {
                ToastService.show(Application.getStage(), "Review details are required", ToastController.ToastType.ERROR);
                return;
            }
            // if there is an existing review marked for removal, remove it
            if (removeExistingReview) LocationService.removeReview(locationName.getText(), description);
            addReview(reviewTextArea.getText(), selectedStarRating[0], selectedPriceRating[0], Application.getUser().getDisplayName());
            // refresh page to reload reviews, data, order of reviews, etc.
            Application.loadPage("location-detail-display.fxml", "location-detail-display/" + locationName.getText());
            popup.hide();
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);

        // for popup content structure
        VBox buttonVBox = new VBox();
        buttonVBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonVBox.getChildren().add(buttonBox);

        VBox ratingVBox = new VBox();
        ratingVBox.getChildren().addAll(starRatingBox, priceRatingBox);

        HBox bottomBox = new HBox();
        HBox.setHgrow(buttonVBox, Priority.ALWAYS);
        bottomBox.getChildren().addAll(ratingVBox, buttonVBox);

        // set the review options to the arguments passed in
        reviewTextArea.setText(description);
        selectedStarRating[0] = rating;
        updateClickableStars(starLabels, selectedStarRating[0]);
        selectedPriceRating[0] = priceRating;
        updateClickablePrices(priceLabels, selectedPriceRating[0]);

        // add all components to the popup content
        popupContent.getChildren().addAll(titleLabel, reviewTextArea, bottomBox);

        popupContent.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
        // add content to popup
        popup.getContent().add(popupContent);

        // show the popup
        Window window = ((Node) event.getSource()).getScene().getWindow();
        popup.show(window, window.getX() + (window.getWidth() - popupContent.getMinWidth()) / 2, window.getY() + (window.getHeight() - 300) / 2);
    }

    /**Joshua
     * Adds a new review to the current location and updates the UI and average ratings accordingly
     *
     * @param description The text description of the review
     * @param rating The star rating for the review (between 1.0 and 5.0)
     * @param priceRating The price rating for the review (between 1.0 and 5.0)
     * @param user The name of the user who submitted the review
     */
    private void addReview(String description, double rating, double priceRating, String user) {
        Review newReview = new Review(
                description,
                rating,
                priceRating,
                user,
                new Date() // current date
        );

        LocationService.addReview(currentLocation.getName(), newReview);

        // add to the location's reviews
        currentLocation.getReviews().add(newReview);

        // update the UI
        VBox reviewBox = createReviewBox(newReview);
        reviewsContainer.getChildren().addFirst(reviewBox); // add to top

        // update the location's average ratings
        updateLocationRatings();

        ToastService.show(Application.getStage(), "Review submitted successfully", ToastController.ToastType.SUCCESS);
    }

    /**Joshua
     * Updates the amount of stars filled in based on the star the user is hovering
     *
     * @param stars An array of labels representing the star ratings
     * @param value The current star rating value to be displayed
     */
    private void updateClickableStars(Label[] stars, double value) {
        for (int i = 0; i < stars.length; i++) {
            // remove all past styles
            stars[i].getStyleClass().removeAll("filled-star", "half-star", "empty-star");
            if (i+1 <= value) {
                stars[i].getStyleClass().add("filled-star");
            } else {
                if (value + 0.5 >= (i+1)) {
                    stars[i].getStyleClass().add("half-star");
                } else {
                    stars[i].getStyleClass().add("empty-star");
                }
            }
        }
    }
    /**
     * Updates the amount of price labels filled in based on the price item the user is hovering
     *
     * @param prices An array of labels representing the price ratings
     * @param value The current price rating value to be displayed
     */
    private void updateClickablePrices(Label[] prices, double value) {
        for (int i = 0; i < prices.length; i++) {
            // remove all past styles
            prices[i].getStyleClass().removeAll("filled-price", "half-price", "empty-price");
            if (i+1 <= value) {
                prices[i].getStyleClass().add("filled-price");
            } else {
                if (value + 0.5 >= (i+1)) {
                    prices[i].getStyleClass().add("half-price");
                } else {
                    prices[i].getStyleClass().add("empty-price");
                }
            }
        }
    }

    /**Jessica
     * Updates the UI with the average ratings for the location based off the reviews
     */
    private void updateLocationRatings() {
        // calculate new average ratings
        double totalStarRating = 0;
        double totalPriceRating = 0;
        List<Review> reviews = currentLocation.getReviews();

        for (Review review : reviews) {
            totalStarRating += review.getRating();
            totalPriceRating += review.getPriceRating();
        }

        double avgStarRating = totalStarRating / reviews.size();
        double avgPriceRating = totalPriceRating / reviews.size();

        // update the location object
        currentLocation.setRating(avgStarRating);
        currentLocation.setPrice(avgPriceRating);

        // update the UI
        starRatingLabel.setText(String.format("%.1f", avgStarRating));
        priceRatingLabel.setText(String.format("%.1f", avgPriceRating));

        // update the star and price rating displays
        updateStarRating(avgStarRating, starsContainer);
        updatePriceRating(avgPriceRating, priceContainer);
    }
}