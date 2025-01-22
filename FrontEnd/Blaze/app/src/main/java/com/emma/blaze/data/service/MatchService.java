package com.emma.blaze.data.service;

import com.emma.blaze.data.model.UserMatch;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MatchService {
    @GET("/api/matches/{userId}")
    Call<List<UserMatch>> getAllMatchesByUserId(@Path("userId") Long userId);
}
