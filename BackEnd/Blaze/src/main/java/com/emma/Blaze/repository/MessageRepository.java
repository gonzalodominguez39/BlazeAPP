package com.emma.Blaze.repository;

import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.sender.id = :user1 AND m.match.user1.id = :user2 OR m.match.user2.id = :user2")
    List<Message> findBySenderIdAndRecipientId(@Param("user1") Long user1, @Param("user2") Long user2);

}
