package com.itcelaya.cosmosexplorerdemo.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itcelaya.cosmosexplorerdemo.database.planet.Conexion;
import com.itcelaya.cosmosexplorerdemo.models.planet.Planet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlanetService {

    private static final String NASA_API_KEY = "anVVkgLkx5VBL8IOhwNUzuISdr54R1qehai5P7zU";

    // =======================
    //   OBTENER APOD DEL DÍA
    // =======================
    public static Planet obtenerApod() {
        try {
            String endpoint = "https://api.nasa.gov/planetary/apod?api_key=" + NASA_API_KEY;
            HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();

                return parsearApod(data);
            }
        } catch (Exception e) {
            System.out.println("❌ Error al obtener APOD: " + e.getMessage());
        }
        return null;
    }

    // ===============================
    //   OBTENER APOD POR FECHA
    // ===============================
    public static Planet obtenerApodPorFecha(String fecha) {
        try {
            String endpoint = "https://api.nasa.gov/planetary/apod?api_key=" + NASA_API_KEY + "&date=" + fecha;
            HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();

                return parsearApod(data);
            }
        } catch (Exception e) {
            System.out.println("❌ Error al obtener APOD por fecha: " + e.getMessage());
        }
        return null;
    }

    private static Planet parsearApod(JsonObject data) {
        String date = data.get("date").getAsString();
        String title = data.get("title").getAsString();
        String explanation = data.get("explanation").getAsString();
        String url = data.get("url").getAsString();
        String mediaType = data.get("media_type").getAsString();

        return new Planet(date, title, explanation, url, mediaType);
    }

    // ===============================
    //   GUARDAR APOD EN BASE DE DATOS
    // ===============================
    public static boolean guardarApodEnBD(Planet apod) {
        String query = """
            INSERT INTO apod (fecha, titulo, explicacion, tipo_medio, url, version, fecha_guardado)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, apod.getDate());
            stmt.setString(2, apod.getTitle());
            stmt.setString(3, apod.getExplanation());
            stmt.setString(4, apod.getMediaType());
            stmt.setString(5, apod.getUrl());
            stmt.setString(6, "v1");
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error al guardar APOD: " + e.getMessage());
        }
        return false;
    }

    // ===============================
    //     DESCARGAR IMAGEN APOD
    // ===============================
    public static String descargarImagen(Planet apod) {
        try {
            // Obtener ruta del escritorio
            String escritorio = System.getProperty("user.home") + "/Desktop/";

            // Carpeta destino
            String folder = escritorio + "ExploradorCosmos/APOD/";

            File dir = new File(folder);
            if (!dir.exists()) {
                boolean creada = dir.mkdirs();
                System.out.println("📁 Carpeta creada: " + creada + " → " + folder);
            } else {
                System.out.println("📁 Carpeta existente → " + folder);
            }

            // Nombre del archivo
            String filename = "apod_" + apod.getDate().replace("-", "_") + ".jpg";
            String fullPath = folder + filename;

            // Descargar
            URL url = new URL(apod.getUrl());
            InputStream in = url.openStream();
            FileOutputStream out = new FileOutputStream(fullPath);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            in.close();
            out.close();

            System.out.println("✅ Imagen guardada en: " + fullPath);
            return fullPath;

        } catch (Exception e) {
            System.out.println("❌ Error descargando imagen: " + e.getMessage());
        }
        return null;
    }

    // ===============================
    //   OBTENER APODS GUARDADOS
    // ===============================
    public static List<Planet> obtenerApodsGuardados() {
        List<Planet> lista = new ArrayList<>();

        String query = "SELECT fecha, titulo, explicacion, url, tipo_medio FROM apod ORDER BY fecha DESC";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Planet apod = new Planet(
                        rs.getString("fecha"),
                        rs.getString("titulo"),
                        rs.getString("explicacion"),
                        rs.getString("url"),
                        rs.getString("tipo_medio")
                );
                lista.add(apod);
            }

        } catch (Exception e) {
            System.out.println("❌ Error al obtener APODs guardados: " + e.getMessage());
        }

        return lista;
    }
}
