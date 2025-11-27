package com.itcelaya.cosmosexplorerdemo.DTO.iss;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssPositionDto {
    private String message;
    private long timestamp;
    private IssPosition iss_position;

    public static class IssPosition {
        private String latitude;
        private String longitude;

        public double getLatitude() {
            try { return Double.parseDouble(latitude); } catch (Exception e) { return 0.0; }
        }
        public double getLongitude() {
            try { return Double.parseDouble(longitude); } catch (Exception e) { return 0.0; }
        }

        // Getters y Setters originales para Jackson
        public void setLatitude(String latitude) { this.latitude = latitude; }
        public void setLongitude(String longitude) { this.longitude = longitude; }
    }

    // Getters y Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public IssPosition getIss_position() { return iss_position; }
    public void setIss_position(IssPosition iss_position) { this.iss_position = iss_position; }
}