package com.emma.blaze.helpers;

import android.content.Context;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.emma.blaze.R;
import com.emma.blaze.data.model.Message;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.*;
import okio.ByteString;


public class WebSocketClient {
    private static final String TAG = "WebSocketClient";
    private String SERVER_URL;
    private final MutableLiveData<List<Message>> messagesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Message> recivedMessage = new MutableLiveData<>();
    private List<Message> updatedMessages = new ArrayList<>();

    private OkHttpClient client;
    private WebSocket webSocket;

    public void connect(String userId, String recipientId, Context context) {
        updatedMessages = new ArrayList<>();
        client = new OkHttpClient();
        SERVER_URL = context.getString(R.string.WEBSOCKETSERVER);
        Log.d("webSocketClient", "connect: " + SERVER_URL + "/chat?userId=" + userId + "&recipientId=" + recipientId);
        Request request = new Request.Builder()
                .url(SERVER_URL + "/chat?userId=" + userId + "&recipientId=" + recipientId)
                .build();


        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                Log.d(TAG, "Conexión WebSocket abierta");
                // Aquí puedes enviar un mensaje de bienvenida o autenticación si es necesario
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d(TAG, "Mensaje recibido: " + text);

                Gson gson = new Gson();
                Message message = gson.fromJson(text, Message.class);
                if (message != null) {

                    List<Message> currentMessages = messagesLiveData.getValue();

                    if (currentMessages == null) {
                        currentMessages = new ArrayList<>();
                    }


                    currentMessages.add(message);

                    recivedMessage.postValue(message);
                    messagesLiveData.postValue(currentMessages);

                    Log.d("message", "Mensaje agregado. Total mensajes: " + currentMessages.size());
                } else {
                    Log.d(TAG, "Mensaje no válido, no agregado.");
                }
            }


            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {

            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
                Log.e(TAG, "Error en WebSocket", t);
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d(TAG, "Conexión WebSocket cerrada. Razón: " + reason);
            }
        });

    }

    public MutableLiveData<List<Message>> getMessagesLiveData() {
        return messagesLiveData;
    }

    public MutableLiveData<Message> getRecivedMessage() {
        return recivedMessage;
    }

    public void sendMessage(String userId, String recipientId, String content) {

        Map<String, Object> jsonInput = new HashMap<>();
        jsonInput.put("senderId", userId);
        jsonInput.put("recipientId", recipientId);
        jsonInput.put("content", content);


        Gson gson = new Gson();
        String jsonMessage = gson.toJson(jsonInput);


        if (webSocket != null && webSocket.send(jsonMessage)) {
            Log.d(TAG, "Mensaje enviado: " + jsonMessage);
        } else {
            Log.e(TAG, "No se pudo enviar el mensaje. WebSocket no está abierto.");
        }
    }

    private Message parseMessage(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Message.class);
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Cierre normal");
            Log.d(TAG, "Conexión WebSocket cerrada");
        }
    }
}
