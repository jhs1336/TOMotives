package com.tomotives.tomotives;

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
    StudySpot,
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