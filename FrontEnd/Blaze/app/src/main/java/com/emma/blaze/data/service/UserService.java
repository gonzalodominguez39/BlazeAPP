package com.emma.blaze.data.service;

import com.emma.blaze.data.model.User;
import com.emma.blaze.data.response.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface UserService {

    @POST("/api/users/save")
    Call<User> saveUser(@Body User userRequest);

    @GET("/api/users")
    Call <List<UserResponse>> getAllUsers();

}

