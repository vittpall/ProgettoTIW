package it.polimi.ProgettoTIW.beans;

import java.util.Date;

public class album {
    //primary key
    private Integer User_id;
    private String Title;

    private Date Creation_Date;

    public void setCreation_Date(Date creation_Date) {
        Creation_Date = creation_Date;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setUser_id(Integer user_id) {
        User_id = user_id;
    }

    public Integer getUser_id() {
        return User_id;
    }

    public Date getCreation_Date() {
        return Creation_Date;
    }

    public String getTitle() {
        return Title;
    }
}
