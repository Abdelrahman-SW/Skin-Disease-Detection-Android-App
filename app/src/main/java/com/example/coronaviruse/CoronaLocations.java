package com.example.coronaviruse;

public class CoronaLocations {
    private Double Long ;
    private Double Lat ;

    public CoronaLocations(double aLong, double lat) {
        Long = aLong;
        Lat = lat;
    }

    public CoronaLocations() {
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }
}
