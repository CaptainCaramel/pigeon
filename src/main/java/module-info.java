module com.ldal.pigeonapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;
    requires java.mail;

    opens com.ldal.pigeonapp to javafx.fxml;
    exports com.ldal.pigeonapp;
}