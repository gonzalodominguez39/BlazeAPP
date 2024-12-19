package com.emma.blaze.data.repository;

import com.emma.blaze.data.api.RetrofitClient;
import com.emma.blaze.data.model.User;
import com.emma.blaze.data.service.UserService;

import retrofit2.Call;

public class UserRepository {
    private final UserService userService;

    public UserRepository() {
        this.userService = RetrofitClient.getRetrofitInstance().create(UserService.class);
    }

    public Call<User> registerUser(User userRequest) {
        return userService.saveUser(userRequest);
    }
}