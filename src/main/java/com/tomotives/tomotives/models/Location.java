package com.tomotives.tomotives.models;

import java.util.ArrayList;

public class Location {
    private String name;
    private ArrayList<Category> categories;
    private double rating;
    private double price;
    private String description;
    private ArrayList<Review> reviews;
    private String image;


    /** Joshua
     * Constructs a new Location object with the specified properties.
     *
     * @param name        the name of the location
     * @param description the description of the location
     * @param rating      the rating of the location
     * @param price       the price of the location
     * @param categories  the categories associated with the location
     * @param reviews     the reviews for the location
     * @param image       the image associated with the location
     */
    public Location(String name, String description, double rating, double price, ArrayList<Category> categories, ArrayList<Review> reviews, String image) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.categories = categories;
        this.reviews = reviews;
        this.image = image;
    }
    /**
     * Saul
     * Constructs a new Location object with rating and price calculated from reviews
     *
     * @param name        the name of the location
     * @param description the description of the location
     * @param categories  the categories associated with the location
     * @param reviews     the reviews for the location
     * @param image       the image associated with the location
     */
    public Location(String name, String description, ArrayList<Category> categories, ArrayList<Review> reviews, String image) {
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.reviews = reviews;
        this.image = image;

        if (reviews != null && !reviews.isEmpty()) {
            double totalRating = 0;
            double totalPrice = 0;
            for (Review review : reviews) {
                totalRating += review.getRating();
                totalPrice += review.getPriceRating();
            }
            this.rating = totalRating / reviews.size();
            this.price = totalPrice / reviews.size();
        } else {
            this.rating = 0;
            this.price = 0;
        }
    }


    /** Joshua
     * Constructs a new Location object by copying the properties of the provided Location object
     *
     * @param location the Location object to copy
     */
        public Location(Location location) {
        this.name = location.name;
        this.categories = new ArrayList<>(location.categories);
        this.rating = location.rating;
        this.price = location.price;
        this.description = location.description;
        this.reviews = new ArrayList<>(location.reviews);
        this.image = location.image;
    }

    /** Saul
     * The name of the location
     *
     * @return the name of the location
     */
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
