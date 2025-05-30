package com.tomotives.tomotives.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.models.Review;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LocationService {
    private final static String LOCATIONS_FILE_PATH = "src/main/resources/com/tomotives/tomotives/data/locations.json";
    private static Gson gson = new Gson();

    /**Joshua
     * Retrieves a list of location objects, as type Object from the locations JSON file
     *
     * @return an ArrayList of location objects as type Object, or null if an IOException occurs while reading the file
     */
    public static ArrayList<Object> getLocationObjectList() {
        try {
            String locationList = new String(Files.readAllBytes(Paths.get(LOCATIONS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
            ArrayList<Object> locations = gson.fromJson(locationList, listType);
            return locations;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**Joshua
     * Retrieves a list of Location objects as type Location from the locations JSON file
     *
     * @return an ArrayList of Location objects as type Location, or null if an IOException occurs while reading the file
     */
    public static ArrayList<Location> getLocationList() {
        ArrayList<Object> locationList = getLocationObjectList();
        ArrayList<Location> locations = new ArrayList<>();
        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        for (Object locationObject : locationList) {
            locations.add(getLocationFromMap(gson.fromJson(gson.toJson(locationObject), mapType)));
        }
        return locations;
    }

    public static Location getLocation(String name) {
        for (Location location : getLocationList()) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null;
    }
    /**Joshua
     * Constructs a new Location object from the provided Map of location data
     *
     * @param locationMap a Map containing the location data, with keys for name, description, rating, priceRating, categories, reviews, and image (url)
     * @return a new Location object with the data from the provided Map
     */
    public static Location getLocationFromMap(Map<String, Object> locationMap) {
        ArrayList<Category> categories = gson.fromJson(gson.toJson(locationMap.get("categories")), new TypeToken<ArrayList<Category>>(){}.getType());
        ArrayList<Map<String, Object>> reviews = gson.fromJson(gson.toJson(locationMap.get("reviews")), new TypeToken<ArrayList<Map<String, Object>>>(){}.getType());
        double totalRating = 0;
        double totalPriceRating = 0;
        ArrayList<Review> reviewList = new ArrayList<>();
        for (Map<String, Object> review : reviews) {
            totalRating += (double) review.get("rating");
            totalPriceRating += (double) review.get("priceRating");

            String dateStr = (String) review.get("date");
            String[] dateParts = dateStr.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int day = Integer.parseInt(dateParts[2]);
            reviewList.add(new Review((String) review.get("description"), (double) review.get("rating"), (double) review.get("priceRating"), (String) review.get("user"), new Date(year - 1900, month, day)));
        }

        int reviewCount = reviews.size();
        double averageRating = reviewCount > 0 ? totalRating / reviewCount : 0;
        double averagePriceRating = reviewCount > 0 ? totalPriceRating / reviewCount : 0;


        return new Location((String) locationMap.get("name"), (String) locationMap.get("description"), averageRating, averagePriceRating, categories, reviewList, (String) locationMap.get("image"));
    }

    public static void addReview(String locationName, Review review) {
        if (!Files.exists(Paths.get(LOCATIONS_FILE_PATH))) return;

        try {
            // read the current locations from the file
            String locationListJson = new String(Files.readAllBytes(Paths.get(LOCATIONS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
            ArrayList<Map<String, Object>> locations = gson.fromJson(locationListJson, listType);

            // find the location by name
            for (Map<String, Object> location : locations) {
                if (location.get("name").equals(locationName)) {
                    // get the reviews array for this location
                    ArrayList<Map<String, Object>> reviews = gson.fromJson(gson.toJson(location.get("reviews")), new TypeToken<ArrayList<Map<String, Object>>>(){}.getType());

                    // create a new review map
                    Map<String, Object> reviewMap = Map.of(
                        "description", review.getDescription(),
                        "rating", review.getRating(),
                        "priceRating", review.getPriceRating(),
                        "user", review.getUser(),
                        "date", new SimpleDateFormat("yyyy-MM-dd").format(review.getDate())
                    );

                    // add the new review to the reviews array
                    reviews.add(reviewMap);

                    // update the reviews in the location
                    location.put("reviews", reviews);

                    // write the updated locations back to the file
                    Files.write(Paths.get(LOCATIONS_FILE_PATH), gson.toJson(locations).getBytes());

                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
