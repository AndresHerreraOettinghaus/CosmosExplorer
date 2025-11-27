package com.itcelaya.cosmosexplorerdemo.models.spaceX;

import java.util.Objects;

public class FavoriteEntry {

    private final String displayTitle;
    private final String imageUrl;
    private final String fullTitle;
    private final String date;
    private final String description;
    private final String articleUrl;

    public FavoriteEntry(String displayTitle, String imageUrl, String fullTitle,
                         String date, String description, String articleUrl) {
        this.displayTitle = displayTitle;
        this.imageUrl = imageUrl;
        this.fullTitle = fullTitle;
        this.date = date;
        this.description = description;
        this.articleUrl = articleUrl;
    }

    public String getDisplayTitle() { return displayTitle; }
    public String getImageUrl() { return imageUrl; }
    public String getFullTitle() { return fullTitle; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getArticleUrl() { return articleUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteEntry that)) return false;
        return Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}
