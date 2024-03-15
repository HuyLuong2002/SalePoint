package com.example.salepoint.model;

public class Service {

    private String Name;
    private String Price;

    private boolean IsActive;

    public Service() {
    }

    public Service(String name, String price) {
        Name = name;
        Price = price;
        IsActive = true;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
