package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import it.polimi.ProgettoTIW.beans.Image;
import it.polimi.ProgettoTIW.beans.User;


public class imageDAO {
    private final Connection con;

    public imageDAO(Connection connection) {
        this.con = connection;
    }

    public List<Image> findImagesByAlbum(String albumTitle, int Offset) throws SQLException {
        List<Image> images = new ArrayList<>();
        String query = "SELECT id, title, path, creation_date, description FROM images as i, contains_images as c WHERE i.id = c.imagesId AND c.albumTitle = ? LIMIT 6 OFFSET ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, albumTitle);
            pstatement.setInt(2, Offset);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Image image = new Image();
                    image.setImage_Id(result.getInt("id"));
                    image.setTitle(result.getString("title"));
                    image.setSystem_Path(result.getString("path"));
                    image.setCreation_Date(result.getDate("creation_date"));          
                    image.setDescription(result.getString("description"));
                    images.add(image);
                }
            }
        }
        return images;
    }

    public void addImage(Image image) throws SQLException {
        String query = "INSERT INTO images (title, description, systemPath, creationDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, image.getTitle());
            pstatement.setString(2, image.getDescription());
            pstatement.setString(3, image.getSystem_Path()); 
            pstatement.setDate(4, (java.sql.Date)image.getCreation_Date());
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

	public int RetrieveNextImageId() throws SQLException {
		int images_id = 0;
		String query = "SELECT id FROM images ORDER BY decr LIMIT 1";
		try (PreparedStatement pstatement = con.prepareStatement(query);)
		{
			try(ResultSet result = pstatement.executeQuery())
			{
				while(result.next())
				{
					images_id = result.getInt("id");
				}
			}
		}
		return images_id+1;
	}
	
	public Image findImageById(int imageId) throws SQLException {
        String query = "SELECT id, title, path, creation_date, description FROM images WHERE id = ?";
        Image image = null;
        try (PreparedStatement pstatement = con.prepareStatement(query)) {
            pstatement.setInt(1, imageId);
            try (ResultSet result = pstatement.executeQuery()) {
                if (result.next()) {
                    image = new Image();
                    image.setImage_Id(result.getInt("id"));
                    image.setTitle(result.getString("title"));
                    image.setSystem_Path(result.getString("path"));
                    image.setCreation_Date(result.getDate("creation_date"));
                    image.setDescription(result.getString("description"));
                }
            }
        }
        return image;
    }
	
	public List<Image> RetrieveAllImagesByUser(User user) throws SQLException
	{
        List<Image> images = new ArrayList<>();
        String query = "SELECT id, title, path, creationDate, description FROM images as i, contains_images as c WHERE i.id = c.imageId AND c.userId = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, user.getId());
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Image image = new Image();
                    image.setImage_Id(result.getInt("id"));
                    image.setTitle(result.getString("title"));
                    image.setSystem_Path(result.getString("path"));
                    image.setCreation_Date(new Date());
                    image.setDescription(result.getString("description"));
                    images.add(image);
                }
            }
        }
        return images;
	}
}
