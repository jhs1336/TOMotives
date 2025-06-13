/* The Category enum represents the categories of locations in the application
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.models;

public enum Category {
    INDOORS,
    OUTDOORS,
    PARK,
    NATURE,
    BUDGET,
    CLOTHING,
    VINTAGE,
    WITH_FRIENDS,
    SOLO,
    DATE_SPOT,
    NIGHT_TIME,
    LANDMARK,
    BIRDWATCHING,
    FAMILY,
    RESTAURANT,
    CAFE,
    SWIMMING,
    BEACH,
    DOG_FRIENDLY,
    BREAKFAST,
    LUNCH,
    DINNER,
    STUDY_SPOT,
    WALKING,
    TOURIST,
    BARS,
    SLEDDING,
    MALL,
    SHOP,
    SHOPPING,
    HIKING,
    BIKING,
    SCENIC,
    HISTORIC,
    MUSEUM,
    PARKING,
    MUSIC,
    GYM,
    CAR_RENTAL,
    CAR_WASH,
    CAR_REPAIR,
    EDUCATIONAL;


    /** Joshua
     * returns the string representation of the category, with underscores replaced by spaces and the first letter capitalized
     * ex. INDOORS -> Indoors, STUDY_SPOT -> Study Spot
     * @return the string representation of the category
     */
    @Override
    public String toString() {
        return super.toString().charAt(0) + super.toString().replace("_", " ").toLowerCase().substring(1);
    }
}