package com.emma.blaze.data.model;

import com.google.gson.annotations.SerializedName;

public class UserInterest {

    @SerializedName("userId")
    private Long userId;

    @SerializedName("interestId")
    private Long interestId;

    public UserInterest(Long userId, Long interestId) {
        this.userId = userId;
        this.interestId = interestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInterestId() {
        return interestId;
    }

    public void setInterestId(Long interestId) {
        this.interestId = interestId;
    }

}