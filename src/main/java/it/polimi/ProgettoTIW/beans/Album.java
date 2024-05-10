package it.polimi.ProgettoTIW.beans;

import java.time.LocalDateTime;

public class Album {
    //primary key
    //i added Username because i use it to retrieve data from the list of album
	private String Username; 
    private int User_id;
    private String Title;

    private LocalDateTime Creation_Date;

    public void setCreation_Date(LocalDateTime creation_Date) {
        Creation_Date = creation_Date;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }
    
    public void setUsername(String Username)
    {
    	this.Username = Username;
    } 

    public int getUser_id() {
        return User_id;
    }

    public LocalDateTime getCreation_Date() {
        return Creation_Date;
    }

    public String getTitle() {
        return Title;
    }
    public String getUsername() {
    	return Username;
    }
}
