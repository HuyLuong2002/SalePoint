package com.example.salepoint.model;

public class User {
    private String Name;
    private String Password;

    private int Point;

    private Boolean isStaff;

    private String Link;

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
        isStaff = false;
        Point = 0;
        Link = "";
    }

    public boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }
    public String getName() {
        return Name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int point) {
        this.Point = point;
    }
}
