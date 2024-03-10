package com.example.salepoint.model;

public class User {
    private String Name;
    private String Password;

    private boolean IsStaff;

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;
        IsStaff = false;
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
}
