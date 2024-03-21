package com.example.salepoint.model;


public class Service {

    private String id;
    private String name;
    private int price;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp modified;
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

    public Service(String id, String name, int price, boolean isActive, Timestamp createdAt, Timestamp modified) {
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public static class Timestamp {
        private long seconds;
        private long nanoseconds;

        // Getters và setters

        public long getSeconds() {
            return seconds;
        }

        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }

        public long getNanoseconds() {
            return nanoseconds;
        }

        public void setNanoseconds(long nanoseconds) {
            this.nanoseconds = nanoseconds;
        }


        // Có thể thêm phương thức khác để chuyển đổi timestamp này thành các đối tượng từ java.time.*
    }

}
