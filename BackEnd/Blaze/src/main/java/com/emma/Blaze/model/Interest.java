package com.emma.Blaze.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "interests")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    private String name;
    private String url_image;

    @ManyToMany(mappedBy = "interests")
    @JsonIgnore
    private List<User> users;

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
        this.interestId= interestId;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
