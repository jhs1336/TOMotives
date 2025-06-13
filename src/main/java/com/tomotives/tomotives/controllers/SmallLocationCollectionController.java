/* The SmallLocationCollectionController class is the controller for the small-location-collection.fxml component which is a collection of locations in the small location display format
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.services.LocationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class SmallLocationCollectionController {
    @FXML
    private GridPane gridPane;

    private int columns = 3;

    /** Saul
     * Returns the number of columns in the grid layout
     *
     * @return the number of columns in the grid layout
     */
    public int getColumns() {
        return columns;
    }

    /** Saul
     * Sets the number of columns in the grid layout
     *
     * @param columns the number of columns in the grid layout
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /** Saul
     * Clears all the children from the GridPane
     */
    private void clearGridPane() {
        gridPane.getChildren().clear();
    }


    /** Choeying
     * Load a list of locations to the grid pane
     * If the provided list of locations is empty, a it displays no results found
     *
     * @param locations the list of locations to display in the grid pane
     */
    public void loadLocations(List<Location> locations) {
        clearGridPane();
        if (locations.isEmpty()) {
            Label noResultsLabel = new Label("No results found.");
            gridPane.getChildren().add(noResultsLabel);
            return;
        }
        for (Location location : locations) {
            loadLocation(location);
        }
    }

    /**Joshua
     * Loads a list of locations to the grid pane, filtering by the provided list of categories
     * If the filtered list of locations is empty, no results is displayed
     *
     * @param locations the list of locations to display in the grid pane
     * @param categories the list of categories to filter the locations by
     */
    public void loadLocations(List<Location> locations, List<Category> categories) {
        clearGridPane();
        boolean found = false;
        for (Location location : locations) {
            // ensure at least one category matches
            if (categories.stream().anyMatch(category -> location.getCategories().contains(category))) {
                loadLocation(location);
                found = true;
            }
        }
        if (!found) {
            Label noResultsLabel = new Label("No results found.");
            gridPane.getChildren().add(noResultsLabel);
        }
    }

    /** Joshua
     * Loads a single location display component into the grid pane
     *
     * @param location the location to display
     */
    private void loadLocation(Location location) {
        try {
            // load the large location display component
            FXMLLoader loader = new FXMLLoader(Application.getResource("small-location-display.fxml"));
            Node locationDisplay = loader.load();
            SmallLocationController controller = loader.getController();
            controller.setLocationData(location);

            int row = gridPane.getChildren().size() / columns;
            int col = gridPane.getChildren().size() % columns;
            GridPane.setRowIndex(locationDisplay, row);
            GridPane.setColumnIndex(locationDisplay, col);
            gridPane.getChildren().add(locationDisplay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
