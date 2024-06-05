package com.example.coronaviruse.FindNearbyHospitals;

public class Hospital {
    private String Name ;
    private String vicinity ;
    private boolean Is_open ;
    private double Latitude ;
    private double Longitude ;
    private double Rating ;
    private int place_img;

    public Hospital(String name, String vicinity, boolean is_open, double latitude, double longitude, double rating , int hos_img) {
        Name = name;
        this.vicinity = vicinity;
        Is_open = is_open;
        Latitude = latitude;
        Longitude = longitude;
        Rating = rating;
        this.place_img = hos_img ;
    }

    public int getPlace_img() {
        return place_img;
    }

    public void setPlace_img(int place_img) {
        this.place_img = place_img;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public boolean isIs_open() {
        return Is_open;
    }

    public void setIs_open(boolean is_open) {
        Is_open = is_open;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }
}
