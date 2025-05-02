package com.tomotives.tomotives;

/** Joshua
 * Represents the different categories of activities or places that can be associated with a location
 */
public enum Category {
    // copied from mindomo diagram
    Indoors,
    Outdoors,
    Park,
    WithFriends("With Friends"),
    Solo,
    DateSpot("Date Spot"),
    NightTime("Night Time"),
    Landmark,
    Family,
    Restaurant,
    Cafe,
    Breakfast,
    Lunch,
    Dinner,
    StudySpot("Study Spot"),
    Walking,
    Tourist,
    Bars,
    Mall,
    Shop,
    Shopping,
    Hiking,
    Scenic,
    Historic,
    Museum,
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