package com.example.veganadvisor;

public class rating {
    private String rID;
    private String uID;
    private String text;
    private float value;

    public void setrID(String rID) {
        this.rID = rID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getrID() {
        return rID;
    }

    public String getuID() {
        return uID;
    }

    public String getText() {
        return text;
    }

    public float getValue() {
        return value;
    }

    public rating() {
    }

    public rating(String rID, String uID, String text, float value) {
        this.rID = rID;
        this.uID = uID;
        this.text = text;
        this.value = value;
    }
}
