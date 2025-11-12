package com.itcelaya.cosmosexplorerdemo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinksV5Dto {
    private FlickrV5Dto flickr;
    private String article; // El enlace al artículo
    public FlickrV5Dto getFlickr() { return flickr; }
    public void setFlickr(FlickrV5Dto flickr) { this.flickr = flickr; }
    public String getArticle() { return article; }
    public void setArticle(String article) { this.article = article; }
}