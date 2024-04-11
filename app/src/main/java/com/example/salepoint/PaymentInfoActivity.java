package com.example.salepoint;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.dao.IPaymentBanking;
import com.example.salepoint.request.PaymentRequest;
import com.example.salepoint.response.PaymentResponse;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.stripe.android.model.PaymentMethod;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.Address;

public class PaymentInfoActivity extends AppCompatActivity {
    private EditText editTextCardNumber, editTextExpiryDate, editTextCVC;
    private Button buttonPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);

        PaymentConfiguration.init(getApplicationContext(), Constants.PUBLISHABLE_KEY);

        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        editTextCVC = findViewById(R.id.editTextCVC);
        buttonPay = findViewById(R.id.buttonSubmit);

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNumber = editTextCardNumber.getText().toString().trim();
                String expiryDate = editTextExpiryDate.getText().toString().trim();
                String cvc = editTextCVC.getText().toString().trim();

                processPayment(110000,cardNumber, expiryDate, cvc);
            }
        });
    }

    private void processPayment(long amount, String cardNumber, String expiryDate, String cvc) {
        IPaymentBanking paymentBankingService = RetrofitClient.getClient().create(IPaymentBanking.class);

        PaymentRequest paymentRequest = new PaymentRequest(amount, cardNumber, expiryDate, cvc);

        Call<PaymentResponse> call = paymentBankingService.processPayment("Bearer " + Constants.PUBLISHABLE_KEY, paymentRequest);

        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    PaymentResponse paymentResponse = response.body();


                    if (paymentResponse != null && paymentResponse.isSuccess()) {
                        Stripe stripe = new Stripe(getApplicationContext(), paymentResponse.getClientSecret());

                        CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);

                        PaymentMethodCreateParams.Card card = cardInputWidget.getPaymentMethodCard();

                        Address address = new Address.Builder()
                                .setLine1("123 Main Street") // Địa chỉ dòng 1
                                .setCity("Anytown") // Thành phố
                                .setState("CA") // Bang hoặc tỉnh
                                .setPostalCode("12345") // Mã bưu chính
                                .setCountry("US") // Quốc gia
                                .build();

                        PaymentMethod.BillingDetails billingDetails = new PaymentMethod.BillingDetails.Builder()
                                .setName("John Doe") // Tên người thanh toán
                                .setEmail("john@gmail.com") // Email người thanh toán
                                .setPhone("0315648792") // Số điện thoại
                                .setAddress(address) // Thông tin địa chỉ
                                .build();

                        PaymentMethodCreateParams paymentMethodParams = PaymentMethodCreateParams.create(
                                card,
                                billingDetails
                        );


                        ConfirmPaymentIntentParams params = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                                paymentMethodParams, paymentResponse.getClientSecret());

                        stripe.confirmPayment(PaymentInfoActivity.this, params);


                        Toast.makeText(PaymentInfoActivity.this, "Payment successful!", Toast.LENGTH_SHORT).show();
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
