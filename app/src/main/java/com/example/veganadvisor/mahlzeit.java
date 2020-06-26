package com.example.veganadvisor;

public class mahlzeit {

    public int ID;
    public String Name;
    public double Preis;
    public int BewertungID;

    public mahlzeit() {
    }

    public mahlzeit(int ID, String Name, double Preis, int BewertungID) {
        this.ID = ID;
        this.Name = Name;
        this.Preis = Preis;
        this.BewertungID = BewertungID;
    }
}
