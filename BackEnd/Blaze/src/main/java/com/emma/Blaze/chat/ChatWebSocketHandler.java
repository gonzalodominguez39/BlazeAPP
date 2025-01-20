package com.emma.Blaze.chat;


import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.model.Message;
import com.emma.Blaze.model.UserMatch;
import com.emma.Blaze.repository.UserMatchRepository;
import com.emma.Blaze.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final MessageService messageService;

    @Autowired
    public ChatWebSocketHandler(MessageService messageService) {
        this.messageService = messageService;
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);


    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Obtener el userId de los parámetros
        String userId = getUserIdFromSession(session);

        if (userId != null) {
            sessions.put(userId, session);
            LOGGER.info("Usuario conectado: " + userId);



            String recipientId = getRecipientIdFromSession(session);
            LOGGER.info("Usuario destino: " + recipientId);

            List<Message> messages = messageService.getMessagesBetween(userId, recipientId);


            for (Message message : messages) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSenderId(userId);
                chatMessage.setRecipientId(recipientId);
                chatMessage.setContent(message.getContent());
                session.sendMessage(new TextMessage(chatMessage.toJson()));

            }
        } else {
            LOGGER.warn("Conexión sin userId detectada, cerrando sesión");
            session.close();
        }
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // Obtener el contenido del mensaje en formato JSON
            String payload = message.getPayload();

            ChatMessage chatMessage = ChatMessage.fromJson(payload);

            System.out.println(chatMessage.toJson());
            messageService.saveMessage(chatMessage);

            WebSocketSession recipientSession = sessions.get(chatMessage.getRecipientId());

            if (recipientSession != null && recipientSession.isOpen()) {
                // Si la sesión del destinatario está abierta, enviar el mensaje
                recipientSession.sendMessage(new TextMessage(chatMessage.toJson()));
                LOGGER.info("Mensaje enviado a " + chatMessage.getRecipientId());
            } else {
                // Si el destinatario no está conectado, registrar una advertencia
                LOGGER.warn("El destinatario no está conectado: " + chatMessage.getRecipientId());
                // Opcionalmente podrías guardar el mensaje para enviarlo cuando se conecte
            }
        } catch (Exception e) {
            // Manejar cualquier excepción durante el procesamiento del mensaje
            LOGGER.error("Error al procesar el mensaje: ", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.remove(userId);
            LOGGER.info("Usuario desconectado: " + userId);
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        // Extraer el userId de los parámetros de la URL
        String query = session.getUri().getQuery(); // Ejemplo: "userId=123"
        if (query != null) {
            // Utilizamos regex para obtener el número después de "userId="
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("userId=")) {
                    return param.split("=")[1]; // Solo devolver el valor numérico
                }
            }
        }
        return null;
    }
    public String getRecipientIdFromSession(WebSocketSession session) {
        String recipientId = null;
        try {
            URI uri = session.getUri();
            String query = uri.getQuery();
            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals("recipientId")) {
                        recipientId = keyValue[1];
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error al obtener recipientId de la URL", e);
        }
        return recipientId;
    }
}
