package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.LocationControllerBase;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.models.Review;
import com.tomotives.tomotives.services.LocationService;
import com.tomotives.tomotives.services.ToastService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LocationDetailController extends LocationControllerBase {

    @FXML
    private ImageView locationImage;
    @FXML
    private ResizableImageController locationImageController;

    @FXML
    private Label locationName;

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

    @FXML
    private void initialize() {
        locationImageController.resize(locationImage.getFitWidth(), locationImage.getFitHeight());
        locationImageController.applyRoundedCorners(10);
        loadLocationData();
    }

    private void loadLocationData() {
        currentLocation = LocationService.getLocation(Application.getPage().substring(Application.getPage().indexOf("/") + 1));
        loadLocationData(currentLocation);
    }
    private void loadLocationData(Location location) {
        locationName.setText(location.getName());
        locationImageController.setImage(new Image(Application.getResourceAsStream("images/" + location.getImage())));
        locationImageController.resize(locationImage.getFitWidth(), locationImage.getFitHeight());
        starRatingLabel.setText(String.valueOf(location.getRating()));
        priceRatingLabel.setText(String.valueOf(location.getPrice()));
        descriptionText.setText(location.getDescription());

        for (int i = 0; i < filterHBox.getChildren().size(); i++) {
            Node child = filterHBox.getChildren().get(i);
            if (child instanceof Button) {
                ((Button) child).setText(location.getCategories().get(i).getName());
            }
        }

        updateStarRating(location.getRating(), starsContainer);
        updatePriceRating(location.getPrice(), priceContainer);

        loadReviews(location.getReviews());
    }



    private void loadReviews(List<Review> reviews) {
        // Clear existing reviews
        reviewsContainer.getChildren().clear();

        // Add mock reviews
        for (Review review : reviews) {
            VBox reviewBox = createReviewBox(review);
            reviewsContainer.getChildren().add(reviewBox);
        }
    }

    private VBox createReviewBox(Review review) {
        VBox reviewBox = new VBox();
        reviewBox.getStyleClass().add("review-item");
        reviewBox.setSpacing(5);

        // Review header (author and date)
        HBox headerBox = new HBox();
        headerBox.setSpacing(10);

        Label authorLabel = new Label(review.getUser());
        authorLabel.getStyleClass().add("review-author");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        Label dateLabel = new Label(dateFormat.format(review.getDate()));
        dateLabel.getStyleClass().add("review-date");

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

        HBox.setHgrow(authorLabel, Priority.ALWAYS);
        headerBox.getChildren().addAll(authorLabel, dateLabel, starRatingBox, priceRatingBox);

        Label contentLabel = new Label(review.getDescription());
        contentLabel.getStyleClass().add("review-content");
        contentLabel.setWrapText(true);

        reviewBox.getChildren().addAll(headerBox, contentLabel);
        return reviewBox;
    }

    @FXML
    private void handleAddReviewButtonClick(ActionEvent event) {
        Popup popup = new Popup();
        popup.setAutoHide(true);
        // setup popup structure
        VBox popupContent = new VBox();
        popupContent.getStyleClass().add("review-popup");
        popupContent.setSpacing(15);
        popupContent.setPadding(new Insets(20));
        popupContent.setMinWidth(400);
        popupContent.setMaxWidth(500);

        Label titleLabel = new Label("Add Review");
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
        final int[] selectedStarRating = {0}; // default is 0 stars
        final int[] selectedPriceRating = {0};
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
                selectedStarRating[0] = value;
                updateClickableStars(starLabels, value);
            });
            priceLabels[i].setOnMouseClicked(e -> {
                selectedPriceRating[0] = value;
                updateClickablePrices(priceLabels, value);
            });

            // set hover listeners so display can be updated depending on which item is hovered
            starLabels[i].setOnMouseEntered(e -> {
                updateClickableStars(starLabels, value);
            });
            priceLabels[i].setOnMouseEntered(e -> {
                updateClickablePrices(priceLabels, value);
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
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setOnAction(e -> popup.hide());

        Button submitButton = new Button("Submit Review");
        submitButton.getStyleClass().add("submit-button");
        submitButton.setOnAction(e -> {
            // validate input
            if (reviewTextArea.getText().isEmpty()) {
                ToastService.show(Application.getStage(), "Review details are required", ToastController.ToastType.ERROR);
                return;
            }
            addReview(reviewTextArea.getText(), selectedStarRating[0], selectedPriceRating[0], "Current User");
            popup.hide();
        });

        buttonBox.getChildren().addAll(cancelButton, submitButton);

        // add all components to the popup content
        popupContent.getChildren().addAll(
                titleLabel,
                starRatingBox,
                priceRatingBox,
                reviewTextArea,
                buttonBox
        );
        popupContent.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");

        // add content to popup
        popup.getContent().add(popupContent);

        // show the popup
        Window window = ((Node) event.getSource()).getScene().getWindow();
        popup.show(window,
                window.getX() + (window.getWidth() - popupContent.getMinWidth()) / 2,
                window.getY() + (window.getHeight() - 300) / 2);
    }

    private void addReview(String description, double rating, double priceRating, String user) {
        Review newReview = new Review(
                description,
                rating,
                priceRating,
                user,
                new Date() // current date
        );

        // add to the location's reviews
        currentLocation.getReviews().add(newReview);

        // update the UI
        VBox reviewBox = createReviewBox(newReview);
        reviewsContainer.getChildren().addFirst(reviewBox); // add to top

        // update the location's average ratings
        updateLocationRatings();
    }

    private void updateClickableStars(Label[] stars, int value) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].getStyleClass().removeAll("filled-star", "empty-star");
            if (i < value) {
                stars[i].getStyleClass().add("filled-star");
            } else {
                stars[i].getStyleClass().add("empty-star");
            }
        }
    }

    private void updateClickablePrices(Label[] prices, int value) {
        for (int i = 0; i < prices.length; i++) {
            prices[i].getStyleClass().removeAll("filled-price", "empty-price");
            if (i < value) {
                prices[i].getStyleClass().add("filled-price");
            } else {
                prices[i].getStyleClass().add("empty-price");
            }
        }
    }

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