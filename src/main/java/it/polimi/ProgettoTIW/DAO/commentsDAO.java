package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polimi.ProgettoTIW.beans.Comment;

public class commentsDAO {
    private final Connection con;

    public commentsDAO(Connection connection) {
        this.con = connection;
    }

    public List<Comment> findCommentsByImage(int imageId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT id, text, userId FROM comments WHERE imageId = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, imageId);
            try (ResultSet result = pstatement.executeQuery();) {
                while (result.next()) {
                    Comment comment = new Comment();
                    comment.setUser_id(result.getInt("id"));
                    comment.setText(result.getString("text"));
                  //  comment.setUser_id(result.getInt("userId"));
                    comments.add(comment);
                }
            }
        }
        return comments;
    }

    public void addComment(Comment comment) throws SQLException {
        String query = "INSERT INTO comments (text, userId, imageId) VALUES (?, ?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, comment.getText());
            pstatement.setInt(2, comment.getUser_id());
            pstatement.setInt(3, comment.getImage_id());
            pstatement.executeUpdate();
        }
    }

    public boolean updateComment(int commentId, String newText) throws SQLException {
        String query = "UPDATE comments SET text = ? WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, newText);
            pstatement.setInt(2, commentId);
            int rowsAffected = pstatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public void deleteComment(int commentId) throws SQLException {
        String query = "DELETE FROM comments WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, commentId);
            pstatement.executeUpdate();
        }
    }
}