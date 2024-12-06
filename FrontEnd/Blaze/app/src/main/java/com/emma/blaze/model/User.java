package com.emma.blaze.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

    private Long userId;
    private String phoneNumber;
    private String email;
    private String password;
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private GenderInterest genderInterest;
    private String biography;
    private String profilePicture;
    private RelationshipType relationshipType;
    private PrivacySetting privacySetting;
    private LocalDateTime registrationDate;
    private boolean status;

    // Constructor vacío
    public User(Long id,String name, String email,String profilePicture) {
        this.userId = id;
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    // Constructor con todos los campos necesarios
    public User(Long userId, String phoneNumber, String email, String password, String name, LocalDate birthDate,
                Gender gender, GenderInterest genderInterest, String biography, String profilePicture,
                RelationshipType relationshipType, PrivacySetting privacySetting, LocalDateTime registrationDate,
                boolean status) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.name = name;
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

    // Getters y Setters
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Enumeraciones
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
