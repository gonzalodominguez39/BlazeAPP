package com.emma.blaze.ui.message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emma.blaze.R;
import com.emma.blaze.data.model.Message;
import com.emma.blaze.databinding.FragmentMessageBinding;
import com.emma.blaze.helpers.WebSocketClient;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class UserMessage extends Fragment {

    private OkHttpClient client;
    private WebSocket webSocket;
    private String WEBSOCKET_SERVER_URL; // Cambia esta URL a tu servidor WebSocket

    private FragmentMessageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        connectToPrivateChat("2", "1");
        return binding.getRoot();
    }

    public void connectToPrivateChat(String user1, String user2) {
        WebSocketClient chatClient = new WebSocketClient();
        chatClient.connect("2", "1",requireContext());
        chatClient.sendMessage("2", "1", "bien y tu?");
    }

}
