package com.emma.Blaze.service;

import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.model.Message;
import com.emma.Blaze.model.User;
import com.emma.Blaze.model.Match;
import com.emma.Blaze.repository.MessageRepository;
import com.emma.Blaze.repository.MatchRepository;
import com.emma.Blaze.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatchRepository matchRepository;

    public void saveMessage(ChatMessage message) {

        User sender = userRepository.findById(Long.parseLong(message.getSenderId()))
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        User recipient = userRepository.findById(Long.parseLong(message.getRecipientId()))
                .orElseThrow(() -> new IllegalArgumentException("Destinatario no encontrado"));


        Match match = matchRepository.findByUser1IdAndUser2Id(
                        Long.parseLong(message.getSenderId()), Long.parseLong(message.getRecipientId()))
                .orElseThrow(() -> new IllegalArgumentException("Match no encontrado"));

        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setContent(message.getContent());
        newMessage.setMatch(match);
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



        return removeDuplicatesAndSortByDate(allMessages);
    }

    private static List<Message> removeDuplicatesAndSortByDate(List<Message> messages) {
        Set<Long> seenIds = new HashSet<>();
        return messages.stream()
                .filter(message -> seenIds.add(message.getMessageId()))
                .sorted(Comparator.comparing(Message::getMessageDate))
                .collect(Collectors.toList());
    }




    public List<ChatMessage> getLastMessagesForUser(Long userId) {
        List<Message> messages = messageRepository.findLastMessagesForUser(userId);
        for (Message message: messages){
            System.out.println(message.toString());
        }
        return messages.stream()
                .map(message -> new ChatMessage(
                        String.valueOf(message.getSender().getUserId()),
                        String.valueOf(getRecipientId(message, userId)),
                        message.getContent()
                ))
                .toList();
    }

    private Long getRecipientId(Message message, Long userId) {
        if (message.getSender().getUserId().equals(message.getMatch().getUser1().getUserId())) {
            return message.getMatch().getUser2().getUserId();
        } else {
            return  message.getMatch().getUser1().getUserId();
        }
    }
}
