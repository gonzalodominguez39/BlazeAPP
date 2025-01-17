package com.emma.Blaze.dto;


public class ChatMessage {
    private String senderId;
    private String message;
    private String receiverId;

    public ChatMessage(String message, String senderId) {
        this.senderId= senderId;
        this.message=message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
