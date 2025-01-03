package com.emma.Blaze.model;


import com.emma.Blaze.relationship.UserInterest;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, length = 15)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private String password;
    private String name;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private GenderInterest genderInterest;

    @Lob
    private String biography;

    private List<String> userPictures;

    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType;

    @Enumerated(EnumType.STRING)
    private PrivacySetting privacySetting = PrivacySetting.PUBLIC;

    private LocalDateTime registrationDate = LocalDateTime.now();

    private boolean status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInterest> interests;

    @OneToOne(mappedBy = "user")
    private Location location;

    @OneToMany(mappedBy = "user")
    private List<Swipe> swipes;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User_Match> matchesAsUser1;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User_Match> matchesAsUser2;

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    public Long getUserId() {
        return userId;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


    public List<Swipe> getSwipes() {
        return swipes;
    }

    public void setSwipes(List<Swipe> swipes) {
        this.swipes = swipes;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<UserInterest> getInterests() {
        return interests;
    }

    public void setInterests(List<UserInterest> interests) {
        this.interests = interests;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public GenderInterest getGenderInterest() {
        return genderInterest;
    }

    public void setGenderInterest(GenderInterest genderInterest) {
        this.genderInterest = genderInterest;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<String> getUserPictures() {
        return userPictures;
    }

    public void setUserPictures(List<String> userPictures) {
        this.userPictures = userPictures;
    }

    public List<User_Match> getMatchesAsUser1() {
        return matchesAsUser1;
    }

    public void setMatchesAsUser1(List<User_Match> matchesAsUser1) {
        this.matchesAsUser1 = matchesAsUser1;
    }

    public List<User_Match> getMatchesAsUser2() {
        return matchesAsUser2;
    }

    public void setMatchesAsUser2(List<User_Match> matchesAsUser2) {
        this.matchesAsUser2 = matchesAsUser2;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public PrivacySetting getPrivacySetting() {
        return privacySetting;
    }

    public void setPrivacySetting(PrivacySetting privacySetting) {
        this.privacySetting = privacySetting;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum GenderInterest {
        MALE, FEMALE, ALL
    }

    public enum RelationshipType {
        FRIENDS, CASUAL, FORMAL, OTHER
    }

    public enum PrivacySetting {
        PUBLIC, PRIVATE
    }
}
