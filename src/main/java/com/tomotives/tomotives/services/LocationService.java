/* The LocationService is a service which handles all operations related to locations and provides a service for other parts of the application to access data from
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 13, 2025
 */

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

    /**Saul
     * gets the location object from the location list
     * @param name the name of the location
     * @return the location object
     */
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
        // add all reviews from location
        for (Map<String, Object> review : reviews) {
            totalRating += (double) review.get("rating");
            totalPriceRating += (double) review.get("priceRating");

            String dateStr = (String) review.get("date");
            String[] dateParts = dateStr.split("-");
            // get parts of date
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int day = Integer.parseInt(dateParts[2]);
            // add to review list
            reviewList.add(new Review((String) review.get("description"), (double) review.get("rating"), (double) review.get("priceRating"), (String) review.get("user"), new Date(year - 1900, month, day)));
        }

        // calculate averages
        int reviewCount = reviews.size();
        double averageRating = reviewCount > 0 ? totalRating / reviewCount : 0;
        double averagePriceRating = reviewCount > 0 ? totalPriceRating / reviewCount : 0;


        return new Location((String) locationMap.get("name"), (String) locationMap.get("description"), averageRating, averagePriceRating, categories, reviewList, (String) locationMap.get("image"));
    }

    /** Emmett
     * get the amount of favourites on a given location
     * @param location location to get favourites from
     * @return the amount of favourites
     */
    public static int getLocationAmountOfFavourites(Location location) {
        int counter = 0;

        for (User user : UserService.getUserList()) {
            if (user.getFavourites().contains(location.getName())) {
                counter++;
            }
        }

        return counter;
    }

    /**Joshua
     * add a review to a location
     * @param locationName the name of the location to add the review to
     * @param review the review to add
     */
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

    /**Joshua
     * remove a review from a location
     * @param locationName the name of the location to remove the review from
     * @param reviewDescription the description of the review to remove
     */
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

    /**Joshua
     * filters the location list by name
     * @param searchTerm the name of the location to search for
     * @return an ArrayList of the best 20 matches for the search term, sorted by similarity to the search term
     */
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

    /**Choeying
     * filters the location list by category(ies)
     * @param categories the categories to filter by
     * @return the list of locations that have one of the categories provided
     */
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

    /**Choeying
     * filters the location list by rating
     * @param min minimum rating
     * @param max maximum rating
     * @return the list of locations that have a rating between the min and max
     */
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

    /**Choeying
     * filters the location list by price rating
     * @param min minimum rating
     * @param max maximum rating
     * @return the list of locations that have a price rating between the min and max
     */
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
}
