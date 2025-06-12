package com.tomotives.tomotives.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.models.Location;
import com.tomotives.tomotives.models.Review;
import com.tomotives.tomotives.models.User;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    }//end getLocationObjectList method

    /**Joshua
     * Retrieves a arraylist with all the locations from the file
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
    }//end getLocationList method

    /**Joshua
     * Retrieves a list of Location objects as type Location from the locations JSON file
     *
     * @return an ArrayList of Location objects as type Location, or null if an IOException occurs while reading the file
     */
    public static ArrayList<String> getLocationNamesList() {
        ArrayList<Object> locationList = getLocationObjectList();
        ArrayList<String> locations = new ArrayList<>();
        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        for (Object locationObject : locationList) {
            Map<String, Object> locationMap = gson.fromJson(gson.toJson(locationObject), mapType);
            locations.add((String) locationMap.get("name"));
        }
        return locations;
    }//end getLocationNamesList method

    public static ArrayList<Location> filterLocationListByName(String searchTerm) {
        ArrayList<Location> locations = getLocationList();

        // filter locations based on name similarity and substring matching
        Map<Location, Integer> distanceMap = new HashMap<>();
        for (Location location : locations) {
            String locationName = location.getName().toLowerCase();
            String searchTermLower = searchTerm.toLowerCase();

            // check if search term is a substring of location name
            if (locationName.contains(searchTermLower)) {
                distanceMap.put(location, 0);
            } else {
                int distance = LevenshteinDistance.getDefaultInstance()
                    .apply(locationName, searchTermLower);

                // split location name into words and check each word
                String[] words = locationName.split("\\s+");
                for (String word : words) {
                    int wordDistance = LevenshteinDistance.getDefaultInstance()
                        .apply(word, searchTermLower);
                    distance = Math.min(distance, wordDistance);
                }

                distanceMap.put(location, distance);
            }
        }

        return distanceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()) // sorted (to put the closest matches first)
                .limit(20) // 20 results max
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Location> filterLocationListByCategories(ArrayList<Category> categories) {
        //create arrayList of all the locations and an empty arrayList for locations with given category
        ArrayList<Location> filteredLocations =  new ArrayList<>();
        ArrayList<Location> unfilteredLocations = getLocationList();

        //loop through all locations
        for (Location location : unfilteredLocations){

            //creates an arraylist of the categories each location has
            ArrayList<Category> locationCategories = location.getCategories();

            //compares category of the location to the category user inputed
            for(Category category : locationCategories){

                //if the location has user's inputted category
                if (categories.contains(category)) {
                    filteredLocations.add(location);
                    break;
                }
            }
        }
        return filteredLocations;
    }//end filterLocationLostByCategories method

    public static ArrayList<Location> filterLocationListByRating(double min, double max) {
        ArrayList<Location> filteredLocations = new ArrayList<>();
        ArrayList<Location> unfilteredLocations = getLocationList();

        for (Location location : unfilteredLocations){
            //get the ratings
            double rating = location.getRating();
            if(rating>=min && rating<=max){
                filteredLocations.add(location);
            }
        }

        return filteredLocations;
    }//end filterLocationByRating method

    public static ArrayList<Location> filterLocationListByPrice(double min, double max) {
        ArrayList<Location> filteredLocations = new ArrayList<>();
        ArrayList<Location> unfilteredLocations = getLocationList();

        for (Location location : unfilteredLocations){
            //get the price
            double price = location.getRating();
            if(price>=min && price<=max){
                filteredLocations.add(location);
            }
        }

        return filteredLocations;
    }//end filteredLocationListByPrice method

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

    public static int getLocationAmountOfFavourites(Location location) {
        int counter = 0;
        // given a location, return the amount of favorites it has NOTE: favorites are stored by user, not location.
        // steps: loop through all users (UserService.getUserList()) will provide you will all users
        // for each user, check if the user has the location in their favorites list. If so, add to a counter, and eventually return the counter

        for (User user : UserService.getUserList()) {
            if (user.getFavourites().contains(location.getName())) {
                counter++;
            }
        }

        return counter;
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
    public static void removeReview(String locationName, String reviewDescription) {
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

                    // find and remove the review with the matching description
                    reviews.removeIf(review -> reviewDescription.equals(review.get("description")));

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
