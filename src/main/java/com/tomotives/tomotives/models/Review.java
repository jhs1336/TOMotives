package com.tomotives.tomotives.models;

import java.util.Date;

public class Review {
    private String description;
    private double rating;
    private double priceRating;
    private String user;
    private Date date;

    public Review(String description, double rating, double priceRating, String user, Date date) {
        this.description = description;
        this.rating = rating;
        this.priceRating = priceRating;
        this.user = user;
        this.date = date;
    }

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
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
