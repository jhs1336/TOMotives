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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public ArrayList<String> getFavourites() { return favourites; }
    public void setFavourites(ArrayList<String> favourites) { this.favourites = favourites; }

    public ArrayList<String> getRecentLocations() { return recentLocations; }
    public void setRecentLocations(ArrayList<String> recentLocations) { this.recentLocations = recentLocations; }

    public ArrayList<String> getFriends() { return friends; }
    public void setFriends(ArrayList<String> friends) { this.friends = friends; }


}
