package com.itcelaya.cosmosexplorerdemo.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class PlanetAPIService {

    private static final String API_KEY = "AQUI_TU_API_KEY";

    private static String getAuthHeader() {
        String raw = API_KEY + ":";
        return "Basic " + Base64.getEncoder().encodeToString(raw.getBytes());
    }

    /**
     * Consulta escenas satelitales con un filtro básico (fecha + área aproximada)
     */
    public static JsonArray buscarEscenas(String fecha) {
        try {
            URL url = new URL("https://api.planet.com/data/v1/quick-search");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", getAuthHeader());
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);

            // Filtro simple: productos tipo PSScene4Band y fecha exacta
            String body =
                    "{\n" +
                            "  \"item_types\": [\"PSScene4Band\"],\n" +
                            "  \"filter\": {\n" +
                            "    \"type\": \"AndFilter\",\n" +
                            "    \"config\": [\n" +
                            "      {\n" +
                            "        \"type\": \"DateRangeFilter\",\n" +
                            "        \"field_name\": \"acquired\",\n" +
                            "        \"config\": {\n" +
                            "          \"gte\": \"" + fecha + "T00:00:00.000Z\",\n" +
                            "          \"lte\": \"" + fecha + "T23:59:59.999Z\"\n" +
                            "        }\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  }\n" +
                            "}";

            conn.getOutputStream().write(body.getBytes());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            JsonObject response = new Gson().fromJson(reader, JsonObject.class);

            reader.close();

            return response.getAsJsonArray("features");

        } catch (Exception e) {
            System.out.println("⚠ Error consultando Planet Data API: " + e.getMessage());
            return null;
        }
    }

    /**
     * Descarga thumbnail de una escena
     */
    public static String descargarThumbnail(String assetUrl) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(assetUrl).openConnection();
            conn.setRequestProperty("Authorization", getAuthHeader());

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject json = new Gson().fromJson(reader, JsonObject.class);
            reader.close();

            return json.get("location").getAsString(); // URL directa de la imagen

        } catch (Exception e) {
            System.out.println("⚠ Error descargando thumbnail: " + e.getMessage());
            return null;
        }
    }
}
