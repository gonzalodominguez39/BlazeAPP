package com.emma.blaze.ui.login;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.emma.blaze.data.repository.UserRepository;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.helpers.UserManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final UserManager userManager;
    private final MutableLiveData<Boolean> userStatus;


    MutableLiveData<UserResponse> currentUser = new MutableLiveData<>();


    public LoginViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        userManager = UserManager.getInstance();
        this.userStatus = new MutableLiveData<>();
    }


    public MutableLiveData<UserResponse> getCurrentUser() {
        return currentUser;
    }




    public void login(String email) {
        userRepository.getUserByEmail(email).enqueue(new Callback<>() {
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
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                currentUser.postValue(null);
                Log.d("login", " " + t.getMessage());
            }


        });

    }

}

