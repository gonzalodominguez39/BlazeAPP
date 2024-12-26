package com.emma.blaze.data.service;

import com.emma.blaze.data.model.Interest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterestService {

    @GET("/api/interest")
    Call<List<Interest>> allInterest();
}
