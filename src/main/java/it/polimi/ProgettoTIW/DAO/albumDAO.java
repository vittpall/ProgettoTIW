package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoTIW.beans.album;

public class albumDAO {
    private final Connection con;

    public albumDAO(Connection connection) {
        this.con = connection;
    }

    public List<Album> findAlbumsByUser(String username) throws SQLException {
        List<album> albums = new ArrayList<>();
        String query = "SELECT id, title FROM albums WHERE username = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, username);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    album album = new album();
                    album.setUser_id(result.getInt("id"));
                    album.setTitle(result.getString("title"));
                    albums.add(album);
                }
            }
        }
        return albums;
    }
    
    public void createAlbum(Album album) throws SQLException {
        String query = "INSERT INTO albums (title, userId) VALUES (?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, album.getTitle());
            pstatement.setInt(2, album.getCreatorId());
            pstatement.executeUpdate();
        }
    }

    public void updateAlbum(Album album) throws SQLException {
        String query = "UPDATE albums SET title = ? WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, album.getTitle());
            pstatement.setInt(2, album.getId());
            pstatement.executeUpdate();
        }
    }

    public void deleteAlbum(int albumId) throws SQLException {
        String query = "DELETE FROM albums WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, albumId);
            pstatement.executeUpdate();
        }
    }
}