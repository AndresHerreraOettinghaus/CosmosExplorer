package com.itcelaya.cosmosexplorerdemo.util.spaceX;

import java.util.Objects;

/**
 * Esta clase guarda los datos de un favorito.
 * Almacena la URL, un título corto (para mostrar) y todos los
 * datos completos para restaurar la vista.
 */
public class FavoriteEntry {

    // --- Nuevos campos ---
    private final String displayTitle;  // El título corto (2 palabras)
    private final String imageUrl;
    private final String fullTitle;     // El título completo
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

    // --- Getters para todos los campos ---
    public String getDisplayTitle() { return displayTitle; }
    public String getImageUrl() { return imageUrl; }
    public String getFullTitle() { return fullTitle; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getArticleUrl() { return articleUrl; }

    // El 'equals' sigue basándose solo en la URL, lo cual es correcto
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteEntry that = (FavoriteEntry) o;
        return imageUrl.equals(that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}