package com.emma.Blaze.request;

import java.util.List;

public class UserRequest {

    private String phoneNumber;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String birthDate;
    private String gender;
    private String genderInterest;
    private String biography;
    private List<String> profilePictures;
    private String relationshipType;
    private String privacySetting;
    private List<String> interests;
    private boolean status;

    // Constructor vacío
    public UserRequest() {}

    // Constructor con todos los campos
    public UserRequest(String phoneNumber, String email, String password, String name, String lastName,
                       String birthDate, String gender, String genderInterest, String biography,
                       List<String> profilePictures, String relationshipType, String privacySetting,
                       List<String> interests, boolean status) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.genderInterest = genderInterest;
        this.biography = biography;
        this.profilePictures = profilePictures;
        this.relationshipType = relationshipType;
        this.privacySetting = privacySetting;
        this.interests = interests;
        this.status = status;
    }

    // Getters y setters
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

    public List<String> getProfilePictures() {
        return profilePictures;
    }

    public void setProfilePictures(List<String> profilePictures) {
        this.profilePictures = profilePictures;
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

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                ", genderInterest='" + genderInterest + '\'' +
                ", biography='" + biography + '\'' +
                ", profilePictures=" + profilePictures +
                ", relationshipType='" + relationshipType + '\'' +
                ", privacySetting='" + privacySetting + '\'' +
                ", interests=" + interests +
                ", status=" + status +
                '}';
    }
}