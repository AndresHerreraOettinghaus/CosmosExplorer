package com.itcelaya.cosmosexplorerdemo.models.planet;

public class Planet {

    private String date;
    private String title;
    private String explanation;
    private String url;
    private String mediaType;

    public Planet(String date, String title, String explanation, String url, String mediaType) {
        this.date = date;
        this.title = title;
        this.explanation = explanation;
        this.url = url;
        this.mediaType = mediaType;
    }

    public String getDate() { return date; }
    public String getTitle() { return title; }
    public String getExplanation() { return explanation; }
    public String getUrl() { return url; }
    public String getMediaType() { return mediaType; }
}
