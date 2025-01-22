package com.emma.blaze.ui.message;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.data.dto.UserResponse;
import com.emma.blaze.data.model.Message;
import com.emma.blaze.helpers.WebSocketClient;

import java.util.ArrayList;
import java.util.List;


public class UserMessageViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>();
   private final MutableLiveData<UserResponse> user2Connect = new MutableLiveData<>();
    private final WebSocketClient chatClient;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> MessageSend = new MutableLiveData<>();

    public UserMessageViewModel(@NonNull Application application) {
        super(application);
        chatClient = new WebSocketClient();
    }

    public WebSocketClient getChatClient() {
        return chatClient;
    }

    public MutableLiveData<List<Message>> getMessages() {
        return messages;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getMessageSend() {
        return MessageSend;
    }

    public void connectToPrivateChat(String user1, String user2, Context context) {
        chatClient.connect(user1, user2, context);

    }


    public void sendMessage(String user1, String user2, String message) {
        chatClient.sendMessage(user1, user2, message);
    }


    public void disconnect() {
        chatClient.disconnect();
    }

    public MutableLiveData<UserResponse> getUser2Connect() {
        return user2Connect;
    }
}