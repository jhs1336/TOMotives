package com.tomotives.tomotives;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class UserDataManager {
    private static String usersFilePath = "src/main/resources/com/tomotives/tomotives/data/users.json";
    private static Gson gson = new Gson();

    public static ArrayList<Object> getUserObjectList() {
        try {
            String userList = new String(Files.readAllBytes(Paths.get(usersFilePath)));
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
}
