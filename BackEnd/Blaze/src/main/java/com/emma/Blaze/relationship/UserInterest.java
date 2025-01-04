package com.emma.Blaze.relationship;

import com.emma.Blaze.model.Interest;
import com.emma.Blaze.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "users_interests")
public class UserInterest {

    @EmbeddedId
    private UserInterestId id;

    @ManyToOne
    @MapsId("userId") // Vincula userId del Embeddable con esta relación
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("interestId") // Vincula interestId del Embeddable con esta relación
    @JoinColumn(name = "interest_id")
    private Interest interest;

    public UserInterest() {}

    public UserInterest(User user, Interest interest) {
        this.user = user;
        this.interest = interest;
        this.id = new UserInterestId(user.getUserId(), interest.getInterestId());
    }

    // Getters y setters
    public UserInterestId getId() {
        return id;
    }

    public void setId(UserInterestId id) {
        this.id = id;
    }

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