package com.emma.Blaze.controller;

import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {
    @Autowired
    private MessageService messageService;
/*
    @GetMapping("/exist/{user1Id}/{user2Id}")
    public ResponseEntity<Boolean> existsMessagesBetweenUsers(@PathVariable long user1Id, @PathVariable long user2Id) {
        boolean exists = messageService.existsMessagesBetweenUsers(user1Id, user2Id);
        return ResponseEntity.ok(exists);
    }
*/
    @GetMapping("/last/{user1Id}/{user2Id}")
    public ResponseEntity<ChatMessage> findLastMessageBetweenUsers(@PathVariable long user1Id, @PathVariable long user2Id) {
        ChatMessage lastMessage = messageService.findLastMessageBetweenUsers(user1Id, user2Id);
        if (lastMessage != null) {
            return ResponseEntity.ok(lastMessage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}