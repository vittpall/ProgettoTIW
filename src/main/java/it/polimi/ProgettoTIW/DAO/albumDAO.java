package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoTIW.beans.Album;


public class albumDAO {
    private final Connection con;

    public albumDAO(Connection connection) {
        this.con = connection;
    }

    public List<Album> findAlbumsByUser(String username) throws SQLException {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT id, title, username FROM albums WHERE username = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, username);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Album album = new Album();
                    album.setUser_id(result.getInt("id"));
                    album.setTitle(result.getString("title"));
                    album.setUsername(username);
                    albums.add(album);
                }
            }
        }
        return albums;
    }
    
    public void createAlbum(Album album) throws SQLException {
        String query = "INSERT INTO albums (title, userId, creationDate) VALUES (?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, album.getTitle());
            pstatement.setInt(2, album.getUser_id());
            pstatement.setDate(3, (Date) album.getCreation_Date());
            pstatement.executeUpdate();
        }
    }
    
    //contain_images n-n
    public void AddImagesToAlbum (int images_id, int User_id, String title) throws SQLException
    {
    	String query = "INSERT INTO Contains_Images (userId, title, imageId) VALUES (?,?,?)";
    	try(PreparedStatement pstatement = con.prepareStatement(query);)
    	{
    		pstatement.setInt(1, images_id);
    		pstatement.setString(2, title);
    		pstatement.setInt(3, User_id);
    	}
    }

    public void updateAlbum(Album album) throws SQLException {
        String query = "UPDATE albums SET title = ? WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, album.getTitle());
            pstatement.setInt(2, album.getUser_id());
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