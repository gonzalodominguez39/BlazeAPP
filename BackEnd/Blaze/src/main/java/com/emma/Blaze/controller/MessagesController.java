package com.emma.Blaze.controller;

import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {
    @Autowired
    private MessageService messageService;

    @Operation(summary = "Obtener los últimos mensajes de un usuario", description = "Devuelve la lista de los últimos mensajes para un usuario dado su ID")
    @GetMapping("/last-messages/{userId}")
    public ResponseEntity<List<ChatMessage>> getLastMessagesForUser(@PathVariable Long userId) {
        List<ChatMessage> lastMessages = messageService.getLastMessagesForUser(userId);
        return ResponseEntity.ok(lastMessages);
    }

}