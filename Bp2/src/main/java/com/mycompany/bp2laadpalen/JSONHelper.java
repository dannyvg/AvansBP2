package com.mycompany.bp2laadpalen;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import Model.Laadpaal;

public class JSONHelper {
    private StringBuilder stringBuilder = new StringBuilder();
    private JSONBuilder jsonBuilder = new JSONBuilder();

    public String getJSONData() {
        try {
            URL url
                    = new URL("https://services7.arcgis.com/21GdwfcLrnTpiju8/"
                            + "arcgis/rest/services/Oplaadpunten/"
                            + "FeatureServer/0/"
                            + "query?outFields=*&where=1%3D1&f=geojson");
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responsecode = connection.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("RersponseCode: " + responsecode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    stringBuilder.append(scanner.nextLine());
                }
                scanner.close();
            }
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return stringBuilder.toString();       

    }
    
    public ArrayList<Laadpaal> getAlHups(){
        ArrayList<Laadpaal> hups = new ArrayList();
        hups = jsonBuilder.buildHups(getJSONData());
        return hups;
    }
    
    public ArrayList<Laadpaal> getAllLaadpalenToBeInstalled(){
        ArrayList<Laadpaal> hups = new ArrayList();
        hups = jsonBuilder.buildLaadpalenToBeInstalled(getJSONData());
        return hups;
    }
    
    public ArrayList<Laadpaal> getAllLaadpalenDone(){
        ArrayList<Laadpaal> hups = new ArrayList();
        hups = jsonBuilder.buildLaadpalenDone(getJSONData());
        return hups;
    }

}
