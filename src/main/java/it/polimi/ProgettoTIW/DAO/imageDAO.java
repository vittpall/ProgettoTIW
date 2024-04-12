package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polimi.ProgettoTIW.beans.Image;
import it.polimi.ProgettoTIW.beans.Album; // will be used stupid eclipse 

public class imageDAO {
    private final Connection con;

    public imageDAO(Connection connection) {
        this.con = connection;
    }

    public List<Image> findImagesByAlbum(int albumId) throws SQLException {
        List<Image> images = new ArrayList<>();
        String query = "SELECT id, title, path FROM images WHERE albumId = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, albumId);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Image image = new Image();
                    image.setImage_Id(result.getInt("id"));
                    image.setTitle(result.getString("title"));
                    image.setSystem_Path(result.getString("path"));
                    images.add(image);
                }
            }
        }
        return images;
    }

    public void addImage(Image image) throws SQLException {
        String query = "INSERT INTO images (title, path, albumId) VALUES (?, ?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, image.getTitle());
            pstatement.setString(2, image.getSystem_Path());
           // right now album doesn't have an id
          //  pstatement.setInt(3, image.getAlbumId());
            pstatement.executeUpdate();
        }
    }

    public void updateImage(Image image) throws SQLException {
        String query = "UPDATE images SET title = ?, path = ? WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, image.getTitle());
            pstatement.setString(2, image.getSystem_Path());
            pstatement.setInt(3, image.getImage_Id());
            pstatement.executeUpdate();
        }
    }

    public void deleteImage(int imageId) throws SQLException {
        String query = "DELETE FROM images WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, imageId);
            pstatement.executeUpdate();
        }
    }
}
