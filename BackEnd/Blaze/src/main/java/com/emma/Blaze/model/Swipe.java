package com.emma.Blaze.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "swipes")
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSwipedUser() {
        return swipedUser;
    }

    public void setSwipedUser(User swipedUser) {
        this.swipedUser = swipedUser;
    }

    public SwipeDirection getDirection() {
        return direction;
    }

    public void setDirection(SwipeDirection direction) {
        this.direction = direction;
    }

    public LocalDateTime getSwipeDate() {
        return swipeDate;
    }

    public void setSwipeDate(LocalDateTime swipeDate) {
        this.swipeDate = swipeDate;
    }
}
