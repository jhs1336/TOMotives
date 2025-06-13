/* The Location class represents a location in the application
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

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


    /** Choeying
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


    /** Jessica
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
     * @return the name of the location
     */
    public String getName() {
        return name;
    }

    /** Saul
     * Sets the name of the location
     * @param name the new name of the location
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Saul
     * get the categories associated with the location
     * @return the categories associated with the location
     */
    public ArrayList<Category> getCategories() {
        return categories;
    }

    /** Saul
     * Sets the categories associated with the location
     * @param categories the new categories associated with the location
     */
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    /** Choeying
     * get the rating of the location
     * @return the rating of the location
     */
    public double getRating() {
        return rating;
    }

    /** Choeying
     * set the rating of the location
     * @param rating the new rating of the location
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /** Choeying
     * get the price of the location
     * @return the price of the location
     */
    public double getPrice() {
        return price;
    }

    /** Choeying
     * set the price of the location
     * @param price the new price of the location
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /** Jessica
     * get the description of the location
     * @return the description of the location
     */
    public String getDescription() {
        return description;
    }

    /** Jessica
     * set the description of the location
     * @param description the new description of the location
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Jessica
     * get the reviews of the location
     * @return the reviews of the location
     */
    public ArrayList<Review> getReviews() {
        return new ArrayList<Review>(reviews);
    }

    /** Jessica
     * set the reviews of the location
     * @param reviews the new reviews of the location
     */
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    /** Emmett
     * get the image of the location
     * @return the image of the location
     */
    public String getImage() {
        return image;
    }

    /** Emmett
     * set the image of the location
     * @param image the new image of the location
     */
    public void setImage(String image) {
        this.image = image;
    }
}
