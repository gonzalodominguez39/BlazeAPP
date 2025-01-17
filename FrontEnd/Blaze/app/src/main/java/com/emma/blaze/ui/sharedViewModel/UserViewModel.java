package com.emma.blaze.ui.sharedViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.model.Swipe;
import com.emma.blaze.data.model.User;
import com.emma.blaze.data.repository.SwipeRepository;
import com.emma.blaze.data.repository.UserRepository;
import com.emma.blaze.data.response.UserResponse;
import com.emma.blaze.databases.UserCache;
import com.emma.blaze.databases.UserCacheRepository;
import com.emma.blaze.helpers.UserManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {


    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;

    private final UserManager userManager;

    private final MutableLiveData<Boolean> isLoggedIn;
    private final MutableLiveData<User> userLiveData;

    private final MutableLiveData<String> errorMessage;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        this.userCacheRepository = new UserCacheRepository(application);
        this.userManager = UserManager.getInstance();
        isLoggedIn = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public void userIsLogin() {
        userCacheRepository.getLoggedInUser().observeForever(cachedUser -> {
            if (cachedUser != null && cachedUser.isLoggedIn()) {
                validateUserWithBackend(cachedUser.getEmail());
            } else {
                isLoggedIn.setValue(false);
                errorMessage.setValue("No se encontró un usuario logueado en la caché");
            }
        });
    }

    private void validateUserWithBackend(String email) {
        Call<UserResponse> call = userRepository.getUserByEmail(email);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    Log.d("login", "onResponse: " + userResponse);
                    userManager.setCurrentUser(userResponse);
                    updateUserCache(userResponse, true);
                    isLoggedIn.setValue(true);
                } else {
                    errorMessage.setValue("Error en la respuesta del servidor");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                errorMessage.setValue("Error al contactar al servidor: " + t.getMessage());
            }
        });
    }

    private void updateUserCache(UserResponse userResponse, boolean loggedIn) {
        UserCache cachedUser = new UserCache();
        cachedUser.setEmail(userResponse.getEmail());
        cachedUser.setName(userResponse.getName());
        cachedUser.setLoggedIn(loggedIn);
        userCacheRepository.update(cachedUser);
    }


    public void saveUser() {
        Call<UserResponse> call = userRepository.registerUser(userLiveData.getValue());
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse savedUser = response.body();
                    if (savedUser != null) {
                        userCacheRepository.createUserCache(savedUser);
                        userManager.setCurrentUser(savedUser);
                        Log.d("userViewModel", "managerdUser: " + userManager.getCurrentUser().toString());
                    } else {
                        Log.d("userViewModel", "managedUser: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                errorMessage.setValue(t.getMessage());

            }
        });

    }


    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }


    public MutableLiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }


    public void loginWithPhone(String phone) {
        Call<UserResponse> call = userRepository.getUserByPhone(phone);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()&&response.body()!=null){
                    UserResponse userResponse = (UserResponse) response.body();
                    userManager.setCurrentUser(userResponse);
                    updateUserCache(userResponse, true);
                    isLoggedIn.setValue(true);
                    Log.d("phone", "onResponse: "+userResponse.toString());
                }else {
                    isLoggedIn.setValue(false);
                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d("phone", "onFailure: "+ t.getMessage());
            }
        });
    }
}