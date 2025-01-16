package com.emma.blaze.helpers;

import com.emma.blaze.data.model.Swipe;

public class UserFunctions {


    public static Swipe CrateSwipe(long userId, Long swipedUserId, String direction){
        Swipe swipe = new Swipe();
        swipe.setUserId(userId);
        swipe.setSwipedUserId(swipedUserId);
        swipe.setDirection(direction);
        return swipe;
    }
}
