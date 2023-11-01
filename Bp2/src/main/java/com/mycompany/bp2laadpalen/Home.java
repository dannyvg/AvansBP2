package com.mycompany.bp2laadpalen;

//IMPORTS
import java.io.IOException;
import javafx.fxml.FXML;
import Model.Laadpaal;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Home {
 //FXML Imports
    @FXML
    private TableView<Laadpaal> tvLaadpalen;
    @FXML
    private TableColumn<Laadpaal, String> colLaadpalen;
    @FXML
    private WebView webView;
    @FXML
    private WebEngine engine;

    //Switches for the menu
    @FXML
    private void switchToMedewerker() throws IOException {
        App.setRoot("Medewerker");
    }
    
    @FXML
    private void switchToReserveringen() throws IOException {
        App.setRoot("Reservering");
    }
    
    @FXML
    private void switchToControle() throws IOException {
        App.setRoot("Controle");
    }

    @FXML
    private void switchToKlanten() throws IOException {
        App.setRoot("Klant");
    }

    @FXML
    private void switchToInstallatiePlanning() throws IOException {
        App.setRoot("InstallatiePlanning");
    }

    //Initialize function that starts when this class is called or the page is loaded
    public void initialize() {
        try {
            //Fill tableview with JSON Data
            JSONBuilder json = new JSONBuilder();
            JSONHelper JS = new JSONHelper();
            
            ArrayList AL = JS.getAllLaadpalenDone();
            
            //Getting all laadpalen from the json file from the web
            ObservableList<Laadpaal> List = FXCollections.observableArrayList(AL);
            
            colLaadpalen.setCellValueFactory(new PropertyValueFactory<>("identificatie"));
            tvLaadpalen.setItems(List);
            
            //WebView
            engine = webView.getEngine();
            URL link = new File("src/main/resources/Map.html").toURI().toURL();
//        engine.load("https://www.openstreetmap.org/#map=14/51.5878/4.7716l");
engine.load(link.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
