package com.example.salepoint.model;

import java.io.Serializable;
import java.util.List;

public class Receipt implements Serializable {

    private String id;

    private String car_id;
    private String customer;

    private String paymentMethod;
    private int totalPrice;
    private int totalQuantity;

    private int exchange_points;

    private List<DetailReceipt> detailReceipt;

    private boolean isActive;

    private String createdAt;
    private String modified;

    public Receipt() {
    }

    public Receipt(String car_id, String customer, String paymentMethod, int totalPrice, int totalQuantity) {
        this.car_id = car_id;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.isActive = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
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

    public int getExchange_points() {
        return exchange_points;
    }

    public void setExchange_points(int exchange_points) {
        this.exchange_points = exchange_points;
    }

    public List<DetailReceipt> getDetailReceipt() {
        return detailReceipt;
    }

    public void setDetailReceipt(List<DetailReceipt> detailReceipt) {
        this.detailReceipt = detailReceipt;
    }
}
