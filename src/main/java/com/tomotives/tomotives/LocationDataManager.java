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
    public static Location getLocation() {
        try {
            String locationList = new String(Files.readAllBytes(Paths.get("src/main/resources/com/tomotives/tomotives/locations.json")));
            System.out.println(locationList);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
            ArrayList<Object> locations = gson.fromJson(locationList, listType);
            System.out.println(locations);
            String locationJson = gson.toJson(locations.get(0));
            System.out.println(locationJson);

            Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> locationMap = gson.fromJson(locationJson, mapType);
            System.out.println(locationMap);

            ArrayList<Category> categories = gson.fromJson(gson.toJson(locationMap.get("categories")), new TypeToken<ArrayList<Category>>(){}.getType());

            return new Location((String) locationMap.get("name"), (String) locationMap.get("description"), (double) locationMap.get("rating"), (double) locationMap.get("priceRating"), categories, (ArrayList<Review>) locationMap.get("reviews"), (String) locationMap.get("image"));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }}
