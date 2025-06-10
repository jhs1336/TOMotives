package com.tomotives.tomotives.models;

/** Joshua + Saul
 * Represents the different categories of activities or places that can be associated with a location
 */
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


    @Override
    public String toString() {
        return super.toString().charAt(0) + super.toString().replace("_", " ").toLowerCase().substring(1);
    }
}