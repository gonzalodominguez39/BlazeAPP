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
import com.emma.blaze.databinding.FragmentMessageBinding;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class UserMessage extends Fragment {

    private UserMessageViewModel mViewModel;
    private WebSocket webSocket;
    private OkHttpClient client;
    private FragmentMessageBinding binding;

    public static UserMessage newInstance() {
        return new UserMessage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);

        client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(requireContext().getString(R.string.WEBSOCKETSERVER))
                .build();

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Log.d("WebSocket", "Conexión abierta");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                Log.d("WebSocket", "Mensaje recibido: " + text);
                // Aquí puedes actualizar la interfaz de usuario con el mensaje recibido
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                Log.d("WebSocket", "Mensaje recibido: " + bytes.hex());
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                Log.e("WebSocket", "Error: " + t.getMessage());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.d("WebSocket", "Conexión cerrada: " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                Log.d("WebSocket", "Conexión cerrada permanentemente: " + reason);
            }
        };

        webSocket = client.newWebSocket(request, listener);





        return binding.getRoot();
    }
    public void sendMessage(String message){
        if (webSocket != null) {
            webSocket.send(message);
        }
    }


}