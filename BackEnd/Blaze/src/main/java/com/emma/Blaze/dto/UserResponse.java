package com.emma.Blaze.dto;
import java.util.List;

public class UserResponse {

    private Long userId;
    private String phoneNumber;
    private String email;
    private String name;
    private String biography;
    private String gender;
    private String genderInterest;
    private List<String> interests;
    private LocationRequest location=new LocationRequest();
    private String relationshipType;
    private String privacySetting;
    private String registrationDate;
    private boolean status;
    private List<String> pictureUrls;

    public UserResponse() {}

    public LocationRequest getLocation() {
        return location;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void setLocation(LocationRequest location) {
        this.location = location;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
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

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "userId=" + userId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                ", gender='" + gender + '\'' +
                ", genderInterest='" + genderInterest + '\'' +
                ", relationshipType='" + relationshipType + '\'' +
                ", privacySetting='" + privacySetting + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", status=" + status +
                ", pictureUrls=" + pictureUrls +
                '}';
    }
}
