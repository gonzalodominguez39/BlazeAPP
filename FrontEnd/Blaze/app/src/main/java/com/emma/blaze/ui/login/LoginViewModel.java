package com.emma.blaze.ui.login;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.repository.UserRepository;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.helpers.UserManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private UserManager userManager;


    MutableLiveData<UserResponse> currentUser = new MutableLiveData<>();


    public LoginViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        userManager = UserManager.getInstance();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MutableLiveData<UserResponse> getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(MutableLiveData<UserResponse> currentUser) {
        this.currentUser = currentUser;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void login(String email) {
        userRepository.getUserByEmail(email).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    userManager.setCurrentUser(response.body());
                    currentUser.postValue(response.body());
                    Log.d("responsebody", "onResponse: bienvenido " + userManager.getCurrentUser().getName());
                }else {
                    currentUser.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                currentUser.postValue(null);
                Log.d("responsebody", "onResponse: bienvenido " + t.getMessage());
            }


        });

    }
}

