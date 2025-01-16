package com.emma.Blaze.requestresponse;

public class SwipeResponse {
    private Long swipeId;
    private Long userId;
    private Long swipedUserId;
    private String direction;

    public Long getSwipeId() {
        return swipeId;
    }

    public void setSwipeId(Long swipeId) {
        this.swipeId = swipeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSwipedUserId() {
        return swipedUserId;
    }

    public void setSwipedUserId(Long swipedUserId) {
        this.swipedUserId = swipedUserId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
