package com.example.salepoint.response;

import com.example.salepoint.model.CarInfo;

import java.util.List;

public class ReceiptCarInfoResponse {
    private boolean success;
    private List<CarInfo> CarInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CarInfo> getCarInfos() {
        return CarInfo;
    }

    public void setInfo(List<CarInfo> services) {
        this.CarInfo = services;
    }
}
