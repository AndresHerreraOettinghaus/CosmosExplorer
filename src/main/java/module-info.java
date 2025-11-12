module com.itcelaya.cosmosexplorerdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
    requires com.dlsc.formsfx;
    requires org.controlsfx.controls;
    requires java.net.http;

    // --- AÑADE ESTOS PARA JACKSON ---
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens com.itcelaya.cosmosexplorerdemo.DTO to com.fasterxml.jackson.databind;
    opens com.itcelaya.cosmosexplorerdemo to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers to javafx.fxml;
    exports com.itcelaya.cosmosexplorerdemo;
}