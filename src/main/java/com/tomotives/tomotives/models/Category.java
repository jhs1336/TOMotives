package com.tomotives.tomotives.models;

/** Joshua + Saul
 * Represents the different categories of activities or places that can be associated with a location
 */
public enum Category {
    // copied from mindomo diagram
    Indoors,
    Outdoors,
    Park,
    Nature,
    Budget,
    Clothing,
    Vintage,
    WithFriends("With Friends"),
    Solo,
    DateSpot("Date Spot"),
    NightTime("Night Time"),
    Landmark,
    Birdwatching,
    Family,
    Restaurant,
    Cafe,
    Swimming,
    Beach,
    DogFriendly( "Dog-friendly"),
    Breakfast,
    Lunch,
    Dinner,
    StudySpot("Study Spot"),
    Walking,
    Tourist,
    Bars,
    Sledding,
    Mall,
    Shop,
    Shopping,
    Hiking,
    Biking,
    Scenic,
    Historic,
    Museum,
    Parking,
    Music,
    Gym,
    CarRental( "Car Rental"),
    CarWash( "Car Wash"),
    CarRepair( "Car Repair"),
    Educational;


    private final String name;

    Category() {
        this.name = toString();
    }
    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}