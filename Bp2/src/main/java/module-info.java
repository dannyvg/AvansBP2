module com.mycompany.bp2laadpalen {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires javafx.web;
    
    requires java.sql;
    requires java.management;
    requires org.apache.commons.pool2;
    requires commons.dbcp2;

    opens com.mycompany.bp2laadpalen to javafx.fxml;
    opens Model to javafx.base;
      
    exports com.mycompany.bp2laadpalen;
}
