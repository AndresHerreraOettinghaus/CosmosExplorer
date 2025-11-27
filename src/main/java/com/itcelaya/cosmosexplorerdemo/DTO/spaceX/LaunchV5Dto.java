package com.itcelaya.cosmosexplorerdemo.DTO.spaceX;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LaunchV5Dto {
    private LinksV5Dto links;
    private String name;        // El título
    private String details;     // La descripción

    @JsonProperty("date_utc") // El JSON usa "date_utc"
    private String dateUtc;     // La fecha

    public LinksV5Dto getLinks() { return links; }
    public void setLinks(LinksV5Dto links) { this.links = links; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getDateUtc() { return dateUtc; }
    public void setDateUtc(String dateUtc) { this.dateUtc = dateUtc; }
}