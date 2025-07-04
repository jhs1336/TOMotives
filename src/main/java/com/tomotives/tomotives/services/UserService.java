/* The UserService is a service which handles all operations related to users and provides a service for other parts of the application to access data from
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomotives.tomotives.models.Category;
import com.tomotives.tomotives.models.FriendStatus;
import com.tomotives.tomotives.models.User;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class UserService {
    private final static String USERS_FILE_PATH = "src/main/resources/com/tomotives/tomotives/data/users.json";
    private static Gson gson = new Gson();

    /**Joshua
     * Retrieves a list of user objects, as type Object from the users JSON file
     * @return an ArrayList of user objects as type Object, or null if an IOException occurs while reading the file
     */
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

    /** Joshua
     * Retrieves a list of user objects, as type User from the users JSON file
     * @return an ArrayList of user objects as type User, or null if an IOException occurs while reading the file
     */
    public static ArrayList<User> getUserList() {
        ArrayList<Object> userObjects = getUserObjectList();
        ArrayList<User> users = new ArrayList<>();
        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        for (Object userObject : userObjects) {
            users.add(getUserFromMap(gson.fromJson(gson.toJson(userObject), mapType)));
        }
        return users;
    }

    /**Joshua
     * Retrieves a user object from a map
     * @param map a map of user data
     * @return a User object
     */
    public static User getUserFromMap(Map<String, Object> map) {
        return new User((String) map.get("email"), (String) map.get("firstName"), (String) map.get("lastName"), (String) map.get("password"), (String) map.get("displayName"), (ArrayList<String>) map.get("favourites"), (ArrayList<String>) map.get("recentLocations"), (ArrayList<String>) map.get("friends"), (ArrayList<Category>) map.get("likedCategories"));
    }

    /**Saul
     * gets a user object from an email
     * @param email the email of the user to get
     * @return the user object
     */
    public static User getUserFromEmail(String email) {
        for (User user : getUserList()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**Saul
     * gets a user object from a display name
     * @param displayName the display name of the user to get
     * @return the user object
     */
    public static User getUserFromDisplayName(String displayName) {
        for (User user : getUserList()) {
            if (user.getDisplayName().equals(displayName)) {
                return user;
            }
        }
        return null;
    }

    /** Jessica - goes through the user's friend list and checks if the users they have added are mutual friends
     * @param user the user to get the friends of
     * @return mutualFriends is the arrayList of friends of the user that have added them back
     */
    public static ArrayList<User> getUserFriends(User user) {
        ArrayList<User> mutualFriends = new ArrayList<>();

        for (int i = 0; i < user.getFriends().size(); i++) {
            User friend = getUserFromDisplayName(user.getFriends().get(i));
            if (friend != null && friend.getFriends().contains(user.getDisplayName())) {
                mutualFriends.add(friend);
            }
        }
        return mutualFriends;
    }


    /** Jessica
     * Checks the friend status between two users
     * @param user the first user being compared
     * @param otherUser the second user being compared
     * return FriendStatus that describe their friend status
     */
    public static FriendStatus getUserFriendshipStatus(User user, User otherUser) {
        boolean userHasOtherUser = user.getFriends().contains(otherUser.getDisplayName());
        boolean otherUserHasUser = otherUser.getFriends().contains(user.getDisplayName());

        if (userHasOtherUser && otherUserHasUser) {
            return FriendStatus.FRIEND;
        } else if (userHasOtherUser) {
            return FriendStatus.REQUESTED;
        } else if (otherUserHasUser) {
            return FriendStatus.RECEIVED;
        } else {
            return FriendStatus.NOT_FRIEND;
        }
    } // end getUserFriendshipStatus

    /**Joshua
     * gets a list of other users that the user may know, based on their liked categories, and mutual friends
     * @param user the user to get the people they may know for
     * @return a map of users and their amount of commonalities
     */
    public static Map<User, Integer> getPeopleUserMayKnow(User user) {
        Map<User, Integer> peopleUserMayKnow = new HashMap<>();
        for (User otherUser : getUserList()) {
            // start check for common categories
            if (otherUser.getDisplayName().equals(user.getDisplayName()) || getUserFriendshipStatus(user, otherUser) != FriendStatus.NOT_FRIEND) {
                continue;
            }
            int commonCategories = 0;

            if (user.getLikedCategories() != null) {
                for (int i = 0; i < user.getLikedCategories().size(); i++) {
                    if (otherUser.getLikedCategories() == null) break;
                    if (otherUser.getLikedCategories().contains(user.getLikedCategories().get(i))) {
                        commonCategories++;
                    }
                }
            }

            // END check for common categories
            // check for mutual friends
            int mutualFriends = 0;
            if (user.getFriends() != null) {
                for (String friend : user.getFriends()) {
                    if (otherUser.getFriends().contains(friend)) {
                        mutualFriends++;
                    }
                }
            }
            if (commonCategories + mutualFriends > 0) peopleUserMayKnow.put(otherUser, commonCategories + mutualFriends);
        }

        // sort the map by value
        return peopleUserMayKnow.entrySet()
                .stream()
                .sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /** Joshua
     * sets the user's liked categories
     * @param userDisplayName the user to set the liked categories for
     * @param categories the categories to set
     */
    public static void setLikedCategories(String userDisplayName, List<Category> categories) {
        try {
            // read the current users from the file
            String userListJson = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
            ArrayList<Map<String, Object>> users = gson.fromJson(userListJson, listType);

            // find the user by display name
            for (Map<String, Object> user : users) {
                if (user.get("displayName").equals(userDisplayName)) {
                    // Convert categories to list of strings
                    List<String> categoryStrings = categories.stream().map(Category::name).collect(Collectors.toList());

                    // update the likedCategories field
                    user.put("likedCategories", categoryStrings);

                    // write the updated data back to the file
                    Files.write(Paths.get(USERS_FILE_PATH), gson.toJson(users).getBytes());
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Joshua
     * adds a new user
     * @param user the user to add
     */
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

    /** Joshua
     * edits a user
     * @param userDisplayName the user to edit
     * @param replacementUser the user to replace with
     */
    public static void editUser(String userDisplayName, User replacementUser) {
        try {
            // read the current users from the file
            String userListJson = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
            ArrayList<Map<String, Object>> users = new Gson().fromJson(userListJson, listType);

            // find and update the user in the list
            for (Map<String, Object> user : users) {
                if (user.get("displayName").equals(userDisplayName)) {
                    // update fields
                    user.put("email", replacementUser.getEmail());
                    user.put("firstName", replacementUser.getFirstName());
                    user.put("lastName", replacementUser.getLastName());
                    user.put("password", replacementUser.getPassword());
                    user.put("displayName", replacementUser.getDisplayName());

                    break;
                }
            }
            // write the updated data back to the file
            Files.write(Paths.get(UserService.USERS_FILE_PATH), new Gson().toJson(users).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Joshua
     * adds a friend to a user
     * @param userDisplayName the user to add the friend to
     * @param friendDisplayName the friend to add
     */
    public static void addFriend(String userDisplayName, String friendDisplayName) {
        try {
            // read the current users from the file
            String userListJson = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
            ArrayList<Map<String, Object>> users = gson.fromJson(userListJson, listType);

            // find the user by name
            for (Map<String, Object> user : users) {
                if (user.get("displayName").equals(userDisplayName)) {
                    // add the friend to the user's friends list
                    List<String> friends = (List<String>) user.get("friends");
                    friends.add(friendDisplayName);
                    user.put("friends", friends);
                    Files.write(Paths.get(USERS_FILE_PATH), gson.toJson(users).getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Joshua
     * removes a friend from a user
     * @param userDisplayName the user to remove the friend from
     * @param friendDisplayName the friend to remove
     */
    public static void removeFriend(String userDisplayName, String friendDisplayName) {
        try {
            // read the current users from the file
            String userListJson = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {
            }.getType();
            ArrayList<Map<String, Object>> users = gson.fromJson(userListJson, listType);

            // find the user by name
            for (Map<String, Object> user : users) {
                if (user.get("displayName").equals(userDisplayName)) {
                    // add the friend to the user's friends list
                    List<String> friends = (List<String>) user.get("friends");
                    friends.remove(friendDisplayName);
                    user.put("friends", friends);
                    Files.write(Paths.get(USERS_FILE_PATH), gson.toJson(users).getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Joshua
     * adds a recent location to a user
     * @param displayName the user to add the recent location to
     * @param location the recent location to add
     */
    public static void addRecentLocationToUser(String displayName, String location) {
        try {
            // read the current users from the file
            String userListJson = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
            ArrayList<Map<String, Object>> users = gson.fromJson(userListJson, listType);

            // find the user by name
            for (Map<String, Object> user : users) {
                if (user.get("displayName").equals(displayName)) {
                    // get the recent locations array
                    ArrayList<String> recentLocations = gson.fromJson(gson.toJson(user.get("recentLocations")), new TypeToken<ArrayList<String>>(){}.getType());
                    if (recentLocations.isEmpty()) {
                        recentLocations.add(location);
                        return;
                    }
                    // if the location is last location viewed, return
                    if (recentLocations.getLast().equals(location)) return;
                    // if location exists in array, remove it
                    if (recentLocations.contains(location)) recentLocations.remove(location);
                    // remove the first location if the array is full
                    else if (recentLocations.size() >= 10) recentLocations.removeFirst();
                    recentLocations.add(location);

                    // update the array
                    user.put("recentLocations", recentLocations);

                    // write the updated data back to the file
                    Files.write(Paths.get(USERS_FILE_PATH), gson.toJson(users).getBytes());

                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Joshua
     * If the user already has the location in their favourites, remove it
     * If the user does not have the location in their favourites, add it
     * @param displayName the user to add the favourite to
     * @param location the favourite to add
     */
    public static void addOrRemoveUserFavourite(String displayName, String location) {
        try {
            // read the current users from the file
            String userListJson = new String(Files.readAllBytes(Paths.get(USERS_FILE_PATH)));
            Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();
            ArrayList<Map<String, Object>> users = gson.fromJson(userListJson, listType);

            // find the user by name
            for (Map<String, Object> user : users) {
                if (user.get("displayName").equals(displayName)) {
                    // get the favourites array
                    ArrayList<String> favourites = gson.fromJson(gson.toJson(user.get("favourites")), new TypeToken<ArrayList<String>>(){}.getType());
                    if (favourites.contains(location)) favourites.remove(location);
                    else favourites.add(location);

                    // update the array
                    user.put("favourites", favourites);

                    // write the updated data back to the file
                    Files.write(Paths.get(USERS_FILE_PATH), gson.toJson(users).getBytes());

                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
