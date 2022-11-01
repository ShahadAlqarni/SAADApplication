package com.hfad.SAADApplication;

public class db_send {

    String S_red;
    String message;
    String police;
    String sender;
    String url;


    public db_send() {
    }

    public db_send(String S_red,  String message, String police, String sender, String url) {
        this.message = message;
        this.sender = sender;
        this.S_red = S_red;
        this.police = police;
        this.url = url;
    }

    public String getS_red() {
        return S_red;
    }

    public String getMessage() {
        return message;
    }

    public String getPolice() {
        return police;
    }

    public String getSender() {
        return sender;
    }

    public String getUrl() {
        return url;
    }
}
