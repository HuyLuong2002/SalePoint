package com.example.salepoint.model;

import java.util.List;

public class Revenue {
    private int totalrevenue;

    private List<RevenueByMonth> revenueByMonth;

    public int getTotalrevenue() {
        return totalrevenue;
    }

    public List<RevenueByMonth> getRevenueByMonth() {
        return revenueByMonth;
    }

    public void setRevenueByMonth(List<RevenueByMonth> revenueByMonth) {
        this.revenueByMonth = revenueByMonth;
    }

    public void setTotalrevenue(int totalrevenue) {
        this.totalrevenue = totalrevenue;
    }
}
