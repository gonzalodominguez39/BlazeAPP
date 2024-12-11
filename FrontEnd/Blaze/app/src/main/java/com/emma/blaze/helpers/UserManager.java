package com.emma.blaze.helpers;

import com.emma.blaze.model.User;

public class UserManager {
    private static UserManager instance;
    private User currentUser;

    private UserManager() {}


    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }


    public void setCurrentUser(User user) {
        this.currentUser = user;
    }


    public User getCurrentUser() {
        return currentUser;
    }


    public void clearSession() {
        this.currentUser = null;
    }

    public String getPhoneNumber() {
        return currentUser != null ? currentUser.getPhoneNumber() : null;
    }

    public String getEmail() {
        return currentUser != null ? currentUser.getEmail() : null;
    }

    public String getName() {
        return currentUser != null ? currentUser.getName() : null;
    }

    public String getBiography() {
        return currentUser != null ? currentUser.getBiography() : null;
    }

    public boolean isActive() {
        return currentUser != null && currentUser.isStatus();
    }


}
