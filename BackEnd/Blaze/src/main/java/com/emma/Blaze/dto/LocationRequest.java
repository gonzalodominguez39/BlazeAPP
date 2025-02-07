package com.emma.Blaze.dto;

public class LocationRequest {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public LocationRequest() {

    }

    public LocationRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
