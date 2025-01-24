package com.emma.blaze.data.service;

import com.emma.blaze.data.model.Message;
import com.emma.blaze.data.model.Swipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MessageService {

    @GET("/api/messages/last-messages/{userId}")
    Call<List<Message>> getLastMessagesForUser(@Path("userId") long userId);
}
