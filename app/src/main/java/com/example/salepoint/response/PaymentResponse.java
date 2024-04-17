package com.example.salepoint.response;

import com.google.gson.annotations.SerializedName;

public class PaymentResponse {
    private boolean success;

    private String client_secret;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getClientSecret() {
        return client_secret;
    }

    public void setClientSecret(String clientSecret) {
        this.client_secret = clientSecret;
    }
}
