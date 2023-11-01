package Model;

import java.sql.Date;


public abstract class Werkzaamheden {
    protected String medewerkerNaam;
    protected String laadpaal;
    protected int medewerker;
    protected Date datum;
    protected String Status;
    
    public Werkzaamheden(){
//        this.laadpaal = laadpaal;
//        this.medewerker = medewerker;
//        this.datum = datum;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
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
