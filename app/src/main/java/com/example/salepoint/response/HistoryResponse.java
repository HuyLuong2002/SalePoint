package com.example.salepoint.response;

import com.example.salepoint.model.History;

import java.util.List;

public class HistoryResponse {
    private boolean success;
    private List<History> Histories;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<History> getHistories() {
        return Histories;
    }

    public void setInfo(List<History> histories) {
        this.Histories = histories;
    }
}
