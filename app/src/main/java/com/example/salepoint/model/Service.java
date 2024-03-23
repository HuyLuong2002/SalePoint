package com.example.salepoint.model;


import com.example.salepoint.util.Utils;

public class Service {

    private String id;
    private String name;
    private int price;
    private boolean isActive;
    private String createdAt;
    private String modified;
    public Service() {
    }

    public Service(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Service(String name, int price, boolean isActive) {
        this.name = name;
        this.price = price;
        this.isActive = isActive;
    }

    public Service(String id, String name, int price, boolean isActive, String createdAt, String modified) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
