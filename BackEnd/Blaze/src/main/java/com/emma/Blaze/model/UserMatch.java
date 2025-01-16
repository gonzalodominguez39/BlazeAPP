package com.emma.Blaze.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_matchs")
public class UserMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    public UserMatch(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    private LocalDateTime matchDate = LocalDateTime.now();

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }
// Getters and Setters
}
