package com.emma.Blaze.repository;

import com.emma.Blaze.model.Swipe;
import com.emma.Blaze.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    @Query("SELECT COUNT(s) > 0 FROM Swipe s WHERE s.user.id = :userId AND s.swipedUser.id = :swipedUserId AND s.direction = :direction")
    boolean existsByUserIdAndSwipedUserIdAndDirection(@Param("userId") long userId,
                                                      @Param("swipedUserId") long swipedUserId,
                                                      @Param("direction") Swipe.SwipeDirection direction);

    default boolean userSwiped(long userId, long swipedUserId, Swipe.SwipeDirection direction) {
        return existsByUserIdAndSwipedUserIdAndDirection(userId, swipedUserId, direction);
    }
}