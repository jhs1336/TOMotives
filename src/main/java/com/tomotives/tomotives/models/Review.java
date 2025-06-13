/* The Review class represents a review left by a user on a location in the application
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.models;

import java.util.Date;

public class Review {
    private String description;
    private double rating;
    private double priceRating;
    private String user;
    private Date date;

    /** Emmett
     *
     * @param description description of the review
     * @param rating rating of the review
     * @param priceRating price rating of the review
     * @param user user who wrote the review
     * @param date date of the review
     */
    public Review(String description, double rating, double priceRating, String user, Date date) {
        this.description = description;
        this.rating = rating;
        this.priceRating = priceRating;
        this.user = user;
        this.date = date;
    }

    /** Emmett
     * get the description of the review
     * @return description of the review
     */
    public String getDescription() {
        return description;
    }

    /** Emmett
     * set the description of the review
     * @param description description of the review
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Saul
     * get the rating of the review
     * @return rating of the review
     */
    public double getRating() {
        return rating;
    }

    /** Saul
     * set the rating of the review
     * @param rating rating of the review
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /** Saul
     * get the price rating of the review
     * @return price rating of the review
     */
    public double getPriceRating() {
        return priceRating;
    }

    /** Saul
     * set the price rating of the review
     * @param priceRating price rating of the review
     */
    public void setPriceRating(double priceRating) {
        this.priceRating = priceRating;
    }

    /** Choeying
     * get the user who wrote the review
     * @return user who wrote the review
     */
    public String getUser() {
        return user;
    }

    /** Choeying
     * set the user who wrote the review
     * @param user user who wrote the review
     */
    public void setUser(String user) {
        this.user = user;
    }

    /** Jessica
     * get the date of the review
     * @return date of the review
     */
    public Date getDate() {
        return date;
    }

    /** Jessica
     * set the date of the review
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
