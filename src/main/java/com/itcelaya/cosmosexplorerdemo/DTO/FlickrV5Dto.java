package com.itcelaya.cosmosexplorerdemo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlickrV5Dto {
    private List<String> original;
    public List<String> getOriginal() { return original; }
    public void setOriginal(List<String> original) { this.original = original; }
}