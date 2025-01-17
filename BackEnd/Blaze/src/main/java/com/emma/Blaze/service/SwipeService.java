package com.emma.Blaze.service;

import com.emma.Blaze.model.Swipe;
import com.emma.Blaze.model.User;
import com.emma.Blaze.repository.SwipeRepository;
import com.emma.Blaze.repository.UserRepository;
import com.emma.Blaze.dto.SwipeRequest;
import com.emma.Blaze.dto.SwipeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwipeService {

    @Autowired
    private  SwipeRepository swipeRepository;
    @Autowired
    private  UserRepository userRepository;

    public SwipeService(SwipeRepository swipeRepository) {

    }

    public SwipeResponse createSwipe(SwipeRequest swipeDto) {
        User user = userRepository.findById(swipeDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        User swipedUser = userRepository.findById(swipeDto.getSwipedUserId())
                .orElseThrow(() -> new IllegalArgumentException("Swiped user not found"));

        boolean existSwipe = false;
        String direction = swipeDto.getDirection().toUpperCase();
        System.out.println(direction);
        if (direction.equals("RIGHT")) {
            existSwipe = swipeRepository.userSwiped(swipeDto.getUserId(), swipeDto.getSwipedUserId(), Swipe.SwipeDirection.RIGHT);
        } else if (direction.equals("LEFT")) {
            existSwipe = swipeRepository.userSwiped(swipeDto.getUserId(), swipeDto.getSwipedUserId(), Swipe.SwipeDirection.LEFT);
        } else {
            throw new IllegalArgumentException("Invalid swipe direction: " + swipeDto.getDirection());
        }

        if (!existSwipe) {
            Swipe swipe = new Swipe();
            swipe.setUser(user);
            swipe.setSwipedUser(swipedUser);
            swipe.setDirection(Swipe.SwipeDirection.valueOf(direction));
            Swipe swipeCreated = swipeRepository.save(swipe);
            SwipeResponse swipeResponse = new SwipeResponse();
            swipeResponse.setSwipeId(swipeCreated.getSwipeId());
            swipeResponse.setSwipedUserId(swipeCreated.getSwipedUser().getUserId());
            swipeResponse.setDirection(swipeCreated.getDirection().toString());
            swipeResponse.setUserId(swipeCreated.getUser().getUserId());

            return swipeResponse;
        }

        return null;
    }
}
