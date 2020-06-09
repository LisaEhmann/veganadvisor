package com.example.veganadvisor;

public class Bewertung {

    public int RestaurantID;
    public int MahlzeitID;
    public String text;
    public int smiley;

    public Bewertung(){}

    public Bewertung(int RestaurantID, int MahlzeitID, String text, int smiley){
        this.RestaurantID = RestaurantID;
        this.MahlzeitID = MahlzeitID;
        this.text = text;
        this.smiley = smiley;
    }


}
