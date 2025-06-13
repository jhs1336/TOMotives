/* The BrowseController class is the controller for the "browse.fxml" page and handles the logic for that page
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.controllers;

import com.tomotives.tomotives.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class BrowseController {

    /**Saul
     * Handles the click event on a filter button in the browse page
     * When a filter button is clicked, it loads the search result page with the selected filter
     *
     * @param event The ActionEvent representing the button click.
     */
    @FXML
    private void onFilterClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Application.loadPage("search-result-page.fxml", "search-result-page/filter/" + clickedButton.getText()); // FORMAT: search-result-page/filter or search/term
    }
}
