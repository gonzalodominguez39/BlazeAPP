package com.emma.blaze.ui.sharedViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.model.User;
import com.emma.blaze.data.repository.UserRepository;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.databases.UserCache;
import com.emma.blaze.databases.UserCacheRepository;
import com.emma.blaze.helpers.UserManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {


    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;

    private final MutableLiveData<Boolean> existPhoneUser;
    private final UserManager userManager;
    private final MutableLiveData<User> userLiveData;


    private final MutableLiveData<String> errorMessage;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
        this.userCacheRepository = new UserCacheRepository(application);
        this.userManager = UserManager.getInstance();
        this.existPhoneUser= new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public void userIsLogin(UserCache cachedUser) {
        if (cachedUser != null && cachedUser.isLoggedIn()) {
            Log.d("cache", "userIsLogin: " + cachedUser);
            validateUserWithBackend(cachedUser.getEmail());
        } else {
            errorMessage.setValue("No se encontró un usuario logueado en la caché");
        }
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
                        Log.d("login", ": " + userManager.getCurrentUser().getUserId());
                } else {
                    Log.d("login", ": " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Log.d("login", ": " +t.getMessage());
            }
        });
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

    public void createUserCache(UserResponse user) {
        userCacheRepository.createUserCache(user);
    }

    public MutableLiveData<User> getUserLiveData() {

        return userLiveData;
    }

    public MutableLiveData<Boolean> getExistPhoneUser() {
        return existPhoneUser;
    }

    public LiveData<UserCache> getLoggedInUser() {
        return userCacheRepository.getLoggedInUser();
    }



    public interface LoginCallback {
        void onLoginSuccess(boolean userExists, UserResponse userResponse);
        void onLoginError(String errorMessage);
    }

    public void loginWithPhone(String phone, UserCache userCache, LoginCallback callback) {
        Call<UserResponse> call = userRepository.getUserByPhone(phone);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = (UserResponse) response.body();
                    userManager.setCurrentUser(userResponse);
                    User updateUser = new User();
                    updateUser.setStatus(true);
                    updateUser(userResponse.getUserId(), updateUser);
                    if (userCache == null) {
                        createUserCache(userResponse);
                    }
                    Log.d("phone", "onResponse: " + userResponse.toString());

                    if (callback != null) {
                        callback.onLoginSuccess(true, userResponse);
                    }

                } else {
                    String errorMessage = "Error en la respuesta del servidor";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (callback != null) {
                        callback.onLoginSuccess(false, null);
                        callback.onLoginError(errorMessage);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                String errorMessage = "Error de red: " + t.getMessage();
                Log.e("Login Error", errorMessage, t);
                if (callback != null) {
                    callback.onLoginSuccess(false, null);
                    callback.onLoginError(errorMessage);
                }

            }
        });
    }

    public void updateUser(long id, User user) {
        Call<UserResponse> call = userRepository.updateUser(id, user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse updatedUser = response.body();
                    userManager.setCurrentUser(updatedUser);
                } else {
                    Log.d("updateUser", "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Log.d("updateUser", "onFailure: " + t.getMessage());

            }
        });

    }

    public void removeAccount(UserCache cachedUser) {
        if (cachedUser != null) {
            userCacheRepository.removeUser(cachedUser.getId());
        }
        userManager.clearSession();
    }

    public void deleteAccount(long userId) {
        Call<Boolean> call = userRepository.deleteUser(userId);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if(response.isSuccessful()){
                    Log.d("delete", "onResponse: usuario eliminado");
                }else{
                    Log.d("delete", "error: "+response.code());
                }

            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.d("delete", "error: "+t.getMessage());
            }
        });
        userManager.clearSession();

    }

    public void clear() {
        userLiveData.setValue(null);
        existPhoneUser.setValue(null);
        errorMessage.setValue(null);
        Log.d("UserViewModel", "clear: Datos limpiados correctamente");
    }
}