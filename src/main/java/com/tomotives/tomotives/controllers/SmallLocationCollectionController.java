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

    private void clearGridPane() {
        gridPane.getChildren().clear();
    }

    public int getColumns() {
        return columns;
    }
    public void setColumns(int columns) {
        this.columns = columns;
    }
}
