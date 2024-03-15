package com.example.salepoint.model;

public class User {
    private String Name;
    private String Password;

    private int point;

    private boolean IsStaff;

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
        IsStaff = false;
        point = 0;
    }

    public boolean getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(boolean isStaff) {
        IsStaff = isStaff;
    }


    public String getName() {
        return Name;
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
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isStaff() {
        return IsStaff;
    }

    public void setStaff(boolean staff) {
        IsStaff = staff;
    }
}
