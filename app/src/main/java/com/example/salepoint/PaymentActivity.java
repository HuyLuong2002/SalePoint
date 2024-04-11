package com.example.salepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.dao.IPaymentBanking;
import com.example.salepoint.dao.impl.PaymentBankingImpl;
import com.example.salepoint.response.PaymentResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private Button buttonPay;
    private IPaymentBanking paymentBanking;
    private String stripeApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        // Initialize PaymentBankingImpl
        paymentBanking = new PaymentBankingImpl();

        // Map the views
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonPay = findViewById(R.id.buttonProceed);

        // Call API to get stripeApiKey
        Call<PaymentResponse> apiKeyCall = paymentBanking.getStripeApiKey();
        System.out.println(apiKeyCall);
        apiKeyCall.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {

                if (response.isSuccessful()) {
                    PaymentResponse paymentResponse = response.body();
                    if (paymentResponse != null) {
                        stripeApiKey = paymentResponse.getClientSecret();

                        // Set click listener for payment button
                        buttonPay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Check entered amount
                                String amountString = editTextAmount.getText().toString().trim();
                                if (amountString.isEmpty()) {
                                    editTextAmount.setError("Please enter amount");
                                    editTextAmount.requestFocus();
                                    return;
                                }

                                // Convert amount from String to Long
                                long amount = Long.parseLong(amountString);

                                // Pass necessary data to PaymentInfoActivity
                                Intent intent = new Intent(PaymentActivity.this, PaymentInfoActivity.class);
                                intent.putExtra("amount", amount);
                                intent.putExtra("stripeApiKey", stripeApiKey);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    System.out.println("Failed to get Stripe API Key");
                    // Handle error when getting stripeApiKey
                    Toast.makeText(PaymentActivity.this, "Failed to get Stripe API Key!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                // Handle error
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(PaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
