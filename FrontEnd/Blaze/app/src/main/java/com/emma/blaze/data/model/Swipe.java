package com.emma.blaze.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Swipe implements Serializable {

    @SerializedName("swipeId")
    private long swipeId;

    @SerializedName("userId")
    private long userId;

    @SerializedName("swipedUserId")
    private long swipedUserId;

    @SerializedName("direction")
    private String direction;

    public Swipe() {}


    public Swipe(Long swipeId, Long userId, Long swipedUserId, String direction) {
        this.swipeId = swipeId;
        this.userId = userId;
        this.swipedUserId = swipedUserId;
        this.direction = direction;
    }

    // Getters y Setters
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
