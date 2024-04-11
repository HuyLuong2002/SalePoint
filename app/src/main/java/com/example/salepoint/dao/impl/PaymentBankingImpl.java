package com.example.salepoint.dao.impl;

import com.example.salepoint.RetrofitClient;
import com.example.salepoint.dao.IPaymentBanking;
import com.example.salepoint.model.CarInfo;
import com.example.salepoint.request.PaymentRequest;
import com.example.salepoint.response.PaymentResponse;

import retrofit2.Call;

public class PaymentBankingImpl implements IPaymentBanking {
    private final IPaymentBanking apiService;

    public PaymentBankingImpl() { apiService = RetrofitClient.getClient().create(IPaymentBanking.class);};

    @Override
    public Call<PaymentResponse> getStripeApiKey() {
        return apiService.getStripeApiKey();
    }

    @Override
    public Call<PaymentResponse> processPayment(String apiKey, PaymentRequest paymentRequest) {
        return apiService.processPayment(apiKey, paymentRequest);
    }
}
