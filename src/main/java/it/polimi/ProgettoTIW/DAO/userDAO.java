package it.polimi.ProgettoTIW.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoTIW.beans.User;

public class userDAO {
    private final Connection con;

    public userDAO(Connection connection) {
        this.con = connection;
    }

    public User checkCredentials(String username, String password) throws SQLException {
        
        String query = "SELECT Id, Username, Password FROM `User` WHERE Username = ? AND Password = ?";

        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            try (ResultSet result = pstatement.executeQuery();) {
                if (!result.isBeforeFirst())
                    return null;
                else {
                    result.next();
                    User user = new User();
                    user.setId(result.getInt("Id"));
                    user.setUsername(result.getString("Username"));
                    user.setPassword(result.getString("Password"));
                    return user;
                }
            }
        }
    }
    
    public int checkUsrn(String username) throws SQLException {
        String query = "SELECT COUNT(*) as users_count FROM User WHERE Username = ?";
        int users_count = 0;
        
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, username);
            try (ResultSet result = pstatement.executeQuery();) {
                if (!result.isBeforeFirst())
                    return -1;
                else {
                    users_count = result.getInt("users_count");
                    return users_count;
                }
            }
        }
    }

    public void registerUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, user.getUsername());
            pstatement.setString(2, user.getEmail()); 
            pstatement.setString(3, user.getPassword());
            pstatement.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET name = ? WHERE id = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, user.getUsername());
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
    
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, email FROM users";
        try (PreparedStatement pstatement = con.prepareStatement(query);
             ResultSet result = pstatement.executeQuery();) {
            while (result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
                user.setEmail(result.getString("email"));
                users.add(user);
            }
        }
        return users;
    }
}
