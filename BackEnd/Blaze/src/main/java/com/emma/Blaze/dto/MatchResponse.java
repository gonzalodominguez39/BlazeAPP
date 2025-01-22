package com.emma.Blaze.dto;

public class MatchResponse {
    private String id;
    private String User1Id;
    private String User2Id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser1Id() {
        return User1Id;
    }

    public void setUser1Id(String user1Id) {
        User1Id = user1Id;
    }

    public String getUser2Id() {
        return User2Id;
    }

    public void setUser2Id(String user2Id) {
        User2Id = user2Id;
    }
}
