package com.tomotives.tomotives;

import java.util.ArrayList;

public class Location {
    private String name;
    private ArrayList<Category> categories;
    private double rating;
    private double price;
    private String description;
    private ArrayList<Review> reviews;
    private String image;

    public Location(String name, String description, double rating, double price, ArrayList<Category> categories, ArrayList<Review> reviews, String image) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.categories = categories;
        this.reviews = reviews;
        this.image = image;
    }
    public Location(Location location) {
        this.name = location.name;
        this.categories = new ArrayList<>(location.categories);
        this.rating = location.rating;
        this.price = location.price;
        this.description = location.description;
        this.reviews = new ArrayList<>(location.reviews);
        this.image = location.image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
