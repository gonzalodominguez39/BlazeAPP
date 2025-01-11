package com.emma.blaze.ui.sharedViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.model.User;
import com.emma.blaze.data.repository.UserRepository;
import com.emma.blaze.data.response.UserResponse;
import com.emma.blaze.databases.UserCacheRepository;
import com.emma.blaze.helpers.UserManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {


    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;
    private final UserManager userManager;
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        this.userCacheRepository = new UserCacheRepository(application);
        this.userManager = UserManager.getInstance();
        userLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }


    public void saveUser() {
        Call<UserResponse> call = userRepository.registerUser(userLiveData.getValue());
        call.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse savedUser = response.body();
                    if (savedUser != null) {
                        userCacheRepository.createUserCache(savedUser);
                        userManager.setCurrentUser(savedUser);
                        Log.d("userViewModel", "managerdUser: "+userManager.getCurrentUser().toString());
                        isLoading.setValue(false);
                    }else{
                        Log.d("userViewModel", "managedUser: "+response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue(t.getMessage());

            }
        });

    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }



    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setBiography(user.getBiography());
        userResponse.setGender(user.getGender());
        userResponse.setGenderInterest(user.getGenderInterest());
        userResponse.setRelationshipType(user.getRelationshipType());
        userResponse.setPrivacySetting(user.getPrivacySetting());
        userResponse.setRegistrationDate(user.getRegistrationDate().toString());
        userResponse.setStatus(user.isStatus());
       userResponse.setPictureUrls(user.getProfilePictures());
        return userResponse;
    }


    }