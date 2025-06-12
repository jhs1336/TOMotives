package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class BrowseController {

    @FXML
    private void onFilterClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Application.loadPage("search-result-page.fxml", "search-result-page/filter/" + clickedButton.getText());
    }
}
