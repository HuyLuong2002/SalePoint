package com.example.salepoint.model;

public class User {
    private String Id;
    private String Phone;
    private String Name;
    private String Password;
    private int Point;
    private boolean isStaff;
    private String Link;
    private String Gender;
    private String DateBirth;
    private String Email;
    private boolean isActive;
    private String Created;
    private String Modified;
    private String Address;

    public User() {
    }

    public User(String phone, String name, String password, String gender, String dateBirth, String created, String modified, String email, String address) {
        Phone = phone;
        Name = name;
        Password = password;
        Gender = gender;
        DateBirth = dateBirth;
        Created = created;
        Modified = modified;
        isStaff = false;
        Point = 0;
        Link = "";
        isActive = true;
        Email = email;
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDateBirth() {
        return DateBirth;
    }

    public void setDateBirth(String dateBirth) {
        DateBirth = dateBirth;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }


}
