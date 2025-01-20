package com.emma.blaze.helpers;
import android.content.Context;
import android.util.Log;


import com.emma.blaze.R;
import com.emma.blaze.data.model.Message;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.*;
import okio.ByteString;


public class WebSocketClient {
    private static final String TAG = "WebSocketClient";
    private  String SERVER_URL ; // URL del servidor WebSocket
    private OkHttpClient client;
    private WebSocket webSocket;

    public void connect(String userId, String recipientId, Context context) {
        client = new OkHttpClient();
        SERVER_URL = context.getString(R.string.WEBSOCKETSERVER);
        Log.d("webSocketClient", "connect: "+SERVER_URL+"/chat?userId=" + userId + "&recipientId=" + recipientId);
        Request request = new Request.Builder()
                .url(SERVER_URL+"/chat?userId=" + userId + "&recipientId=" + recipientId)
                .build();

        // Crear el WebSocket
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d(TAG, "Conexión WebSocket abierta");
                // Aquí puedes enviar un mensaje de bienvenida o autenticación si es necesario
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "Mensaje recibido: " + text);
                // Maneja los mensajes que recibes del servidor
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                Log.d(TAG, "Mensaje recibido en formato binario");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e(TAG, "Error en WebSocket", t);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "Conexión WebSocket cerrada. Razón: " + reason);
            }
        });


    }

    // Método para enviar un mensaje a través del WebSocket
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

    // Método para cerrar la conexión WebSocket
    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Cierre normal");
            Log.d(TAG, "Conexión WebSocket cerrada");
        }
    }
}
