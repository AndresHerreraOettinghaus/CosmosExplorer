package com.itcelaya.cosmosexplorerdemo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String HOSTNAME = "localhost";
    private static final String DBNAME = "exploradorCosmos";
    private static final String DBPORT = "3306";
    private static final String DBUSER = "Andres";
    private static final String DBPASS = "SQLPass123";
    private static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + DBPORT + "/" + DBNAME + "?serverTimezone=UTC";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver JDBC de MySQL", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DBUSER, DBPASS);
    }
}