package com.emma.Blaze.service;

import com.emma.Blaze.model.Swipe;
import com.emma.Blaze.model.User;
import com.emma.Blaze.model.UserMatch;
import com.emma.Blaze.repository.SwipeRepository;
import com.emma.Blaze.repository.UserRepository;
import com.emma.Blaze.repository.User_MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MatchService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SwipeRepository swipeRepository;

    @Autowired
    private User_MatchRepository matchRepository;

    @Transactional
    public boolean checkForMatch(long userId, long swipedUserId) {

        boolean userSwipedRight = swipeRepository.userSwiped(userId, swipedUserId, Swipe.SwipeDirection.RIGHT);

        boolean swipedUserSwipedRight = swipeRepository.userSwiped(swipedUserId, userId, Swipe.SwipeDirection.RIGHT);
        System.out.println("user1 "+ userSwipedRight);
        System.out.println("user2"+ swipedUserSwipedRight);

        if (userSwipedRight && swipedUserSwipedRight) {
            if (!matchRepository.checkMatch(userId, swipedUserId)) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                User userSwiped = userRepository.findById(swipedUserId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                UserMatch newMatch = new UserMatch(user, userSwiped);
                matchRepository.save(newMatch);
                return true;
            }else{
                return true;
            }

        }

        return false;
    }
}


