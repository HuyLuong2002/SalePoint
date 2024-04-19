package com.example.salepoint;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.dao.IPaymentBanking;
import com.example.salepoint.request.PaymentRequest;
import com.example.salepoint.response.PaymentResponse;
import com.example.salepoint.server.AdminActivity;
import com.example.salepoint.server.PaymentActivity;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.Address;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.StripeIntent;
import com.stripe.android.view.CardInputWidget;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentInfoActivity extends AppCompatActivity {
    private Button buttonPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);

        PaymentConfiguration.init(getApplicationContext(), Constants.PUBLISHABLE_KEY_LUONG);

        buttonPay = findViewById(R.id.buttonSubmit);

        Intent intent = getIntent();
        long amount = intent.getLongExtra("amount",0);


        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processPayment(amount);
            }
        });
    }

    private void processPayment(long amount) {
        IPaymentBanking paymentBankingService = RetrofitClient.getClient().create(IPaymentBanking.class);

        PaymentRequest paymentRequest = new PaymentRequest(amount);

        Call<PaymentResponse> call = paymentBankingService.processPayment("Bearer " + Constants.PUBLISHABLE_KEY_LUONG, paymentRequest);

        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful()) {
                    PaymentResponse paymentResponse = response.body();

                    if (paymentResponse != null && paymentResponse.isSuccess()) {
                        Stripe stripe = new Stripe(getApplicationContext(), Constants.PUBLISHABLE_KEY_LUONG);

                        CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);

                        PaymentMethodCreateParams.Card card = cardInputWidget.getPaymentMethodCard();

                        PaymentMethod.BillingDetails billingDetails = new PaymentMethod.BillingDetails.Builder()
                                .setName(PaymentActivity.customer.getName()) // Tên người thanh toán
                                .setEmail(PaymentActivity.customer.getEmail()) // Email người thanh toán
                                .setPhone(PaymentActivity.customer.getPhone()) // Số điện thoại
                                .build();

                        PaymentMethodCreateParams paymentMethodParams = PaymentMethodCreateParams.create(
                                card,
                                billingDetails
                        );


                        ConfirmPaymentIntentParams params = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                                paymentMethodParams, paymentResponse.getClientSecret());


                        stripe.confirmPayment(PaymentInfoActivity.this, params);
                        Toast.makeText(getApplicationContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PaymentInfoActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PaymentInfoActivity.this, "Payment failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaymentInfoActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Toast.makeText(PaymentInfoActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
