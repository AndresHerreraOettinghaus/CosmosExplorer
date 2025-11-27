package com.itcelaya.cosmosexplorerdemo.DAO.spaceX;

import java.sql.*;

import com.itcelaya.cosmosexplorerdemo.database.MySQLConnection;
import com.itcelaya.cosmosexplorerdemo.util.spaceX.FavoriteEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FavoriteImageDAO {

    public static void insertFavorite(FavoriteEntry entry, int userId) throws SQLException {
        String sql = """
            INSERT INTO favorite_images 
            (user_id, display_title, image_url, full_title, date_taken, description, article_url)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setString(2, entry.getDisplayTitle());
            ps.setString(3, entry.getImageUrl());
            ps.setString(4, entry.getFullTitle());
            ps.setString(5, entry.getDate());
            ps.setString(6, entry.getDescription());
            ps.setString(7, entry.getArticleUrl());

            ps.executeUpdate();
        }
    }

    public static ObservableList<FavoriteEntry> getFavoritesByUser(int userId) throws SQLException {
        String sql = "SELECT display_title, image_url, full_title, date_taken, description, article_url FROM favorite_images WHERE user_id = ?";
        ObservableList<FavoriteEntry> list = FXCollections.observableArrayList();

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FavoriteEntry fav = new FavoriteEntry(
                        rs.getString("display_title"),
                        rs.getString("image_url"),
                        rs.getString("full_title"),
                        rs.getString("date_taken"),
                        rs.getString("description"),
                        rs.getString("article_url")
                );
                list.add(fav);
            }
        }

        return list;
    }
}
