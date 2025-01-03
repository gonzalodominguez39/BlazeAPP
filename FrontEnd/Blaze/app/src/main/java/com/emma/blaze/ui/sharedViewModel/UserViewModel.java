package com.emma.blaze.ui.sharedViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.model.User;
import com.emma.blaze.data.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {


    private final UserRepository userRepository;
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<List<String>> userInterestsLiveData;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
        userLiveData = new MutableLiveData<>();
        userInterestsLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public MutableLiveData<List<String>> getUserInterestsLiveData() {
        return userInterestsLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
