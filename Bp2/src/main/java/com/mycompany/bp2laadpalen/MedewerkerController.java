package com.mycompany.bp2laadpalen;

//IMPORTS
import Model.Werknemer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MedewerkerController {
 
     //Switches for the menu
    @FXML
    private void switchToMap() throws IOException {
        App.setRoot("Home");
    }
    
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

    //FXML Imports
    @FXML
    private TextField txtVoornaam;
    @FXML
    private TextField txtAchternaam;
    @FXML
    private TextField txtTelefoonnummer;
    @FXML
    private DatePicker DateGeboorte;
    @FXML
    private Button btnVoegToe;
    @FXML
    private Button btnBewerk;
    @FXML
    private Button btnVerwijder;
    @FXML
    private TableView<Werknemer> tvWerknemers;
    @FXML
    private TableColumn<Werknemer, String> colVoornaam;
    @FXML
    private TableColumn<Werknemer, String> colAchternaam;
    @FXML
    private TableColumn<Werknemer, String> colTelefoonnummer;
    @FXML
    private TableColumn<Werknemer, Date> colGeboorteDatum;

    //Function that inserts Medewerkers
    @FXML
    private void insertWerknemers() throws ParseException, IOException {
        Alert a = new Alert(Alert.AlertType.NONE);

        Connection con = null;
        int result = 0;
        try {
            LocalDate Datum = DateGeboorte.getValue();
            Date date = Date.valueOf(Datum);

            Werknemer m = new Werknemer(txtVoornaam.getText(), txtAchternaam.getText(), txtTelefoonnummer.getText(), date);

            if (m.getTelefoonnummer().isEmpty() || m.getVoornaam().isEmpty() || m.getAchternaam().isEmpty()) {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Alle velden moeten ingevuld worden!");
                a.show();
                return;
            }
            //Insert Query
            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("insert into werknemers(Voornaam, Achternaam, Telefoonnummer, GeboorteDatum) values (?,?,?,?)");

            pstat.setString(1, m.getVoornaam());
            pstat.setString(2, m.getAchternaam());
            pstat.setString(3, m.getTelefoonnummer());
            pstat.setDate(4, m.getGeboorteDatum());

            result = pstat.executeUpdate();
            con.close();
            initialize();
            clear();

        } catch (SQLException se) {
            System.out.println(se.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

     //Initialize function that starts when this class is called or the page is loaded
    @FXML
    public void initialize() {
        btnBewerk.setDisable(true);
        //Loading data in the tableview
        ObservableList<Werknemer> List = FXCollections.observableArrayList();
        Connection con = null;
        try {
            con = DBCPDataSource.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from werknemers";
            ResultSet result = stat.executeQuery(sql);
            while (result.next()) {
                Werknemer w = new Werknemer(result.getString("Voornaam"), result.getString("Achternaam"), result.getString("Telefoonnummer"), result.getDate("GeboorteDatum"));
                List.add(w);
            }
            con.close();
        } catch (SQLException se) {

        }
        colVoornaam.setCellValueFactory(new PropertyValueFactory<Werknemer, String>("voornaam"));
        colAchternaam.setCellValueFactory(new PropertyValueFactory<Werknemer, String>("achternaam"));
        colTelefoonnummer.setCellValueFactory(new PropertyValueFactory<Werknemer, String>("telefoonnummer"));
        colGeboorteDatum.setCellValueFactory(new PropertyValueFactory<Werknemer, Date>("geboorteDatum"));
        tvWerknemers.setItems(List);
    }

    //Delete selected data
    @FXML
    public void delete() {
        Connection con = null;
        Werknemer m = tvWerknemers.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Verwijderen");
        alert.setContentText("Wilt u " + m.getVoornaam() + m.getAchternaam() + " verwijderen?");
        Optional<ButtonType> resultAlert = alert.showAndWait();

        if (resultAlert.get() == ButtonType.OK) {
            int result = 0;
            try {
                con = DBCPDataSource.getConnection();
                PreparedStatement pstat = con.prepareStatement("delete from werknemers where werknemers.Telefoonnummer = ?");
                pstat.setString(1, m.getTelefoonnummer());
                result = pstat.executeUpdate();
                initialize();
                con.close();
                initialize();
                clear();
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }

        }

    }

    //Clear all textfields
    @FXML
    public void clear() {
        btnBewerk.setDisable(true);
        txtVoornaam.clear();
        txtAchternaam.clear();
        txtTelefoonnummer.clear();
        DateGeboorte.setValue(null);
    }

    //Function to show the selected data in the textfields
    @FXML
    public void FillTextField() {
        btnBewerk.setDisable(false);
        Werknemer m = tvWerknemers.getSelectionModel().getSelectedItem();

        txtVoornaam.setText(m.getVoornaam());
        txtAchternaam.setText(m.getAchternaam());
        txtTelefoonnummer.setText(m.getTelefoonnummer());
        LocalDate datum = m.getGeboorteDatum().toLocalDate();
        DateGeboorte.setValue(datum);
    }

    //update function
    public void Update() {
        Alert a = new Alert(Alert.AlertType.NONE);
        Connection con = null;
        int result = 0;
        try {
            Werknemer m = tvWerknemers.getSelectionModel().getSelectedItem();

            LocalDate Datum = DateGeboorte.getValue();
            Date date = Date.valueOf(Datum);

            Werknemer D = new Werknemer(txtVoornaam.getText(), txtAchternaam.getText(), txtTelefoonnummer.getText(), date);

            if (m.getTelefoonnummer().isEmpty() || m.getVoornaam().isEmpty() || m.getAchternaam().isEmpty()) {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Alle velden moeten ingevuld worden!");
                a.show();
                return;
            }

            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("update werknemers set Voornaam = ?, Achternaam = ?, Telefoonnummer = ?, GeboorteDatum = ? where Telefoonnummer = ?");
          
            pstat.setString(1, D.getVoornaam());
            pstat.setString(2, D.getAchternaam());
            pstat.setString(3, D.getTelefoonnummer());
            pstat.setDate(4, D.getGeboorteDatum());
            pstat.setString(5, m.getTelefoonnummer());

            result = pstat.executeUpdate();
            initialize();
            con.close();
            clear();

        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
}
