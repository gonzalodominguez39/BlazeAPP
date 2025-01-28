package com.emma.blaze.data.model;

public class UserMatch {
    private String id;
    private String user1Id;
    private String user2Id;

    public String getId() {
        return id;
    }

    public UserMatch() {
    }
    public UserMatch(String id, String user1Id, String user2Id) {
        this.id = id;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(String user1Id) {
        this.user1Id = user1Id;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(String user2Id) {
        this.user2Id = user2Id;
    }

    @Override
    public String toString() {
        return "UserMatch{" +
                "id='" + id + '\'' +
                ", user1Id='" + user1Id + '\'' +
                ", user2Id='" + user2Id + '\'' +
                '}';
    }
}
