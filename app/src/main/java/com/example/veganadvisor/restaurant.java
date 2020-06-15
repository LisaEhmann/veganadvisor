package com.example.veganadvisor;

public class restaurant {

    private String ID;
    private String Name;
    private String Adresse;
    private String opening;
    private String beschreibung;



    public restaurant() {
    }

    public restaurant(String ID, String Name, String Adresse, String Opening, String Beschreibung) {
        this.ID = ID;
        this.Name = Name;
        this.Adresse = Adresse;
        this.opening = Opening;
        this.beschreibung = Beschreibung;

    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getAdresse() {
        return Adresse;
    }

    public String getOpening() {
        return opening;
    }

    public String getBeschreibung() {
        return beschreibung;
    }



    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }



}
