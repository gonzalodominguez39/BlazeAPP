package com.emma.Blaze.repository;

import com.emma.Blaze.dto.ChatMessage;
import com.emma.Blaze.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :user1 AND (m.match.user1.id = :user2 OR m.match.user2.id = :user2)) " +
            "OR (m.sender.id = :user2 AND (m.match.user1.id = :user1 OR m.match.user2.id = :user1)) " +
            "ORDER BY m.messageDate ASC")
    List<Message> findBySenderIdAndRecipientId(@Param("user1") Long user1, @Param("user2") Long user2);

    @Query("SELECT COUNT(m) > 0 FROM Message m WHERE (m.sender.id = :user1Id AND m.match.user1.id = :user2Id) OR (m.sender.id = :user2Id AND m.match.user1.id = :user1Id)")
    boolean existsMessagesBetweenUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("SELECT m " +
            "FROM Message m " +
            "WHERE m.messageId IN (" +
            "   SELECT MAX(m2.messageId) " +
            "   FROM Message m2 " +
            "   WHERE m2.match.user1.id = :userId OR m2.match.user2.id = :userId " +
            "   GROUP BY m2.match.id" +
            ")")
    List<Message> findLastMessagesForUser(@Param("userId") Long userId);

}
