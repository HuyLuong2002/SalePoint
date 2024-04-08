package com.example.salepoint.response;

import com.example.salepoint.model.CarInfo;
import com.example.salepoint.model.Revenue;

import java.util.List;

public class TotalRevenueResponse {
    private boolean success;

    private Revenue Revenue;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public com.example.salepoint.model.Revenue getRevenue() {
        return Revenue;
    }

    public void setRevenue(com.example.salepoint.model.Revenue revenue) {
        Revenue = revenue;
    }
}
