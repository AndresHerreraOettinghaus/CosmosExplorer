module com.itcelaya.cosmosexplorerdemo {
    // Módulos base de JavaFX + HTTP + SQL
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.sql;

    // UI Libraries
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;

    // JSON / Serialization
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.google.gson;

    // Reflection permissions
    opens com.itcelaya.cosmosexplorerdemo to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers.Apod to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers.spaceX to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers.solarSystem to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.DTO to com.google.gson, com.fasterxml.jackson.databind;

    // Exports (solo lo necesario)
    exports com.itcelaya.cosmosexplorerdemo;
    exports com.itcelaya.cosmosexplorerdemo.controllers.solarSystem;
    exports com.itcelaya.cosmosexplorerdemo.DTO;
}
