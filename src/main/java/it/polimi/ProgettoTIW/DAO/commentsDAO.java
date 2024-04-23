package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import it.polimi.ProgettoTIW.beans.Comment;

public class commentsDAO {
    private final Connection con;

    public commentsDAO(Connection connection) {
        this.con = connection;
    }

    public List<Comment> findCommentsByImage(int imageId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM `Comment` WHERE Image_Id= ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, imageId);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Comment comment = new Comment();
                    comment.setUser_id(result.getInt("id"));
                    comment.setText(result.getString("text"));
                    comment.setPublication_date(new Date());
                    comments.add(comment);
                }
            }
        }
        return comments;
    }

    public void addComment(Comment comment) throws SQLException {
        String query = "INSERT INTO `Comment` (Text, Id, Image_Id, Publication_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, comment.getText());
            pstatement.setInt(2, comment.getUser_id());
            pstatement.setInt(3, comment.getImage_id());
          //  pstatement.setDate(4, (java.sql.Date) comment.getPublication_date());
            java.sql.Date sqlDate = new java.sql.Date(comment.getPublication_date().getTime());
            pstatement.setDate(4, sqlDate);
            pstatement.executeUpdate();
        }
    }


    public void deleteAllComment(int imageId) throws SQLException {
        String query = "DELETE FROM `Comment` WHERE Id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, imageId);
            pstatement.executeUpdate();
        }
    }

}