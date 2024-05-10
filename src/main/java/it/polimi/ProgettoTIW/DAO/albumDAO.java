package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        String query = "SELECT * FROM Album WHERE Username = ?  ORDER BY Creation_Date DESC";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, username);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Album album = new Album();
                    album.setUser_id(result.getInt("User_id"));
                    album.setTitle(result.getString("Title"));
                    album.setUsername("Username");
                    album.setCreation_Date(result.getTimestamp("Creation_Date").toLocalDateTime());
                    albums.add(album);
                }
            }
        }
        return albums;
    }
    
    public void createAlbum(Album album) throws SQLException {
        String query = "INSERT INTO `Album` (Title, User_id, Creation_Date, Username) VALUES (?, ?, ?,?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, album.getTitle());
            pstatement.setInt(2, album.getUser_id());
            pstatement.setTimestamp(3, Timestamp.valueOf(album.getCreation_Date()));
            pstatement.setString(4, album.getUsername());
            pstatement.executeUpdate();
        }
    }
    
    //contain_images n-n
    public void AddImagesToAlbum (int images_id, int User_id, String title) throws SQLException
    {
    	String query = "INSERT INTO Contains_Images (Image_Id, title, User_Id ) VALUES (?,?,?)";
    	try(PreparedStatement pstatement = con.prepareStatement(query);)
    	{
    		pstatement.setInt(1, images_id);
    		pstatement.setString(2, title);
    		pstatement.setInt(3, User_id);
    		pstatement.executeUpdate();
    	}
    }

    public void updateAlbum(Album album) throws SQLException {
        String query = "UPDATE `Album` SET title = ? WHERE id = ?";
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