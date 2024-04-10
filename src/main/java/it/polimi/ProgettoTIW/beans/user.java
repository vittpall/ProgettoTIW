package it.polimi.ProgettoTIW.beans;

import java.time.LocalDateTime;
import java.util.Date;

public class user {
    
    private String Username;
    private String Email;
    private String Password;
    private LocalDateTime Reg_Date;

    public String getUsername() {
        return Username;
    }

    public String getEmail() {
        return Email;
    }

    public LocalDateTime getReg_Date() {
        return Reg_Date;
    }

    public String getPassword() {
        return Password;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setReg_Date(LocalDateTime currentDateTime) {
        Reg_Date = currentDateTime;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
