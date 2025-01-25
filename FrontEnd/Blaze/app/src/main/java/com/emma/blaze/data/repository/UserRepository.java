package com.emma.blaze.data.repository;

import android.content.Context;

import com.emma.blaze.data.api.RetrofitClient;
import com.emma.blaze.data.model.User;
import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.data.service.UserService;

import java.util.List;

import retrofit2.Call;

public class UserRepository {
    private final UserService userService;

    public UserRepository(Context context) {
        this.userService = RetrofitClient.getRetrofitInstance(context).create(UserService.class);
    }
    public Call<List<UserResponse>> getAllUsers() {
        return userService.getAllUsers();
    }

    public Call<UserResponse> registerUser(User userRequest) {
        return userService.saveUser(userRequest);
    }

    public Call<UserResponse> getUserByEmail(String email){
        return userService.getUserByEmail(email);
    }
    public Call<UserResponse> getUserByPhone(String phone){
        return userService.getUserByPhone(phone);
    }
   public Call<UserResponse>updateUser(Long id,User user){
        return userService.updateUser(id,user);
    }

}