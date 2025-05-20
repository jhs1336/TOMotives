package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.LocationControllerBase;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.models.Review;
import com.tomotives.tomotives.services.LocationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
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
    public HBox filterHBox;

    @FXML
    public void initialize() {
        locationImageController.resize(locationImage.getFitWidth(), locationImage.getFitHeight());
        locationImageController.applyRoundedCorners(10);
        loadLocationData();
    }

    private void loadLocationData() {
        System.out.println(Application.getPage().substring(Application.getPage().indexOf("/") + 1));
        Location location = LocationService.getLocation(Application.getPage().substring(Application.getPage().indexOf("/") + 1));
        loadLocationData(location);
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
        System.out.println(review.getDate().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        Label dateLabel = new Label(dateFormat.format(review.getDate()));
        dateLabel.getStyleClass().add("review-date");

        Label starRatingLabel = new Label("★ " + review.getRating());
        starRatingLabel.getStyleClass().add("review-rating");

        Label priceRatingLabel = new Label("$ " + review.getPriceRating());
        priceRatingLabel.getStyleClass().add("review-price");

        HBox.setHgrow(dateLabel, Priority.ALWAYS);
        headerBox.getChildren().addAll(authorLabel, starRatingLabel, priceRatingLabel, dateLabel);

        Label contentLabel = new Label(review.getDescription());
        contentLabel.getStyleClass().add("review-content");
        contentLabel.setWrapText(true);

        reviewBox.getChildren().addAll(headerBox, contentLabel);
        return reviewBox;
    }
}