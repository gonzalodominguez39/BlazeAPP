package com.emma.Blaze.relationship;

import java.io.Serializable;
import java.util.Objects;

public class UserInterestId implements Serializable {
    private Long user;
    private Long interest;

    public UserInterestId() {}

    // Getters y setters
    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getInterest() {
        return interest;
    }

    public void setInterest(Long interest) {
        this.interest = interest;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInterestId that = (UserInterestId) o;
        return Objects.equals(user, that.user) && Objects.equals(interest, that.interest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, interest);
    }
}