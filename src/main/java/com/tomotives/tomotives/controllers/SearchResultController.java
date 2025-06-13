/* The SearchResultController class is the controller for the search-result-page.fxml page which is the page for searching and filtering by categories for locations
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

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

    /**Joshua
     * Initializes the SearchResultController by handling the page navigation and loading the appropriate location data based on the search term or selected filters
     * Determines whether the URL is indicating a search or a filter, then loads the appropriate data
     * Also loads the grid of categories to filter by
     */
    @FXML
    private void initialize() {
        // FORMAT: search-result-page/search or filter/term
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

    /**Choeying
     * Refreshes the search results by loading the appropriate location data based on the search term or selected filters
     * If there is a search term, it loads the locations that match the search term and categories filtered by. If not, it just filters by categories
     *
     * @param event The ActionEvent that triggered the refresh
     */
    @FXML
    private void refresh(ActionEvent event) {
        if (searchTerm != null) resultsCollectionController.loadLocations(LocationService.filterLocationListByName(searchTerm), selectedFilters);
        else resultsCollectionController.loadLocations(LocationService.filterLocationListByCategories(selectedFilters));
    }

    /**Joshua
     * Handles the click event on a filter button in the search results page
     * When a filter button is clicked, it either selects or deselects the corresponding category filter
     * The selected filters are used to update the search results displayed on the page
     *
     * @param event The ActionEvent triggered by clicking the filter button.
     */
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
    /** Saul
     * Updates each of the buttons to be selected or unselected based on the selected filters
     */
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
