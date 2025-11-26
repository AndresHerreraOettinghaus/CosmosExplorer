package com.itcelaya.cosmosexplorerdemo.DTO;

import java.util.List;

public class SolarSystemDto {
    // Esta clase representa la respuesta completa de la API
    private List<Body> bodies;

    public List<Body> getBodies() { return bodies; }

    // Esta clase interna representa cada planeta/cuerpo
    public static class Body {
        private String id;
        private String englishName;
        private String bodyType; // Ej: Planet, Moon
        private Float gravity;

        // Getters
        public String getId() { return id; }
        public String getEnglishName() { return englishName; }
        public String getBodyType() { return bodyType; }
        public Float getGravity() { return gravity; }

        @Override
        public String toString() { return englishName; }
    }
}