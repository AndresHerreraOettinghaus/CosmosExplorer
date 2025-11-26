package com.itcelaya.cosmosexplorerdemo.DAO.SolarSystem;


import com.itcelaya.cosmosexplorerdemo.DTO.SolarSystemDto;
import com.itcelaya.cosmosexplorerdemo.database.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SolarSystemDAO {

    public boolean saveFavorite(SolarSystemDto.Body body) {
        String query = "INSERT INTO favoritos_solar_system (body_id, english_name, planet_type, gravity) VALUES (?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, body.getId());
            stmt.setString(2, body.getEnglishName());
            stmt.setString(3, body.getBodyType());
            // Manejo de nulos por si la gravedad no viene en el JSON
            if (body.getGravity() != null) {
                stmt.setFloat(4, body.getGravity());
            } else {
                stmt.setFloat(4, 0.0f);
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}