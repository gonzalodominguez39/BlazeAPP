package com.emma.blaze.data.repository;

import retrofit2.Call;
import com.emma.blaze.data.api.RetrofitClient;
import com.emma.blaze.data.model.Interest;
import com.emma.blaze.data.service.InterestService;

import java.util.List;

public class InterestRepository {

    private final InterestService interestService;

    public InterestRepository() {
        this.interestService = RetrofitClient.getRetrofitInstance().create(InterestService.class);
    }

    public Call<List<Interest>> getAllInterests() {
        return interestService.allInterest();
    }
}
