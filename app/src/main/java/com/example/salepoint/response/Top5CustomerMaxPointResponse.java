package com.example.salepoint.response;

import com.example.salepoint.model.UserPoint;

import java.util.List;

public class Top5CustomerMaxPointResponse {

    private boolean success;

    private List<UserPoint> topCustomer;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<UserPoint> getTopCustomer() {
        return topCustomer;
    }

    public void setTopCustomer(List<UserPoint> topCustomer) {
        this.topCustomer = topCustomer;
    }
}
