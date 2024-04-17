package com.example.salepoint.response;

import com.google.gson.annotations.SerializedName;

public class StripeApiKeyResponse {
    private String stripeApiKey;

    public String getStripeApiKey() {
        return stripeApiKey;
    }

    public void setStripeApiKey(String stripeApiKey) {
        this.stripeApiKey = stripeApiKey;
    }
}
