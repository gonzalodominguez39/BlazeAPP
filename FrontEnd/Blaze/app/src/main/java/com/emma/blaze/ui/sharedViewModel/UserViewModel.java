package com.emma.blaze.ui.sharedViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.model.User;
import com.emma.blaze.data.repository.UserRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {


    private final UserRepository userRepository;
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
        userLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }


    public void saveUser() {
        Call<User> call = userRepository.registerUser(userLiveData.getValue());
        Log.d("saveuser","saveUser: "+userLiveData.getValue().getProfilePictures());
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User savedUser = response.body();
                    if (savedUser != null) {
                        userLiveData.setValue(savedUser);
                        isLoading.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
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

    }