package Model;

import java.sql.Date;

public class Controle extends Werkzaamheden {
    
    private String status;
    
    public Controle(String laadpaal, int medewerker, Date datum, String status){
//        super(laadpaal,medewerker,datum);
        this.laadpaal = laadpaal;
        this.medewerker = medewerker;
        this.datum = datum;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMedewerkerNaam() {
        return medewerkerNaam;
    }

    public void setMedewerkerNaam(String medewerkerNaam) {
        this.medewerkerNaam = medewerkerNaam;
    }

    public String getLaadpaal() {
        return laadpaal;
    }

    public void setLaadpaal(String laadpaal) {
        this.laadpaal = laadpaal;
    }

    public int getMedewerker() {
        return medewerker;
    }

    public void setMedewerker(int medewerker) {
        this.medewerker = medewerker;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

}
