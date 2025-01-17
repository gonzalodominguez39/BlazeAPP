package com.emma.blaze.data.repository;

import android.content.Context;

import com.emma.blaze.data.api.RetrofitClient;
import com.emma.blaze.data.model.Swipe;
import com.emma.blaze.data.service.SwipeService;


import retrofit2.Call;

public class SwipeRepository {
    private final SwipeService swipeService;

    public SwipeRepository(Context context) {
        this.swipeService = RetrofitClient.getRetrofitInstance(context).create(SwipeService.class);
    }

    public Call<Boolean> saveSwipe(Swipe swipeRequest) {
        return swipeService.saveSwipe(swipeRequest);
    }

}
