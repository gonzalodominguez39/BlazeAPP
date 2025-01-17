package com.emma.blaze.data.service;

import com.emma.blaze.data.model.Swipe;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SwipeService {
    @POST("/api/swipes/save")
    Call<Boolean> saveSwipe(@Body Swipe swipeRequest);
}
