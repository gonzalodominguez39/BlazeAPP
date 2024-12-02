package com.emma.Blaze.repository;

import com.emma.Blaze.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {



    @Query("SELECT m FROM Message m WHERE m.match.matchId = :matchId")
    List<Message> findByMatchId(Long matchId);
    @Query("SELECT m FROM Message m WHERE m.match.matchId = :matchId ORDER BY m.messageDate ASC")
    List<Message> findByMatchIdOrderByMessageDateAsc(Long matchId);


    @Query("SELECT m FROM Message m WHERE m.sender.userId = :senderId AND m.isRead = :isRead")
    List<Message> findBySenderIdAndIsRead(Long senderId, Boolean isRead);


    @Query("DELETE FROM Message m WHERE m.match.matchId = :matchId")
    void deleteByMatchId(Long matchId);
}
