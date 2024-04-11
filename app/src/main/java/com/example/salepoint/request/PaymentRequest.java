package com.example.salepoint.request;

import com.google.gson.annotations.SerializedName;

public class PaymentRequest {
    @SerializedName("amount")
    private long amount;
    @SerializedName("cardNumber")
    private String cardNumber;
    @SerializedName("expiryDate")
    private String expiryDate;
    @SerializedName("cvc")
    private String cvc;

    // Constructor with arguments
    public PaymentRequest(long amount, String cardNumber, String expiryDate, String cvc) {
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvc = cvc;
    }

    // Getters and setters
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}
