package com.itcelaya.cosmosexplorerdemo.DTO;

import java.util.List;

public class SolarSystemDto {

    private List<Body> bodies;

    public List<Body> getBodies() { return bodies; }


    public static class Body {
        private String id;
        private String englishName;
        private String bodyType;
        private Float gravity;
        private Float meanRadius;
        private Float density;
        private String discoveredBy;
        private String discoveryDate;


        public String getId() { return id; }
        public String getEnglishName() { return englishName; }
        public String getBodyType() { return bodyType; }
        public Float getGravity() { return gravity; }


        public Float getMeanRadius() { return meanRadius; }
        public Float getDensity() { return density; }
        public String getDiscoveredBy() { return discoveredBy; }
        public String getDiscoveryDate() { return discoveryDate; }

        @Override
        public String toString() { return englishName; }
    }
}