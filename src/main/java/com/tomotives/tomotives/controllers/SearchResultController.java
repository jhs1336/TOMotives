package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.services.LocationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SearchResultController {
    @FXML
    private Label searchResultsLabel;

    @FXML
    private SmallLocationCollectionController resultsCollectionController;
    @FXML
    private GridPane filterGridPane;

    private String searchTerm;
    private ArrayList<Category> selectedFilters = new ArrayList<>();

    @FXML
    private void initialize() {
        String[] parts = Application.getPage().split("/");
        if (parts.length == 3) {
            String middlePart = parts[1];
            if (middlePart.equals("search")) {
                searchTerm = parts[2];
                resultsCollectionController.loadLocations(LocationService.filterLocationListByName(searchTerm));
                searchResultsLabel.setText("Search Results for \"" + searchTerm + "\"");
            } else if (middlePart.equals("filter")) {
                selectedFilters.add(Category.valueOf(parts[2].replace(" ", "_").toUpperCase()));
                resultsCollectionController.loadLocations(LocationService.filterLocationListByCategories(selectedFilters));
                updateButtons();
            }
        }

        for (Node node : filterGridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Tooltip tooltip = new Tooltip(button.getText());
                button.setTooltip(tooltip);
            }
        }
    }

    @FXML
    private void refresh(ActionEvent event) {
        if (searchTerm != null) resultsCollectionController.loadLocations(LocationService.filterLocationListByName(searchTerm), selectedFilters);
        else resultsCollectionController.loadLocations(LocationService.filterLocationListByCategories(selectedFilters));
    }
    @FXML
    private void onFilterClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (selectedFilters.contains(Category.valueOf(clickedButton.getText().replace(" ", "_").toUpperCase()))) {
            // deselect the filter
            selectedFilters.remove(Category.valueOf(clickedButton.getText().replace(" ", "_").toUpperCase()));
            clickedButton.getStyleClass().remove("selected");
        } else {
            // select the filter
            selectedFilters.add(Category.valueOf(clickedButton.getText().replace(" ", "_").toUpperCase()));
            clickedButton.getStyleClass().add("selected");
        }
    }
    private void updateButtons() {
        for (Node node : filterGridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                if (selectedFilters.contains(Category.valueOf(button.getText().replace(" ", "_").toUpperCase()))) {
                    button.getStyleClass().add("selected");
                } else {
                    button.getStyleClass().remove("selected");
                }
            }
        }
    }
}
