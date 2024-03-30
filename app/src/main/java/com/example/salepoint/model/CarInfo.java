package com.example.salepoint.model;

import com.example.salepoint.util.Utils;

public class CarInfo {

    private String id;
    private String name;
    private String car_company;
    private int block_division;

    private String license_plate;

    private int speedometer;

    private int number_of_oil_changes;

    private String user;
    private boolean isActive;
    private String createdAt;
    private String modified;

    public CarInfo() {
    }

    public CarInfo(String name, String car_company, int block_division, String license_plate, int speedometer, int number_of_oil_changes, String user, boolean isActive, String createdAt, String modified) {
        this.name = name;
        this.car_company = car_company;
        this.block_division = block_division;
        this.license_plate = license_plate;
        this.speedometer = speedometer;
        this.number_of_oil_changes = number_of_oil_changes;
        this.user = user;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.modified = modified;
    }

    public CarInfo(String name, String car_company, int block_division, String license_plate, int speedometer, int number_of_oil_changes, String user) {
        this.name = name;
        this.car_company = car_company;
        this.block_division = block_division;
        this.license_plate = license_plate;
        this.speedometer = speedometer;
        this.number_of_oil_changes = number_of_oil_changes;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_company() {
        return car_company;
    }

    public void setCar_company(String car_company) {
        this.car_company = car_company;
    }

    public int getBlock_division() {
        return block_division;
    }

    public void setBlock_division(int block_division) {
        this.block_division = block_division;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public int getSpeedometer() {
        return speedometer;
    }

    public void setSpeedometer(int speedometer) {
        this.speedometer = speedometer;
    }

    public int getNumber_of_oil_changes() {
        return number_of_oil_changes;
    }

    public void setNumber_of_oil_changes(int number_of_oil_changes) {
        this.number_of_oil_changes = number_of_oil_changes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
