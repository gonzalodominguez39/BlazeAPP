package com.emma.blaze.data.service;

import com.emma.blaze.data.model.Message;
import com.emma.blaze.data.model.Swipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MessageService {

    @GET("/api/messages/last/{user1Id}/{user2Id}")
    Call<Message> findLastMessageBetweenUsers(@Path("user1Id") long user1Id, @Path("user2Id") long  user2Id);
}
