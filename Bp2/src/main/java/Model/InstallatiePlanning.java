package Model;

import java.sql.Date;

public class InstallatiePlanning extends Werkzaamheden {

    private String medewerkerNaam;

    public InstallatiePlanning(String laadpaal, int medewerker, Date datum) {
//        super(laadpaal,medewerker,datum);
        this.laadpaal = laadpaal;
        this.medewerker = medewerker;
        this.datum = datum;
    }

    public String getMedewerkerNaam() {
        return medewerkerNaam;
    }

    public void setMedewerkerNaam(String medewerkerNaam) {
        this.medewerkerNaam = medewerkerNaam;
    }

    public int getMedewerker() {
        return medewerker;
    }

    public void setMedewerker(int medewerker) {
        this.medewerker = medewerker;
    }

    public String getLaadpaal() {
        return laadpaal;
    }

    public void setLaadpaal(String laadpaal) {
        this.laadpaal = laadpaal;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

}
