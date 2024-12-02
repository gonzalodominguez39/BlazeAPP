package com.emma.Blaze.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Swipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long swipeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "swiped_user_id")
    private User swipedUser;

    @Enumerated(EnumType.STRING)
    private SwipeDirection direction;

    private LocalDateTime swipeDate = LocalDateTime.now();

    public Long getSwipeId() {
        return swipeId;
    }

    public void setSwipeId(Long swipeId) {
        this.swipeId = swipeId;
    }

    public enum SwipeDirection {
        RIGHT, LEFT
    }
}
