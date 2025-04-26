package com.tomotives.tomotives;

public class Review {
    private String description;
    private double rating;
    private double priceRating;
    private String user;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public double getPriceRating() {
        return priceRating;
    }
    public void setPriceRating(double priceRating) {
        this.priceRating = priceRating;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
}
