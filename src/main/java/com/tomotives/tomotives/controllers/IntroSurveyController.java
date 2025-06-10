package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.services.ToastService;
import com.tomotives.tomotives.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

public class IntroSurveyController {

    @FXML
    private Button doneButton;

    @FXML
    private GridPane filterGridPane;


    private List<Button> filterButtons;
    private List<Button> selectedFilters;
    private static final int MIN_SELECTIONS = 3;

    @FXML
    public void initialize() {
        filterButtons = new ArrayList<>();
        selectedFilters = new ArrayList<>();

        for (Node child : filterGridPane.getChildren()) {
            if (child instanceof Button) {
                filterButtons.add((Button) child);
            }
        }
        // initially disable the Done button
        updateDoneButtonState();
    }

    @FXML
    private void onFilterClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (selectedFilters.contains(clickedButton)) {
            // deselect the filter
            selectedFilters.remove(clickedButton);
            clickedButton.getStyleClass().remove("selected");
        } else {
            // select the filter
            selectedFilters.add(clickedButton);
            clickedButton.getStyleClass().add("selected");
        }

        updateDoneButtonState();
    }

    @FXML
    private void onDoneClick(ActionEvent event) {
        if (selectedFilters.size() >= MIN_SELECTIONS) {
            UserService.setLikedCategories(Application.getUser().getDisplayName(), getSelectedFilters());
            ToastService.show(Application.getStage(), "Intro Survey Completed", ToastController.ToastType.SUCCESS, 4000);
        }
    }

    private void updateDoneButtonState() {
        boolean hasMinimumSelections = selectedFilters.size() >= MIN_SELECTIONS;
        doneButton.setDisable(!hasMinimumSelections);
    }

    public List<Category> getSelectedFilters() {
        List<Category> selectedTexts = new ArrayList<>();
        for (Button button : selectedFilters) {
            selectedTexts.add(Category.valueOf(button.getText().replace(" ", "_").toUpperCase()));
        }
        return selectedTexts;
    }
}