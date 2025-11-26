package com.itcelaya.cosmosexplorerdemo.services;

import com.itcelaya.cosmosexplorerdemo.database.planet.Conexion;
import com.itcelaya.cosmosexplorerdemo.models.planet.Planet;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ApodService {

    // -------------------- GUARDAR APOD EN BASE DE DATOS --------------------
    public boolean guardarApod(Planet apod) {
        String sql = "INSERT INTO apod (fecha, titulo, explicacion, tipo_medio, url, version, fecha_guardado) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, apod.getDate());        // date
            stmt.setString(2, apod.getTitle());       // title
            stmt.setString(3, apod.getExplanation()); // explanation
            stmt.setString(4, apod.getMediaType());   // mediaType
            stmt.setString(5, apod.getUrl());         // url
            stmt.setString(6, "v1");                  // version por defecto

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error guardando APOD: " + e.getMessage());
            return false;
        }
    }

    // -------------------- DESCARGAR IMAGEN --------------------
    public boolean descargarImagen(String urlImagen, String rutaDestino) {
        try {
            URL url = new URL(urlImagen);
            BufferedInputStream input = new BufferedInputStream(url.openStream());
            FileOutputStream output = new FileOutputStream(rutaDestino);

            byte[] buffer = new byte[1024];
            int bytes;
            while ((bytes = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytes);
            }

            input.close();
            output.close();
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error descargando imagen: " + e.getMessage());
            return false;
        }
    }
}
