package com.itcelaya.cosmosexplorerdemo.launcher;

import com.itcelaya.cosmosexplorerdemo.cosmosExplorerApplication;
import com.itcelaya.cosmosexplorerdemo.database.planet.Conexion;
import javafx.application.Application;


public class Launcher {
    public static void main(String[] args) {
        Conexion.getConexion(); // Prueba conexión
        Application.launch(cosmosExplorerApplication.class, args);
    }
}
