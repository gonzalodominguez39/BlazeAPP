package com.emma.Blaze.controller;

import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.model.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat/{roomId}")
    @SendTo("Topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message) {
        return new ChatMessage(message.getMessage(), message.getSenderId());
    }
}
