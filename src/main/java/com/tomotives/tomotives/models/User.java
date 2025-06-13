/* The User class represents a user who signed up for the application
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.models;

import java.util.ArrayList;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String displayName;
    private ArrayList<String> favourites;
    private ArrayList<String> recentLocations;
    private ArrayList<String> friends;
    private ArrayList<Category> likedCategories;

    /** Choeying
     * Constructs a new User object with the specified properties, arraylists are initialized to empty
     * @param email email address of the user
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param password password of the user
     * @param displayName display name of the user
     */
    public User(String email, String firstName, String lastName, String password, String displayName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.displayName = displayName;
        this.favourites = new ArrayList<>();
        this.recentLocations = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.likedCategories = new ArrayList<>();
    }

    /**Saul
     * Constructs a new User object with the specified properties
     * @param email email address of the user
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param password password of the user
     * @param displayName display name of the user
     * @param favourites favourites of the user
     * @param recentLocations recent locations of the user
     * @param friends friends of the user
     * @param likedCategories liked categories of the user
     */
    public User(String email, String firstName, String lastName, String password, String displayName, ArrayList<String> favourites, ArrayList<String> recentLocations, ArrayList<String> friends, ArrayList<Category> likedCategories) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.displayName = displayName;
        this.favourites = favourites;
        this.recentLocations = recentLocations;
        this.friends = friends;
        this.likedCategories = likedCategories;
    }

    /** Jessica
     * Constructs a new User object with the specified properties
     * @param other user to copy from
     */
    public User(User other) {
        this.email = other.email;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.password = other.password;
        this.displayName = other.displayName;
        this.favourites = new ArrayList<>(other.favourites);
        this.recentLocations = new ArrayList<>(other.recentLocations);
        this.friends = new ArrayList<>(other.friends);
        this.likedCategories = new ArrayList<>(other.likedCategories);
    }

    /** Emmett
     * get the email of the user
     * @return the email of the user
     */
    public String getEmail() { return email; }

    /** Emmett
     * set the email of the user
     * @param email the email of the user
     */
    public void setEmail(String email) { this.email = email; }

    /** Saul
     * get the first name of the user
     * @return the first name of the user
     */
    public String getFirstName() { return firstName; }

    /** Saul
     * set the first name of the user
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** Saul
     * get the last name of the user
     * @return the last name of the user
     */
    public String getLastName() { return lastName; }

    /** Saul
     * set the last name of the user
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** Jessica
     * get the password of the user
     * @return the password of the user
     */
    public String getPassword() { return password; }

    /** Jessica
     * set the password of the user
     * @param password the password of the user
     */
    public void setPassword(String password) { this.password = password; }

    /** Jessica
     * get the display name of the user
     * @return the display name of the user
     */
    public String getDisplayName() { return displayName; }

    /** Jessica
     * set the display name of the user
     * @param displayName the display name of the user
     */
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    /** Choeying
     * get the favourites of the user
     * @return the favourites of the user
     */
    public ArrayList<String> getFavourites() { return favourites; }

    /** Choeying
     * set the favourites of the user
     * @param favourites the favourites of the user
     */
    public void setFavourites(ArrayList<String> favourites) { this.favourites = favourites; }

    /** Choeying
     * get the recent locations of the user
     * @return the recent locations of the user
     */
    public ArrayList<String> getRecentLocations() { return recentLocations; }

    /** Choeying
     * set the recent locations of the user
     * @param recentLocations the recent locations of the user
     */
    public void setRecentLocations(ArrayList<String> recentLocations) { this.recentLocations = recentLocations; }

    /** Emmett
     * get the friends of the user
     * @return the friends of the user
     */
    public ArrayList<String> getFriends() { return friends; }

    /** Emmett
     * set the friends of the user
     * @param friends the friends of the user
     */
    public void setFriends(ArrayList<String> friends) { this.friends = friends; }

    /** Emmett
     * get the liked categories of the user
     * @return the liked categories of the user
     */
    public ArrayList<Category> getLikedCategories() { return likedCategories; }

    /** Emmett
     * set the liked categories of the user
     * @param likedCategories the liked categories of the user
     */
    public void setLikedCategories(ArrayList<Category> likedCategories) { this.likedCategories = likedCategories; }


}
