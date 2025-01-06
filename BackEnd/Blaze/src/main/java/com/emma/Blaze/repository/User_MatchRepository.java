package com.emma.Blaze.repository;



import com.emma.Blaze.model.User_Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface User_MatchRepository extends JpaRepository<User_Match, Long> {

    @Query("SELECT um FROM User_Match um WHERE um.user1.userId = :userId1 AND um.user2.userId= :userId2")
    Optional<User_Match> findByUser1IdAndUser2Id(Long userId1, Long userId2);

}

