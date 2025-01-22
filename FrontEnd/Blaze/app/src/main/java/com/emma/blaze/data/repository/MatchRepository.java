package com.emma.blaze.data.repository;

import android.content.Context;

import com.emma.blaze.data.api.RetrofitClient;
import com.emma.blaze.data.model.UserMatch;
import com.emma.blaze.data.service.MatchService;

import java.util.List;

import retrofit2.Call;


public class MatchRepository {
    private MatchService matchService;

    public MatchRepository(Context context) {
        this.matchService = RetrofitClient.getRetrofitInstance(context).create(MatchService.class);
    }

    public Call<List<UserMatch>> getAllMatchesByUserId(Long userId) {
        return matchService.getAllMatchesByUserId(userId);
    }

}
