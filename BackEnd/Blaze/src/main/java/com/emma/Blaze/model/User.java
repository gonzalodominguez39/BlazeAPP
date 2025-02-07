package com.emma.Blaze.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType;

    @Enumerated(EnumType.STRING)
    private PrivacySetting privacySetting = PrivacySetting.PUBLIC;

    private LocalDateTime registrationDate = LocalDateTime.now();



    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @JoinTable(
            name = "users_interests", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "interest_id", referencedColumnName = "interestId"))
    private List<Interest> interests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserPicture> pictures;

    @OneToOne(mappedBy = "user")
    private Location location;

    @OneToMany(mappedBy = "user")
    private List<Swipe> swipes;

    @JsonIgnore
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private List<UserMatch> matchesAsUser1;

    @JsonIgnore
    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    private List<UserMatch> matchesAsUser2;

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;



    private boolean status;
    public Long getUserId() {
        return userId;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
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


    public List<UserMatch> getMatchesAsUser1() {
        return matchesAsUser1;
    }

    public void setMatchesAsUser1(List<UserMatch> matchesAsUser1) {
        this.matchesAsUser1 = matchesAsUser1;
    }

    public List<UserMatch> getMatchesAsUser2() {
        return matchesAsUser2;
    }

    public void setMatchesAsUser2(List<UserMatch> matchesAsUser2) {
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

    public List<UserPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<UserPicture> pictures) {
        this.pictures = pictures;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum GenderInterest {
        MALE, FEMALE, ALL, NOT_SPECIFIED
    }

    public enum RelationshipType {
        FRIENDS, CASUAL, FORMAL, OTHER
    }

    public enum PrivacySetting {
        PUBLIC, PRIVATE
    }
}
