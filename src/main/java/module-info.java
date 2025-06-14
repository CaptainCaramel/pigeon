module com.ldal.pigeonapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    opens com.ldal.pigeonapp to javafx.fxml;
    exports com.ldal.pigeonapp;
}