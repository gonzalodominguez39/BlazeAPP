package com.emma.blaze.ui.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.repository.UserRepository;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.helpers.UserManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private UserManager userManager;
    private final MutableLiveData<Boolean> userStatus;


    MutableLiveData<UserResponse> currentUser = new MutableLiveData<>();


    public LoginViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        userManager = UserManager.getInstance();
        this.userStatus = new MutableLiveData<>();
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
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse user = response.body();
                    userManager.setCurrentUser(user);
                    currentUser.postValue(user);
                    Log.d("responsebody", "onResponse: bienvenido " + userManager.getCurrentUser().getName());
                    if (user != null && user.isStatus()) {
                        userStatus.postValue(true);
                    } else {
                        userStatus.postValue(false);
                    }
                } else { Log.d(
                        "login", ": " + response.code());
                    currentUser.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                currentUser.postValue(null);
                Log.d("login", " " + t.getMessage());
            }


        });

    }

    public MutableLiveData<Boolean> getUserStatus() {
        return userStatus;
    }
}

