package com.emma.Blaze.repository;



import com.emma.Blaze.model.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface User_MatchRepository extends JpaRepository<UserMatch, Long> {

    @Query("SELECT um FROM UserMatch um WHERE um.user1.userId = :userId1 AND um.user2.userId= :userId2")
    Optional<UserMatch> findByUser1IdAndUser2Id(Long userId1, Long userId2);

}

