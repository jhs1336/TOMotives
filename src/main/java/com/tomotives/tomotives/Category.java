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
    FamilyTime("Family Time"),
    Restaurant,
    Cafe,
    Breakfast,
    Lunch,
    Dinner,
    StudySpot,
    Walking,
    TouristAttraction("Tourist Attraction"),
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