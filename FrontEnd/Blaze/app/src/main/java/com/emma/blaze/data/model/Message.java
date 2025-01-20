package com.emma.blaze.data.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
    private String senderId;
    private String recipientId;

    private String content;

    // Constructor
    public Message(String senderId, String receiverId, String messageContent) {
        this.senderId = senderId;
        this.recipientId = receiverId;
        this.content = messageContent;
    }

    // Getters y setters
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return recipientId;
    }

    public void setReceiverId(String receiverId) {
        this.recipientId = receiverId;
    }

    public String getMessage() {
        return content;
    }

    public void setMessage(String message) {
        this.content = message;
    }

    // Método estático para crear un Message desde un JSON (representado como Map)
    public static Message fromJson(Map<String, Object> json) {
        String senderId = (String) json.get("senderId");
        String recipientId = (String) json.get("recipientId");
        String messageContent = (String) json.get("content");

        return new Message(senderId, recipientId, messageContent);
    }

    // Método para convertir el Message a un formato JSON (simulado con un Map)
    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("senderId", this.senderId);
        json.put("recipientId", this.recipientId);
        json.put("content", this.content);
        return json;
    }
}
