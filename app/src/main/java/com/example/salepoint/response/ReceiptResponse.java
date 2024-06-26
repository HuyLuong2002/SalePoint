package com.example.salepoint.response;

import com.example.salepoint.model.CarInfo;
import com.example.salepoint.model.Receipt;

import java.util.List;

public class ReceiptResponse {

    private boolean success;
    private List<Receipt> Receipt;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Receipt> getReceiptList() {
        return Receipt;
    }

    public void setReceiptList(List<Receipt> receiptList) {
        this.Receipt = receiptList;
    }
}
