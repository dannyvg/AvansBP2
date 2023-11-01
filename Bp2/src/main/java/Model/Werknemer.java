package Model;

import java.sql.Date;

public class Werknemer {
    private int werknemerId;
    private String voornaam, achternaam, telefoonnummer;
    private Date geboorteDatum;
    
    public Werknemer( String voornaam, String achternaam, String telefoonnummer, Date geboorteDatum){
//        this.werknemerId = werknemerId;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.telefoonnummer = telefoonnummer;
        this.geboorteDatum = geboorteDatum;
    }

    public int getWerknemerId() {
        return werknemerId;
    }

    public void setWerknemerId(int werknemerId) {
        this.werknemerId = werknemerId;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public void setTelefoonnummer(String telefoonnummer) {
        this.telefoonnummer = telefoonnummer;
    }

    public Date getGeboorteDatum() {
        return geboorteDatum;
    }

    public void setGeboorteDatum(Date geboorteDatum) {
        this.geboorteDatum = geboorteDatum;
    }
    
    @Override
    public String toString(){
        return "[" + werknemerId + "] " + voornaam + " "+achternaam + "  |   " + telefoonnummer;
    }

}
