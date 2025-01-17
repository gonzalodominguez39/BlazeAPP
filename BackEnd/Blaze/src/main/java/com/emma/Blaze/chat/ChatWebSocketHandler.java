package com.emma.Blaze.chat;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessions.add(session);
        LOGGER.info("Nueva conexión WebSocket establecida: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userMessage = message.getPayload();
        LOGGER.info("Mensaje recibido: " + userMessage);


        for (WebSocketSession s : sessions) {
            try {
                s.sendMessage(new TextMessage("Echo: " + userMessage));
            } catch (Exception e) {
                LOGGER.error("Error al enviar el mensaje: ", e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        sessions.remove(session);
        LOGGER.info("Conexión WebSocket cerrada: " + session.getId());
    }
}
