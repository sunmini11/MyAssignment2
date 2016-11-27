package com.egco428.a23285;

public class UserData {
    private String username;
    private String password;
    private double lat;
    private double lon;

    public UserData() {
    }

    public UserData(String username, String password, double lat, double lon) {
        this.username = username;
        this.password = password;
        this.lat = lat;
        this.lon = lon;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

}
