/* The FriendStatus enum represents the status of friendship between two users in the application
 *
 * Project TOMotives
 * Programmers: Joshua Holzman-Sharfe, Saul Mesbur, Choeying Augarshar, Jessica Li, Emmett Cassan
 * Last Edited: June 12, 2025
 */

package com.tomotives.tomotives.models;

public enum FriendStatus {
    FRIEND,
    REQUESTED, // outgoing from current user
    RECEIVED, // incoming to current user
    NOT_FRIEND,
}
