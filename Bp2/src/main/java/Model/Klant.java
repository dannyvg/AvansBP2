package Model;

public class Klant {
    private String telefoonnumer, voornaam, achternaam;
    
    public Klant(String telefoonnummer, String voornaam, String achternaam){
        this.telefoonnumer = telefoonnummer;
        this.achternaam = achternaam;
        this.voornaam = voornaam;
    }

    public String getTelefoonnumer() {
        return telefoonnumer;
    }

    public void setTelefoonnumer(String telefoonnumer) {
        this.telefoonnumer = telefoonnumer;
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
    
//    public StringProperty telefoonnummerProperty(){
//        return telefoonnumer;
//    }
    
    @Override
    public String toString(){
        return "[" + telefoonnumer + "]  " + voornaam + " " + achternaam;
    }
    
}
