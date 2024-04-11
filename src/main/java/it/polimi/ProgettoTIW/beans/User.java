package it.polimi.ProgettoTIW.beans;

import java.time.LocalDateTime;

public class User {
    
	private int Id;
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
    
    public int getId()
    {
    	return Id;
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
    
    public void setId(int id)
    {
    	this.Id = Id;
    }
}
