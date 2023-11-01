package com.mycompany.bp2laadpalen;

//IMPORTS
import Model.Laadpaal;
import Model.Werknemer;
import Model.InstallatiePlanning;
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

public class InstallatiePlanningController {
 //FXML Imports
    @FXML
    private ComboBox comboLaadpaal;
    @FXML
    private ComboBox comboWerknemers;
    @FXML
    private DatePicker datePlanning;
    @FXML
    private TableView<Werkzaamheden> tvInstallatiePlanning;
    @FXML
    private TableColumn<Werkzaamheden, String> colId;
    @FXML
    private TableColumn<Werkzaamheden, String> colNaam;
    @FXML
    private TableColumn<Werkzaamheden, String> colLaadpaal;
    @FXML
    private TableColumn<Werkzaamheden, Date> colDatum;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnBewerk;

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
        ArrayList AL = JS.getAllLaadpalenToBeInstalled();
         //Getting the laadpalen array
        ObservableList<Laadpaal> List = FXCollections.observableArrayList(AL);

        comboLaadpaal.setItems(List);
        comboWerknemers.getItems().addAll(getWerknemersFromDb());

        tableView();
    }

    //Function for the tableview
    @FXML
    public void tableView() {
        //Fill Tableview
        ObservableList<Werkzaamheden> installatiePlanningList = FXCollections.observableArrayList();
        Connection con = null;
        try {
            con = DBCPDataSource.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from installatieplanningen INNER JOIN werknemers on installatieplanningen.Werknemer = werknemers.WerknemerId;";
            ResultSet result = stat.executeQuery(sql);
            while (result.next()) {
                Werkzaamheden i = new InstallatiePlanning(result.getString("Laadpaal"), result.getInt("Werknemer"), result.getDate("Datum"));
                i.setMedewerkerNaam(result.getString("Voornaam"));
                installatiePlanningList.add(i);
            }
            con.close();
        } catch (SQLException se) {

        }
         //Putting data in the cells
        colId.setCellValueFactory(new PropertyValueFactory<>("medewerker"));
        colNaam.setCellValueFactory(new PropertyValueFactory<>("medewerkerNaam"));
        colLaadpaal.setCellValueFactory(new PropertyValueFactory<>("laadpaal"));
        colDatum.setCellValueFactory(new PropertyValueFactory<>("datum"));
        tvInstallatiePlanning.setItems(installatiePlanningList);
    }

    //Filling comboboxen and textfields with the data of the selected item
    @FXML
    public void FillTextField() {
        comboWerknemers.setDisable(true);
//        btnBewerk.setDisable(false);
        Werkzaamheden w = tvInstallatiePlanning.getSelectionModel().getSelectedItem();

//        txtVoornaam.setText(m.getVoornaam());
        comboLaadpaal.getSelectionModel().select(w.getLaadpaal());
        comboWerknemers.getSelectionModel().select(w.getMedewerkerNaam());
//        txtAchternaam.setText(m.getAchternaam());
//        txtTelefoonnummer.setText(m.getTelefoonnummer());

        LocalDate datum = w.getDatum().toLocalDate();
        datePlanning.setValue(datum);
        btnBewerk.setDisable(false);
    }

    //Function that inserts planning
    @FXML
    private void insertInstallatiePlanning() throws ParseException, IOException {

        Alert a = new Alert(Alert.AlertType.NONE);

        if (datePlanning.getValue() == null || comboLaadpaal.getSelectionModel().isEmpty()
                || comboWerknemers.getSelectionModel().isEmpty()) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Alle velden moeten ingevuld worden!");
            a.show();
            return;
        }
        //Filling class
        LocalDate date = datePlanning.getValue();
        Date datum = Date.valueOf(date);

        String id = comboWerknemers.getSelectionModel().getSelectedItem().toString();

        int start = id.indexOf("[");
        int end = id.indexOf("]");

        int werknemerId = Integer.parseInt(id.substring(start + 1, end));

        Werkzaamheden we = new InstallatiePlanning(comboLaadpaal.getSelectionModel().getSelectedItem().toString(), werknemerId, datum);

        Connection con = null;
        int result = 0;
        try {
            //Insert Query
            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("insert into installatieplanningen values (?,?,?)");
            pstat.setInt(1, we.getMedewerker());
            pstat.setString(2, we.getLaadpaal());
            pstat.setDate(3, we.getDatum());

            result = pstat.executeUpdate();
            tableView();
            clear();
            con.close();

        } catch (SQLException se) {
//            Alert if the entry already exists or something else is wrong
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Deze Laadpaal is al toegevoegd!");
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
    }

    //Delete selected data
    @FXML
    public void delete() {
        try {
            Connection con = null;
            Werkzaamheden w = tvInstallatiePlanning.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Verwijderen");
            alert.setContentText("Wilt u de installatie van laadpaal " + w.getLaadpaal() + " Door medewerker  " + w.getMedewerker() + " op " + w.getDatum() + " verwijderen?");
            Optional<ButtonType> resultAlert = alert.showAndWait();

            if (resultAlert.get() == ButtonType.OK) {
                int result = 0;
                try {
                    con = DBCPDataSource.getConnection();
                    PreparedStatement pstat = con.prepareStatement("delete from installatieplanningen where installatieplanningen.laadpaal = ?");
                    pstat.setString(1, w.getLaadpaal());
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
    public void Update() {
        Alert a = new Alert(Alert.AlertType.NONE);
        Connection con = null;
        int result = 0;
        try {
            Werkzaamheden i = tvInstallatiePlanning.getSelectionModel().getSelectedItem();

            //parsing localdate to date
            LocalDate Datum = datePlanning.getValue();
            Date date = Date.valueOf(Datum);
//
//            String id = comboWerknemers.getSelectionModel().getSelectedItem().toString();

//            int start = id.indexOf("[");
//            int end = id.indexOf("]");
//            System.out.println("CHECK3333");
//            int werknemerId = Integer.parseInt(id.substring(start + 1, end));
//            System.out.println(werknemerId);
//            System.out.println("CHECK4444");
            Werkzaamheden we = new InstallatiePlanning(comboLaadpaal.getSelectionModel().getSelectedItem().toString(), i.getMedewerker(), date);

            if (datePlanning.getValue() == null || comboLaadpaal.getSelectionModel().isEmpty()
                    ) {
//                || comboWerknemers.getSelectionModel().isEmpty()
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("Alle velden moeten ingevuld worden!");
                a.show();
                return;
            }

            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("update installatieplanningen set Laadpaal = ?, Datum = ? where Laadpaal = ?");
//            pstat.setInt(1, we.getMedewerker());
            pstat.setString(1, we.getLaadpaal());
            pstat.setDate(2, we.getDatum());
            pstat.setString(3, i.getLaadpaal());

            result = pstat.executeUpdate();
            tableView();
            con.close();
            clear();

        } catch (SQLException se) {
            System.out.println(se.getMessage());
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Error!");
            a.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Error!");
            a.show();
        }
    }

}
