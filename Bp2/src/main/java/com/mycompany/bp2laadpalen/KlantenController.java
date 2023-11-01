package com.mycompany.bp2laadpalen;

//IMPORTS
import Model.Klant;
import java.io.IOException;
import java.text.ParseException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class KlantenController {
 //FXML Imports
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
 //Switches for the menu
    @FXML
    private TextField txtVoornaam;
    @FXML
    private TextField txtAchternaam;
    @FXML
    private TextField txtTelefoonnummer;
    @FXML
    private Button btnVoegToe;
    @FXML
    private Button btnBewerk;
    @FXML
    private Button btnVerwijder;
    @FXML
    private TableView<Klant> tvKlanten;
    @FXML
    private TableColumn<Klant, String> colVoornaam;
    @FXML
    private TableColumn<Klant, String> colAchternaam;
    @FXML
    private TableColumn<Klant, String> colTelefoonnummer;

    //Function that inserts klanten
    @FXML
    private void insertKlanten() throws ParseException, IOException {
        Alert a = new Alert(Alert.AlertType.NONE);

        Connection con = null;
        int result = 0;
        try {
            Klant k = new Klant(txtTelefoonnummer.getText(), txtVoornaam.getText(), txtAchternaam.getText());

            if (k.getTelefoonnumer().isEmpty() || k.getVoornaam().isEmpty() || k.getAchternaam().isEmpty()) {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Alle velden moeten ingevuld worden!");
                a.show();
                return;
            }
            //Insert Query
            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("insert into klanten values (?,?,?)");

            pstat.setString(1, k.getTelefoonnumer());
            pstat.setString(2, k.getVoornaam());
            pstat.setString(3, k.getAchternaam());

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

     //Initialize function that starts when this class is called or the page is loaded / tableView
    @FXML
    public void initialize() {
        btnBewerk.setDisable(true);
        //Loading data in the tableview
        ObservableList<Klant> List = FXCollections.observableArrayList();
        Connection con = null;
        try {
            con = DBCPDataSource.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from klanten";
            ResultSet result = stat.executeQuery(sql);
            while (result.next()) {
                Klant k = new Klant(result.getString("Telefoonnummer"), result.getString("Voornaam"), result.getString("Achternaam"));

//                  k.setTelefoonnumer(result.getString("Telefoonnummer"));
                System.out.println(k.getTelefoonnumer());
                List.add(k);
            }
            con.close();
        } catch (SQLException se) {

        }
        colTelefoonnummer.setCellValueFactory(new PropertyValueFactory<Klant, String>("telefoonnumer"));
        colVoornaam.setCellValueFactory(new PropertyValueFactory<Klant, String>("voornaam"));
        colAchternaam.setCellValueFactory(new PropertyValueFactory<Klant, String>("achternaam"));
        tvKlanten.setItems(List);
    }

    //Delete selected data
    @FXML
    public void delete() {
        Connection con = null;
        Klant k = tvKlanten.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Verwijderen");
        alert.setContentText("Wilt u " + k.getVoornaam() + k.getAchternaam() + " verwijderen?");
        Optional<ButtonType> resultAlert = alert.showAndWait();

        if (resultAlert.get() == ButtonType.OK) {
            int result = 0;
            try {
                con = DBCPDataSource.getConnection();
                PreparedStatement pstat = con.prepareStatement("delete from klanten where klanten.Telefoonnummer = ?");
                pstat.setString(1, k.getTelefoonnumer());
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
    }

    //Function to show the selected data in the textfields
    @FXML
    public void FillTextField() {
        btnBewerk.setDisable(false);
        Klant k = tvKlanten.getSelectionModel().getSelectedItem();

        txtVoornaam.setText(k.getVoornaam());
        txtAchternaam.setText(k.getAchternaam());
        txtTelefoonnummer.setText(k.getTelefoonnumer());
    }

    //update function
    public void Update() {
        Alert a = new Alert(Alert.AlertType.NONE);
        Connection con = null;
        int result = 0;
        try {
            Klant k = tvKlanten.getSelectionModel().getSelectedItem();
            Klant D = new Klant(txtTelefoonnummer.getText(), txtVoornaam.getText(), txtAchternaam.getText());

            if (k.getTelefoonnumer().isEmpty() || k.getVoornaam().isEmpty() || k.getAchternaam().isEmpty()) {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Alle velden moeten ingevuld worden!");
                a.show();
                return;
            }

            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("update Klanten set Telefoonnummer = ?, Voornaam = ?, Achternaam = ? where Telefoonnummer = ?");
            pstat.setString(1, D.getTelefoonnumer());
            pstat.setString(2, D.getVoornaam());
            pstat.setString(3, D.getAchternaam());
            pstat.setString(4, k.getTelefoonnumer());

            result = pstat.executeUpdate();
            initialize();
            con.close();
            clear();

        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

}
