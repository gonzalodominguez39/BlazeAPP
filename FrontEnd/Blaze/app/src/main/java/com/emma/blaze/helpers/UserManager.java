package com.emma.blaze.helpers;

import com.emma.blaze.data.dto.UserResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserManager {
    private static UserManager instance;
    private final MutableLiveData<UserResponse> currentUser = new MutableLiveData<>();

    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setCurrentUser(UserResponse user) {
        currentUser.setValue(user);
    }

    public LiveData<UserResponse> getCurrentUserLiveData() {
        return currentUser;
    }


    public UserResponse getCurrentUser() {
        return currentUser.getValue();
    }


    public String getPhoneNumber() {
        return currentUser.getValue() != null ? currentUser.getValue().getPhoneNumber() : null;
    }

    public String getEmail() {
        return currentUser.getValue() != null ? currentUser.getValue().getEmail() : null;
    }

    public String getName() {
        return currentUser.getValue() != null ? currentUser.getValue().getName() : null;
    }

    public String getBiography() {
        return currentUser.getValue() != null ? currentUser.getValue().getBiography() : null;
    }

    public boolean isActive() {
        return currentUser.getValue() != null && currentUser.getValue().isStatus();
    }

    public void clearSession() {
        currentUser.setValue(null);
    }
}