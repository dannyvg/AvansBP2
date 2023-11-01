package com.mycompany.bp2laadpalen;

//IMPORTS
import Model.Reservering;
import Model.Klant;
import Model.Laadpaal;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class ReserveringsController {
 //FXML Imports
    @FXML
    private ComboBox comboLaadpaal;
    @FXML
    private ComboBox comboKlant;
    @FXML
    private DatePicker datePlanning;
    @FXML
    private ComboBox comboStart;
    @FXML
    private ComboBox comboEind;
    @FXML
    private TableView<Reservering> tvReservering;
    @FXML
    private TableColumn<Reservering, String> colKlant;
    @FXML
    private TableColumn<Reservering, String> colLaadpaal;
    @FXML
    private TableColumn<Reservering, String> colStart;
    @FXML
    private TableColumn<Reservering, String> colEind;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnBewerk;
    @FXML
    private Button btnVoegToe;

    //List with times for in the combobox
    private String[] Tijden = {"06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00", "04:00", "05:00"};

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
        ObservableList<Laadpaal> List = FXCollections.observableArrayList(AL);

        comboLaadpaal.setItems(List);
        comboKlant.getItems().addAll(getKlantenFromDb());
        comboStart.getItems().setAll(Tijden);
        comboEind.getItems().setAll(Tijden);

        tableView();
    }

    //Filling tableview
    @FXML
    public void tableView() {
        //Fill Tableview
        ObservableList<Reservering> reserveringen = FXCollections.observableArrayList();
        Connection con = null;
        try {
            con = DBCPDataSource.getConnection();
            Statement stat = con.createStatement();
            //using inner join to get klanten naam
            String sql = "select * from reserveringen INNER JOIN klanten on reserveringen.Klant = klanten.Telefoonnummer;";
            ResultSet result = stat.executeQuery(sql);
            while (result.next()) {
                Reservering res = new Reservering(result.getString("Voornaam"), result.getString("Laadpaal"), result.getString("StartTijd"), result.getString("EindTijd"));
                res.setReserveringsnummer(result.getInt("Reserveringsnummer"));
                reserveringen.add(res);
            }
            con.close();
        } catch (SQLException se) {

        }
        //Putting data in the cells
        colKlant.setCellValueFactory(new PropertyValueFactory<>("klant"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTijd"));
        colEind.setCellValueFactory(new PropertyValueFactory<>("eindTijd"));
        colLaadpaal.setCellValueFactory(new PropertyValueFactory<>("laadpaal"));
        tvReservering.setItems(reserveringen);
    }

    //Filling comboboxen and textfields with the data of the selected item
    @FXML
    public void FillTextField() {
        comboKlant.setDisable(true);
        Reservering res = tvReservering.getSelectionModel().getSelectedItem();

        comboLaadpaal.getSelectionModel().select(res.getLaadpaal());
        comboKlant.getSelectionModel().select(res.getKlant());
        comboStart.getSelectionModel().select(res.getStartTijd());
        comboEind.getSelectionModel().select(res.getEindTijd());

        datePlanning.setDisable(true);

//        LocalDate datum = res.get().toLocalDate();
//String st = comboStart.getSelectionModel().getSelectedItem().toString();
//
//        String stt = st.substring(st.indexOf(""), st.indexOf(" "));
//        System.out.println(stt);
//        datePlanning.setValue(datum);
        btnBewerk.setDisable(false);
        btnVoegToe.setDisable(true);
    }

    //Function that inserts planning
    @FXML
    private void insertReservering() throws ParseException, IOException {

        Alert a = new Alert(Alert.AlertType.NONE);

        if (datePlanning.getValue() == null || comboLaadpaal.getSelectionModel().isEmpty()
                || comboKlant.getSelectionModel().isEmpty() || comboStart.getSelectionModel().isEmpty() || comboEind.getSelectionModel().isEmpty()) {
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Alle velden moeten ingevuld worden!");
            a.show();
            return;
        }

        LocalDate date = datePlanning.getValue();
        java.sql.Date datum = Date.valueOf(date);
        
        //Trying to parse a localdate to a string to combine the date and the times selected to put it in the database as a DateTime
        LocalDateTime Start = LocalDateTime.parse(datum + "T" + comboStart.getSelectionModel().getSelectedItem().toString());
        LocalDateTime Eind = LocalDateTime.parse(datum + "T" + comboEind.getSelectionModel().getSelectedItem().toString());

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String StartString = Start.format(df);
        String EindString = Eind.format(df);

        //Filling class
        String tel = comboKlant.getSelectionModel().getSelectedItem().toString();

        String telefoon = tel.substring(tel.indexOf("[") + 1, tel.indexOf("]"));

        Reservering re = new Reservering(telefoon, comboLaadpaal.getSelectionModel().getSelectedItem().toString(), StartString, EindString);

        Connection con = null;
        int result = 0;
        try {
            //Insert Query
            con = DBCPDataSource.getConnection();
            PreparedStatement pstat = con.prepareStatement("insert into reserveringen (Klant, StartTijd, EindTijd, Laadpaal) values (?,?,?,?)");
            pstat.setString(1, re.getKlant());
            pstat.setString(2, re.getStartTijd());
            pstat.setString(3, re.getEindTijd());
            pstat.setString(4, re.getLaadpaal());

            result = pstat.executeUpdate();
            tableView();
            clear();
            con.close();

        } catch (SQLException se) {
//            Alert if the entry already exists or something else is wrong
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Deze laadpaal word al gebruikt!");
            a.show();
            System.out.println(se);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Filling combobox with klanten
    public ArrayList<Klant> getKlantenFromDb() {
        ArrayList<Klant> Klanten = new ArrayList();
        Connection con = null;
        try {
            con = DBCPDataSource.getConnection();
            Statement stat = con.createStatement();
            String sql = "select * from klanten";
            ResultSet result = stat.executeQuery(sql);
            while (result.next()) {
                Klant k = new Klant(result.getString("Telefoonnummer"), result.getString("Voornaam"), result.getString("Achternaam"));
                Klanten.add(k);
            }
            con.close();
        } catch (SQLException se) {

        }
        return Klanten;
    }

    //Clearing all fields 
    @FXML
    public void clear() {
        comboKlant.setDisable(false);
        btnBewerk.setDisable(true);
        comboLaadpaal.getSelectionModel().select(null);
        comboKlant.getSelectionModel().select(null);
        comboStart.getSelectionModel().select(null);
        datePlanning.setValue(null);
        comboEind.getSelectionModel().select(null);
        btnVoegToe.setDisable(false);
        datePlanning.setDisable(false);
    }

    //Delete selected data
    @FXML
    public void delete() {
        try {
            Connection con = null;
            Reservering res = tvReservering.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Verwijderen");
            alert.setContentText("Wilt u de reserveing van laadpaal " + res.getLaadpaal() + " voor klant  " + res.getKlant() + " op " + res.getStartTijd() + " verwijderen?");
            Optional<ButtonType> resultAlert = alert.showAndWait();

            if (resultAlert.get() == ButtonType.OK) {
                int result = 0;
                try {
                    con = DBCPDataSource.getConnection();
                    PreparedStatement pstat = con.prepareStatement("delete from reserveringen where reserveringen.Laadpaal = ? and reserveringen.StartTijd = ?");
                    pstat.setString(1, res.getLaadpaal());
                    pstat.setString(2, res.getStartTijd());
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

    @FXML
    public void Update() {
//        Alert a = new Alert(Alert.AlertType.NONE);
//        Connection con = null;
//        int result = 0;
////        try {
//            Reservering res = tvReservering.getSelectionModel().getSelectedItem();
//
//            Reservering we = new Reservering(res.getKlant(), comboLaadpaal.getSelectionModel().getSelectedItem().toString(), comboStart.getSelectionModel().getSelectedItem().toString(), comboEind.getSelectionModel().getSelectedItem().toString());
//            
//            
//            
//            String Start = res.getStartTijd();
//
//            String st = Start.substring(Start.indexOf(" ") , Start.indexOf(""));
//            
//            System.out.println(st);

            
            
//            
//            con = DBCPDataSource.getConnection();
//            PreparedStatement pstat = con.prepareStatement("update reserveringen set StartTijd = ?, EindTijd = ?, Laadpaal =? where Laadpaal = ? and StartTijd = ?");
//            pstat.setString(1, we.getStartTijd());
//            pstat.setString(2, we.getEindTijd());
//            pstat.setString(3, we.getLaadpaal());
//            pstat.setString(4, res.getLaadpaal());
//            pstat.setString(5, res.getStartTijd());
//
//            result = pstat.executeUpdate();
//            tableView();
//            con.close();
//            clear();
//
//        } catch (SQLException se) {
//            a.setAlertType(Alert.AlertType.WARNING);
//            a.setContentText("Deze laadpaal word al gecontroleerd op deze datum!");
//            a.show();
//            System.out.println(se);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            a.setAlertType(Alert.AlertType.WARNING);
//            a.setContentText("Error!");
//            a.show();
//        }
    }
}
