package com.emma.Blaze.model;

import com.emma.Blaze.relationship.UserInterest;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "interests")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    private String name;
    private String url_image;

    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserInterest> userInterests;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInterestId() {
        return interestId;
    }

    public void setInterestId(Long interestId) {
        this.interestId = interestId;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public Set<UserInterest> getUserInterests() {
        return userInterests;
    }

    public void setUserInterests(Set<UserInterest> userInterests) {
        this.userInterests = userInterests;
    }
}
