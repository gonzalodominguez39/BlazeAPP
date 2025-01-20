package com.emma.Blaze.service;

import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.model.Message;
import com.emma.Blaze.model.User;
import com.emma.Blaze.model.UserMatch;
import com.emma.Blaze.repository.MessageRepository;
import com.emma.Blaze.repository.UserMatchRepository;
import com.emma.Blaze.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMatchRepository matchRepository;

    public void saveMessage(ChatMessage message) {

        User sender = userRepository.findById(Long.parseLong(message.getSenderId()))
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        User recipient = userRepository.findById(Long.parseLong(message.getRecipientId()))
                .orElseThrow(() -> new IllegalArgumentException("Destinatario no encontrado"));


        UserMatch userMatch = matchRepository.findByUser1IdAndUser2Id(
                        Long.parseLong(message.getSenderId()), Long.parseLong(message.getRecipientId()))
                .orElseThrow(() -> new IllegalArgumentException("Match no encontrado"));

        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setContent(message.getContent());
        newMessage.setMatch(userMatch);
        newMessage.setRead(false);
        messageRepository.save(newMessage);
    }

    public List<Message> getMessagesBetween(String user1, String user2) {
        List<Message> messagesFromUser1ToUser2 = messageRepository.findBySenderIdAndRecipientId(
                Long.parseLong(user1), Long.parseLong(user2));
        List<Message> messagesFromUser2ToUser1 = messageRepository.findBySenderIdAndRecipientId(
                Long.parseLong(user2), Long.parseLong(user1));


        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(messagesFromUser1ToUser2);
        allMessages.addAll(messagesFromUser2ToUser1);



        return allMessages;
    }
}
