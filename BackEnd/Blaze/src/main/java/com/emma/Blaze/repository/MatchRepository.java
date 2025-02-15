package com.emma.Blaze.repository;



import com.emma.Blaze.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {


    @Query("SELECT m FROM Match m WHERE (m.user1.id = :userId1 AND m.user2.id = :userId2) OR (m.user1.id = :userId2 AND m.user2.id = :userId1)")
    Optional<Match> findByUser1IdAndUser2Id(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Match m WHERE (m.user1.id = :user1Id AND m.user2.id = :user2Id) OR (m.user1.id = :user2Id AND m.user2.id = :user1Id)")
    boolean checkMatch(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("SELECT m FROM Match m WHERE m.user1.id = :userId OR m.user2.id = :userId")
    List<Match> findAllMatchesByUserId(@Param("userId") Long userId);

}

