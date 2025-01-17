package com.emma.Blaze.repository;



import com.emma.Blaze.model.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {


    @Query("SELECT um FROM UserMatch um WHERE um.user1.id = :userId1 AND um.user2.id = :userId2")
    Optional<UserMatch> findByUser1IdAndUser2Id(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM UserMatch m WHERE (m.user1.id = :user1Id AND m.user2.id = :user2Id) OR (m.user1.id = :user2Id AND m.user2.id = :user1Id)")
    boolean checkMatch(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}

