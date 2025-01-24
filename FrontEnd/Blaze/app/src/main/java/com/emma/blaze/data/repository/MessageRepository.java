package com.emma.blaze.data.repository;

import android.content.Context;

import com.emma.blaze.data.api.RetrofitClient;
import com.emma.blaze.data.model.Message;
import com.emma.blaze.data.model.Swipe;
import com.emma.blaze.data.service.MessageService;
import com.emma.blaze.data.service.SwipeService;

import java.util.List;

import retrofit2.Call;

public class MessageRepository {

    private final MessageService messageService;

    public MessageRepository(Context context) {
        this.messageService = RetrofitClient.getRetrofitInstance(context).create(MessageService.class);
    }

    public Call<List<Message>> findLastMessageBetweenUsers(long userId) {
        return messageService.getLastMessagesForUser(userId);
    }
}
