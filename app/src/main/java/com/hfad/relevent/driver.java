package com.hfad.relevent;


import android.widget.Button;

public class driver {

    String Location;
    Button Phone;
    Button Done;
    String massege;
    String id;

    public driver(String Location, Button Phone, Button Done){
        this.Location=Location;
        this.Phone=Phone;
        this.Done=Done;
    }

    public driver(String location, String massege, String id) {
        this.Location = location;
        this.massege = massege;
        this.id = id;
    }

    public String getLocation() {
        return Location;
    }

    public Button getPhone() {
        return Phone;
    }

    public Button getDone() {
        return Done;
    }

    public String getMassege() {
        return massege;
    }

    public String getId() {
        return id;
    }
}
