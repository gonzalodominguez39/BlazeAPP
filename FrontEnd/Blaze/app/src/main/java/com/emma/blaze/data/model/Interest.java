package com.emma.blaze.data.model;

import com.google.gson.annotations.SerializedName;

public class Interest {

    @SerializedName("interestId")
    private Long interestId;

    @SerializedName("name")
    private String name;

    @SerializedName("url_image")
    private String urlImage;


    public Interest(Long interestId, String name, String urlImage) {
        this.interestId = interestId;
        this.name = name;
        this.urlImage = urlImage;
    }

    public Long getInterestId() {
        return interestId;
    }

    public void setInterestId(Long interestId) {
        this.interestId = interestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }


}