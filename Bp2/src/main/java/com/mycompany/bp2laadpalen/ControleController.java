package com.mycompany.bp2laadpalen;

//IMPORTS
import Model.Controle;
import Model.Laadpaal;
import Model.Werknemer;
import Model.Werkzaamheden;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControleController {
    //FXML Imports
    @FXML
    private ComboBox comboLaadpaal;
    @FXML
    private ComboBox comboWerknemers;
    @FXML
    private DatePicker datePlanning;
    @FXML
    private ComboBox comboStatus;
    @FXML
    private TableView<Werkzaamheden> tvControle;
    @FXML
    private TableColumn<Werkzaamheden, String> colId;
    @FXML
    private TableColumn<Werkzaamheden, String> colNaam;
    @FXML
    private TableColumn<Werkzaamheden, String> colLaadpaal;
    @FXML
    private TableColumn<Werkzaamheden, Date> colDatum;
    @FXML
    private TableColumn<Werkzaamheden, String> colStatus;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnBewerk;
    @FXML
    private Button btnVoegToe;

    //Statussen for status combobox
    private String[] Statussen = {"Goed", "Onderzoek nodig", "Bezig"};

     //Switches for the menu
    @FXML
    private void switchToMap() throws IOException {
        App.setRoot("Medewerker");
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

    //Initialize function that starts when this class is called or the page is loaded
    @FXML
    public void initialize() {
        btnBewerk.setDisable(true);
        //COMBOBOX JSON DATA
        JSONHelper JS = new JSONHelper();
        ArrayList AL = JS.getAllLaadpalenDone();
        //Getting the laadpalen array
        ObservableList<Laadpaal> List = FXCollections.observableArrayList(AL);

        comboLaadpaal.setItems(List);
        comboWerknemers.getItems().addAll(getWerknemersFromDb());
        comboStatus.getItems().setAll(Statussen);

        tableView();
    }

    //Function for the tableview
    @FXML
    public void tableView() {
        //Fill Tableview
        ObservableList<Werkzaamheden> controleList = FXCollections.observableArrayList();
        Connection con = null;
        try {
            con = DBCPDataSource.getConnection();
            Statement stat = con.createStatement();
            //Using inner join to get the werknemers Name
            String sql = "select * from controles INNER JOIN werknemers on controles.Werknemer = werknemers.WerknemerId";
            ResultSet result = stat.executeQuery(sql);
            while (result.next()) {
                Werkzaamheden co = new Controle(result.getString("Laadpaal"), result.getInt("Werknemer"), result.getDate("Datum"), result.getString("Status"));
                co.setMedewerkerNaam(result.getString("Voornaam"));
                controleList.add(co);
            }
            con.close();
        } catch (SQLException se) {

        }
        //Putting data in the cells
        colId.setCellValueFactory(new PropertyValueFactory<>("medewerker"));
        colNaam.setCellValueFactory(new PropertyValueFactory<>("medewerkerNaam"));
        colLaadpaal.setCellValueFactory(new PropertyValueFactory<>("laadpaal"));
        colDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tvControle.setItems(controleList);
    }

    //Filling comboboxen and textfields with the data of the selected item
    @FXML
    public void FillTextField() {
        comboWerknemers.setDisable(true);
        Werkzaamheden w = tvControle.getSelectionModel().getSelectedItem();

        comboLaadpaal.getSelectionModel().select(w.getLaadpaal());
        comboWerknemers.getSelectionModel().select(w.getMedewerkerNaam());
        comboStatus.getSelectionModel().select(w.getStatus());

        LocalDate datum = w.getDatum().toLocalDate();
        datePlanning.setValue(datum);
        btnBewerk.setDisable(false);
        btnVoegToe.setDisable(true);
    }

    //Function that inserts Controles
    @FXML
    private void insertControle() throws ParseException, IOException {

        Alert a = new Alert(Alert.AlertType.NONE);

        if (datePlanning.getValue() == null || comboLaadpaal.getSelectionModel().isEmpty()
                || comboWerknemers.getSelectionModel().isEmpty() || comboStatus.getSelectionModel().isEmpty()) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Alle velden moeten ingevuld worden!");
            a.show();
            return;
        }

        LocalDate date = datePlanning.getValue();
        Date datum = Date.valueOf(date);

        //Filling class
        String id = comboWerknemers.getSelectionModel().getSelectedItem().toString();

        //Getting the telefoonnummer from the string
        int start = id.indexOf("[");
        int end = id.indexOf("]");

        //Making an int of that telefoonnummer so we can use it in the query
        int werknemerId = Integer.parseInt(id.substring(start + 1, end));
        
        Werkzaamheden co = new Controle(comboLaadpaal.getSelectionModel().getSelectedItem().toString(), werknemerId, datum, comboStatus.getSelectionModel().getSelectedItem().toString());

        Connection con = null;
        int result = 0;
        try {
            //Insert Query
            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("insert into controles values (?,?,?,?)");
            pstat.setInt(1, co.getMedewerker());
            pstat.setString(2, co.getLaadpaal());
            pstat.setDate(3, co.getDatum());
            pstat.setString(4, co.getStatus());

            result = pstat.executeUpdate();
            tableView();
            clear();
            con.close();

        } catch (SQLException se) {
//            Alert if the entry already exists or something else is wrong
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Deze laadpaal word al gecontroleerd op deze datum!!");
            a.show();
            System.out.println(se);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Getting all werknemers
    public ArrayList<Werknemer> getWerknemersFromDb() {
        ArrayList<Werknemer> Werknemers = new ArrayList();
        Connection con = null;
        try {
            con = DBCPDataSource.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from werknemers";
            ResultSet result = stat.executeQuery(sql);
            while (result.next()) {
                Werknemer w = new Werknemer(result.getString("Voornaam"), result.getString("Achternaam"), result.getString("Telefoonnummer"), result.getDate("GeboorteDatum"));
                w.setWerknemerId(result.getInt("werknemerId"));
                Werknemers.add(w);
            }
            con.close();
        } catch (SQLException se) {

        }
        return Werknemers;
    }

    //Clearing all fields
    @FXML
    public void clear() {
        comboWerknemers.setDisable(false);
        btnBewerk.setDisable(true);
        comboLaadpaal.getSelectionModel().select(null);
        comboWerknemers.getSelectionModel().select(null);
        datePlanning.setValue(null);
        comboStatus.getSelectionModel().select(null);
        btnVoegToe.setDisable(false);
    }

    //Delete selected data
    @FXML
    public void delete() {
        try {
            Connection con = null;
            Werkzaamheden w = tvControle.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Verwijderen");
            alert.setContentText("Wilt u de controle van laadpaal " + w.getLaadpaal() + " Door medewerker  " + w.getMedewerker() + " op " + w.getDatum() + " verwijderen?");
            Optional<ButtonType> resultAlert = alert.showAndWait();

            if (resultAlert.get() == ButtonType.OK) {
                int result = 0;
                try {
                    con = DBCPDataSource.getConnection();
                    PreparedStatement pstat = con.prepareStatement("delete from controles where controles.laadpaal = ? and controles.datum = ?");
                    pstat.setString(1, w.getLaadpaal());
                    pstat.setDate(2, w.getDatum());
                    result = pstat.executeUpdate();
                    tableView();
                    con.close();
                    clear();
                } catch (SQLException se) {
                    System.out.println(se.getMessage());
                }

            }

        } catch (Exception E) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Niks geselecteerd");
            alert.show();
            System.out.println(E);
        }
    }

    //Function that updates
    @FXML
    public void Update() {
        Alert a = new Alert(Alert.AlertType.NONE);
        Connection con = null;
        int result = 0;
        try {
            Werkzaamheden i = tvControle.getSelectionModel().getSelectedItem();

            LocalDate Datum = datePlanning.getValue();
            Date date = Date.valueOf(Datum);

            Werkzaamheden we = new Controle(comboLaadpaal.getSelectionModel().getSelectedItem().toString(), i.getMedewerker(), date, comboStatus.getSelectionModel().getSelectedItem().toString());

            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("update controles set Laadpaal = ?, Datum = ?, Status =? where Laadpaal = ? and Datum = ?");
            pstat.setString(1, we.getLaadpaal());
            pstat.setDate(2, we.getDatum());
            pstat.setString(3, we.getStatus());
            pstat.setString(4, i.getLaadpaal());
            pstat.setDate(5, i.getDatum());

            result = pstat.executeUpdate();
            tableView();
            con.close();
            clear();

        } catch (SQLException se) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Deze laadpaal word al gecontroleerd op deze datum!");
            a.show();
            System.out.println(se);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Error!");
            a.show();
        }
    }

}
