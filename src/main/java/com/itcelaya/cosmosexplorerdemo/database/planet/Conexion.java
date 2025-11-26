package com.itcelaya.cosmosexplorerdemo.database.planet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/exploradorcosmos";
    private static final String USER = "Andres";
    private static final String PASSWORD = "SQLPass123";

    private static Connection conexion;

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Carga el driver manualmente
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión exitosa a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver MySQL no encontrado: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cerrar conexión: " + e.getMessage());
        }
    }
}
