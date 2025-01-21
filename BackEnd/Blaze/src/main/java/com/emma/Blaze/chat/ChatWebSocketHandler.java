package com.emma.Blaze.chat;


import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.model.Message;
import com.emma.Blaze.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.net.URI;
import java.util.List;
import java.util.Map;
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
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {

        String userId = getUserIdFromSession(session);

        if (userId != null) {
            sessions.put(userId, session);
            LOGGER.info("Usuario conectado: " + userId);



            String recipientId = getRecipientIdFromSession(session);
            LOGGER.info("Usuario destino: " + recipientId);

            List<Message> messages = messageService.getMessagesBetween(userId, recipientId);


            for (Message message : messages) {

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSenderId(String.valueOf(message.getSender().getUserId()));
                if(userId.equals(String.valueOf(message.getSender().getUserId()))){
                    chatMessage.setRecipientId(recipientId);
                }else {
                    chatMessage.setRecipientId(userId);
                }
                chatMessage.setContent(message.getContent());
                System.out.println(chatMessage.toJson());
                session.sendMessage(new TextMessage(chatMessage.toJson()));

            }
        } else {
            LOGGER.warn("Conexión sin userId detectada, cerrando sesión");
            session.close();
        }
    }


    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        try {
            String payload = message.getPayload();
            ChatMessage chatMessage = ChatMessage.fromJson(payload);
            messageService.saveMessage(chatMessage);

            WebSocketSession recipientSession = sessions.get(chatMessage.getRecipientId());
            if (recipientSession != null && recipientSession.isOpen()) {
                recipientSession.sendMessage(new TextMessage(chatMessage.toJson()));
                LOGGER.info("Mensaje enviado a " + chatMessage.getRecipientId());
            } else {
                LOGGER.warn("El destinatario no está conectado: " + chatMessage.getRecipientId());
            }
        } catch (Exception e) {
            LOGGER.error("Error al procesar el mensaje: ", e);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,@NonNull  CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.remove(userId);
            LOGGER.info("Usuario desconectado: " + userId);
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("userId=")) {
                    return param.split("=")[1];
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
