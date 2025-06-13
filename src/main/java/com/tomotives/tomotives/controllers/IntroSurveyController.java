/* The IntroSurveyControlller class is the controller for the intro-survey.fxml page which is the page after a user signs up, where they select their interests
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

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

    /**Joshua
     * Initialize the controller by setting up the filter buttons, and disabling the Done button (as it requires 3 selections first)
     */
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

    /** Emmett (with help from Joshua for FXML and CSS)
     * Handles the click from a filter button
     * If the button is already selected, it is deselected
     * If it is not selected, then it is selected
     * Then the done button state is updated
     *
     * @param event The ActionEvent from the button click
     */
    @FXML
    private void onFilterClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        // check if the button is already selected
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

    /**Saul
     * Handles the click from the done button
     * If the minimum number of filters are selected, it sets the user's liked categories in the UserService and in the current Application user
     * It also shows a success toast and loads the home page
     *
     * @param event The ActionEvent from the button click
     */
    @FXML
    private void onDoneClick(ActionEvent event) {
        // check if the minimum number of filters are selected
        if (selectedFilters.size() >= MIN_SELECTIONS) {
            UserService.setLikedCategories(Application.getUser().getDisplayName(), getSelectedFilters());
            Application.getUser().setLikedCategories((ArrayList<Category>) getSelectedFilters());
            ToastService.show(Application.getStage(), "Intro Survey Completed", ToastController.ToastType.SUCCESS, 4000);
            Application.loadPage("home.fxml");
        }
    }

    /** Jessica
     * Updates the state of the Done button based on the number of selected filters
     * If the number of selected filters is less than the minimum required which by default is 3, then the button is disabled
     * Otherwise the button is enabled
     */
    private void updateDoneButtonState() {
        // check if the minimum number of selections is met
        boolean hasMinimumSelections = selectedFilters.size() >= MIN_SELECTIONS;
        doneButton.setDisable(!hasMinimumSelections);
    }

    /**Joshua
     * Gets the list of selected filters from the buttons on screen
     *
     * @return The list of selected filters
     */
    public List<Category> getSelectedFilters() {
        List<Category> selectedTexts = new ArrayList<>();
        for (Button button : selectedFilters) {
            // replace spaces with underscores and convert to uppercase, then get the enum value
            selectedTexts.add(Category.valueOf(button.getText().replace(" ", "_").toUpperCase()));
        }
        return selectedTexts;
    }
}