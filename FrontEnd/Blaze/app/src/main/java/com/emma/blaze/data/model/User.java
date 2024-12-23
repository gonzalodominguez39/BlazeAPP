package com.emma.blaze.data.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User implements Serializable {

    private Long userId;
    private String phoneNumber;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String birthDate;
    private String gender;
    private String genderInterest;
    private String biography;
    private String profilePicture;
    private String relationshipType;
    private String privacySetting;
    private LocalDateTime registrationDate;
    private boolean status;

    // Constructor completo
    public User(Long userId, String phoneNumber, String email, String password, String name, String lastName,
                String birthDate, String gender, String genderInterest, String biography,
                String profilePicture, String relationshipType, String privacySetting,
                LocalDateTime registrationDate, boolean status) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.genderInterest = genderInterest;
        this.biography = biography;
        this.profilePicture = profilePicture;
        this.relationshipType = relationshipType;
        this.privacySetting = privacySetting;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    // Constructor vacío (necesario para Retrofit y otras librerías)
    public User() {}

    // Getters y setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderInterest() {
        return genderInterest;
    }

    public void setGenderInterest(String genderInterest) {
        this.genderInterest = genderInterest;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getPrivacySetting() {
        return privacySetting;
    }

    public void setPrivacySetting(String privacySetting) {
        this.privacySetting = privacySetting;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}