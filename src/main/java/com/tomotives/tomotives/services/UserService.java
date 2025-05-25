package com.tomotives.tomotives.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tomotives.tomotives.Application;
import com.tomotives.tomotives.models.User;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final static String USERS_FILE_PATH = "src/main/resources/com/tomotives/tomotives/data/users.json";
    private static Gson gson = new Gson();

    public static ArrayList<Object> getUserObjectList() {
        try {
            String userList = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
            ArrayList<Object> users = gson.fromJson(userList, listType);
            return users;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<User> getUserList() {
        ArrayList<Object> userObjects = getUserObjectList();
        ArrayList<User> users = new ArrayList<>();
        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        for (Object userObject : userObjects) {
            users.add(getUserFromMap(gson.fromJson(gson.toJson(userObject), mapType)));
        }
        return users;
    }

    public static User getUserFromMap(Map<String, Object> map) {
        return new User((String) map.get("email"), (String) map.get("firstName"), (String) map.get("lastName"), (String) map.get("password"), (String) map.get("displayName"), (ArrayList<String>) map.get("favourites"), (ArrayList<String>) map.get("recentLocations"), (ArrayList<String>) map.get("friends"));
    }

    public static User getUserFromEmail(String email) {
        for (User user : getUserList()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
    public static User getUserFromDisplayName(String displayName) {
        for (User user : getUserList()) {
            if (user.getDisplayName().equals(displayName)) {
                return user;
            }
        }
        return null;
    }

    public static void addUser(User user) {
        if (!Files.exists(Paths.get(USERS_FILE_PATH))) return;

        Map<String, Object> userMap = Map.of(
            "email", user.getEmail(),
            "password", user.getPassword(),
            "firstName", user.getFirstName(),
            "lastName", user.getLastName(),
            "displayName", user.getDisplayName(),
            "favourites", user.getFavourites(),
            "recentLocations", user.getRecentLocations(),
            "friends", user.getFriends()
        );
        try {
            ArrayList<Map<String, Object>> users;
            // read the current users from the file
            String userListJson = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
            users = gson.fromJson(userListJson, listType);

            users.add(userMap);
            Files.write(Paths.get(USERS_FILE_PATH), gson.toJson(users).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
