package com.tomotives.tomotives;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class LocationDataManager {
    private static String locationsFilePath = "src/main/resources/com/tomotives/tomotives/locations.json";
    private static Gson gson = new Gson();

    // dont use
//    public static Location getLocation() {
//        try {
//            String locationList = new String(Files.readAllBytes(Paths.get(locationsFilePath)));
//            Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
//            ArrayList<Object> locations = gson.fromJson(locationList, listType);
//            String locationJson = gson.toJson(locations.get(0));
//
//            Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
//            Map<String, Object> locationMap = gson.fromJson(locationJson, mapType);
//
//            return getLocationFromMap(locationMap);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static ArrayList<Object> getLocationObjectList() {
        try {
            String locationList = new String(Files.readAllBytes(Paths.get(locationsFilePath)));
            Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
            ArrayList<Object> locations = gson.fromJson(locationList, listType);
            return locations;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<Location> getLocationList() {
        ArrayList<Object> locationList = getLocationObjectList();
        ArrayList<Location> locations = new ArrayList<>();
        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        for (Object locationObject : locationList) {
            locations.add(getLocationFromMap(gson.fromJson(gson.toJson(locationObject), mapType)));
        }
        return locations;
    }

    public static Location getLocationFromMap(Map<String, Object> locationMap) {
        ArrayList<Category> categories = gson.fromJson(gson.toJson(locationMap.get("categories")), new TypeToken<ArrayList<Category>>(){}.getType());
        return new Location((String) locationMap.get("name"), (String) locationMap.get("description"), (double) locationMap.get("rating"), (double) locationMap.get("priceRating"), categories, (ArrayList<Review>) locationMap.get("reviews"), (String) locationMap.get("image"));
    }
}
