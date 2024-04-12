package com.example.salepoint.request;

import com.google.gson.annotations.SerializedName;

public class PaymentRequest {
    @SerializedName("amount")
    private long amount;

    // Constructor with arguments
    public PaymentRequest(long amount) {
        this.amount = amount;
    }

    // Getters and setters
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

}
