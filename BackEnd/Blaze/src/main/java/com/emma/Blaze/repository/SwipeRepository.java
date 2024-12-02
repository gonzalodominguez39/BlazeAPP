package com.emma.Blaze.repository;

import com.emma.Blaze.model.Swipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SwipeRepository extends JpaRepository<Swipe, Long> {


    @Query("SELECT s FROM Swipe s WHERE s.user.userId = :userId")
    List<Swipe> findByUserId(Long userId);


    @Query("SELECT s FROM Swipe s WHERE s.user.userId = :userId AND s.swipedUser.userId = :swipedUserId")
    List<Swipe> findByUserIdAndSwipedUserId(Long userId, Long swipedUserId);


    @Query("SELECT COUNT(s) FROM Swipe s WHERE s.user.userId = :userId AND s.direction = :direction")
    long countByUserIdAndDirection(Long userId, String direction);

}