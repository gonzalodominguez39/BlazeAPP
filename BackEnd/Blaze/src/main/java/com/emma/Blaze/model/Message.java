package com.emma.Blaze.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long messageId;
    @ManyToOne
    @JoinColumn(name = "match_id")
    @JsonIgnore
    private UserMatch match;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Lob
    private String content;

    private LocalDateTime messageDate = LocalDateTime.now();

    private boolean isRead = false;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Long getMessageId() {
        return messageId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(LocalDateTime messageDate) {
        this.messageDate = messageDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public UserMatch getMatch() {
        return match;
    }

    public void setMatch(UserMatch match) {
        this.match = match;
    }

}
