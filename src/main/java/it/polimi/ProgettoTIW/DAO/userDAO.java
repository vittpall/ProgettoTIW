package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoTIW.beans.user;

public class userDAO {
    private final Connection con;

    public userDAO(Connection connection) {
        this.con = connection;
    }

    public user checkCredentials(String username, String password) throws SQLException {
        String query = "SELECT id, username FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            try (ResultSet result = pstatement.executeQuery();) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    result.next();
                    user user = new user();
                    user.setUser_id(result.getInt("id"));
                    user.setUsername(result.getString("username"));
                    return user;
                }
            }
        }
    }
    
    public int checkUsrn(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, username);
            try (ResultSet result = pstatement.executeQuery();) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    result.next();
                    user user = new user();
                    user.setUsername(result.getString("username"));
                    return user;
                }
            }
        }
    }

    public void registerUser(user user) throws SQLException {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, user.getUsername());
            pstatement.setString(2, user.getEmail()); 
            pstatement.setString(3, user.getPassword());
            pstatement.executeUpdate();
        }
    }

    public void updateUser(user user) throws SQLException {
        String query = "UPDATE users SET name = ? WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, user.getName());
            pstatement.setInt(2, user.getId());
            pstatement.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setInt(1, userId);
            pstatement.executeUpdate();
        }
    }
    
    public List<user> getAllUsers() throws SQLException {
        List<user> users = new ArrayList<>();
        String query = "SELECT id, username, name FROM users";
        try (PreparedStatement pstatement = con.prepareStatement(query);
             ResultSet result = pstatement.executeQuery();) {
            while (result.next()) {
                user user = new user();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
                user.setEmail(result.getString("email"));
                users.add(user);
            }
        }
        return users;
    }
}
