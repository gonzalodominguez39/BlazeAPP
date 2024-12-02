package com.emma.Blaze.relationship;

import com.emma.Blaze.model.Interest;
import com.emma.Blaze.model.User;
import jakarta.persistence.*;

@Entity
@IdClass(UserInterestId.class)
public class UserInterest {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "interest_id", nullable = false)
    private Interest interest;

    // Getters y setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }
}
