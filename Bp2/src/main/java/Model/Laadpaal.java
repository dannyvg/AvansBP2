package Model;

public class Laadpaal {
    private String identificatie, locatie, locatieStatus;
    
    public Laadpaal(String identificatie, String locatie, String locatieStatus){
        this.identificatie = identificatie;
        this.locatie = locatie;
        this.locatieStatus = locatieStatus;
    }

    public String getIdentificatie() {
        return identificatie;
    }

    public void setIdentificatie(String identificatie) {
        this.identificatie = identificatie;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getLocatieStatus() {
        return locatieStatus;
    }

    public void setLocatieStatus(String locatieStatus) {
        this.locatieStatus = locatieStatus;
    }
    
 @Override
    public String toString(){
        return identificatie;
    }
    
}
