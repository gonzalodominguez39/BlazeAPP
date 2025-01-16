package com.emma.blaze.data.response;
public class SwipeResponse {
    private long swipeId;
    private long userId;
    private long swipedUserId;
    private String direction;



    public long getSwipeId() {
        return swipeId;
    }

    public void setSwipeId(long swipeId) {
        this.swipeId = swipeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSwipedUserId() {
        return swipedUserId;
    }

    public void setSwipedUserId(long swipedUserId) {
        this.swipedUserId = swipedUserId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
