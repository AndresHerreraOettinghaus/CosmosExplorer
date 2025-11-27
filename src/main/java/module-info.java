module com.itcelaya.cosmosexplorerdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
    requires javafx.web;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires jdk.jsobject;

    opens com.itcelaya.cosmosexplorerdemo.DTO.iss to com.fasterxml.jackson.databind;
    opens com.itcelaya.cosmosexplorerdemo to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers.Apod to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers.spaceX to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.controllers.solarSystem to javafx.fxml;
    opens com.itcelaya.cosmosexplorerdemo.DTO to com.google.gson, com.fasterxml.jackson.databind;
    exports com.itcelaya.cosmosexplorerdemo;
    exports com.itcelaya.cosmosexplorerdemo.controllers.solarSystem;
    exports com.itcelaya.cosmosexplorerdemo.controllers.iss;
    opens com.itcelaya.cosmosexplorerdemo.controllers.iss to javafx.fxml;
    exports com.itcelaya.cosmosexplorerdemo.DTO;
    exports com.itcelaya.cosmosexplorerdemo.DTO.spaceX;
    opens com.itcelaya.cosmosexplorerdemo.DTO.spaceX to com.fasterxml.jackson.databind, com.google.gson;
}
