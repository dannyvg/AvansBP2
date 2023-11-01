package com.mycompany.bp2laadpalen;

//IMPORTS
import Model.Laadpaal;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONBuilder {

    //EXAMPLE AND TEST
    public ArrayList<Laadpaal> buildHups(String data) {
        ArrayList<Laadpaal> hups = new ArrayList();
        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(data);
            JSONArray jaFeatures = (JSONArray) json.get("features");
            for (Object o : jaFeatures) {
                JSONObject jo = (JSONObject) o;
                JSONObject joAttributes = (JSONObject) jo.get("properties");
                JSONObject joGeometry = (JSONObject) jo.get("geometry");
//                int objectid = Integer.parseInt(joAttributes.get("OBJECTID").toString());
                String coordinates = joGeometry.get("coordinates").toString();

                if (joAttributes.get("LOCATIE") != null && joAttributes.get("LOCATIESTATUS") != null && joAttributes.get("IDENTIFICATIE") != null) {
                    String locatie = joAttributes.get("LOCATIE").toString();
                    String locatieStatus = joAttributes.get("LOCATIESTATUS").toString();
                    String identificatie = joAttributes.get("IDENTIFICATIE").toString();
                    Laadpaal l = new Laadpaal(identificatie, locatie, locatieStatus);
                    hups.add(l);
                }
            }
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
        return hups;
    }

    //Getting all the laadpalen that needs to be installed
    public ArrayList<Laadpaal> buildLaadpalenToBeInstalled(String data) {
        ArrayList<Laadpaal> hups = new ArrayList();
        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(data); 
            JSONArray jaFeatures = (JSONArray) json.get("features");
            for (Object o : jaFeatures) {
                JSONObject jo = (JSONObject) o;
                JSONObject joAttributes = (JSONObject) jo.get("properties");
                JSONObject joGeometry = (JSONObject) jo.get("geometry");
//                int objectid = Integer.parseInt(joAttributes.get("OBJECTID").toString());
                String coordinates = joGeometry.get("coordinates").toString();

                //Checking to see if the laadpalen needs to be installed
                if (joAttributes.get("LOCATIE") != null && "Gepland".equals(joAttributes.get("LOCATIESTATUS").toString()) && joAttributes.get("IDENTIFICATIE") != null) {
                    String locatie = joAttributes.get("LOCATIE").toString();
                    String locatieStatus = joAttributes.get("LOCATIESTATUS").toString();
                    String identificatie = joAttributes.get("IDENTIFICATIE").toString();
                    Laadpaal l = new Laadpaal(identificatie, locatie, locatieStatus);
                    hups.add(l);
                }
            }
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
        return hups;
    }
    
    //Getting all the laadpalen that are already installed and can be used
    public ArrayList<Laadpaal> buildLaadpalenDone(String data) {
        ArrayList<Laadpaal> hups = new ArrayList();
        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(data); 
            JSONArray jaFeatures = (JSONArray) json.get("features");
            for (Object o : jaFeatures) {
                JSONObject jo = (JSONObject) o;
                JSONObject joAttributes = (JSONObject) jo.get("properties");
                JSONObject joGeometry = (JSONObject) jo.get("geometry");
//                int objectid = Integer.parseInt(joAttributes.get("OBJECTID").toString());
                String coordinates = joGeometry.get("coordinates").toString();

                //checking the laadpalen to see if they are already installed
                if (joAttributes.get("LOCATIE") != null && "Gerealiseerd".equals(joAttributes.get("LOCATIESTATUS").toString()) && joAttributes.get("IDENTIFICATIE") != null) {
                    String locatie = joAttributes.get("LOCATIE").toString();
                    String locatieStatus = joAttributes.get("LOCATIESTATUS").toString();
                    String identificatie = joAttributes.get("IDENTIFICATIE").toString();
                    Laadpaal l = new Laadpaal(identificatie, locatie, locatieStatus);
                    hups.add(l);
                }
            }
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
        return hups;
    }

}
