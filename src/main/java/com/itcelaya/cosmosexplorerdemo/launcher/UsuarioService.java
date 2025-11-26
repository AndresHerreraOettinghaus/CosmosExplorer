package com.itcelaya.cosmosexplorerdemo.launcher;

import com.itcelaya.cosmosexplorerdemo.database.planet.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioService {

    // -------------------- LOGIN --------------------
    public boolean validarLogin(String username, String password) {

        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.out.println("❌ Error al validar login: " + e.getMessage());
            return false;
        }
    }

    // -------------------- REGISTRO --------------------
    public boolean registrarUsuario(String nombre, String username, String password) {

        String sql = "INSERT INTO usuarios (nombre_usuario, username, password, rol, fecha_registro) " +
                "VALUES (?, ?, ?, 'usuario', NOW())";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, username);
            stmt.setString(3, password);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
}
