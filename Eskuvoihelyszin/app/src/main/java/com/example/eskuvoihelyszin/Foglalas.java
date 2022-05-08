package com.example.eskuvoihelyszin;

public class Foglalas {
    private String id;
    private String venueId;
    private String userDisplayName;
    private String userEmail;
    private String date;
    private String phoneNumber;

    public Foglalas(String venueId, String userDisplayName, String userEmail, String date, String phoneNumber) {
        this.venueId = venueId;
        this.userDisplayName = userDisplayName;
        this.userEmail = userEmail;
        this.date = date;
        this.phoneNumber = phoneNumber;
    }

    public Foglalas(){}

    public String _getId(){ return id; }

    public String getVenueId() {
        return venueId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getDate() {
        return date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void _setId(String id){
        this.id = id;
    }
}
