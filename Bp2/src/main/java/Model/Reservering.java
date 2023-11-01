package Model;

//import java.time.LocalDateTime;

public class Reservering {
    private int reserveringsnummer;
    private String klant;
    private String laadpaal;
    private String startTijd, eindTijd;
    
    public Reservering(String klant, String laadpaal, String startTijd, String eindTijd){
        this.klant = klant;
        this.laadpaal = laadpaal;
        this.startTijd = startTijd;
        this.eindTijd = eindTijd;
    }

    public int getReserveringsnummer() {
        return reserveringsnummer;
    }

    public void setReserveringsnummer(int reserveringsnummer) {
        this.reserveringsnummer = reserveringsnummer;
    }

    public String getKlant() {
        return klant;
    }

    public void setKlant(String klant) {
        this.klant = klant;
    }

    public String getLaadpaal() {
        return laadpaal;
    }

    public void setLaadpaal(String laadpaal) {
        this.laadpaal = laadpaal;
    }

    public String getStartTijd() {
        return startTijd;
    }

    public void setStartTijd(String startTijd) {
        this.startTijd = startTijd;
    }

    public String getEindTijd() {
        return eindTijd;
    }

    public void setEindTijd(String eindTijd) {
        this.eindTijd = eindTijd;
    }


    
}
