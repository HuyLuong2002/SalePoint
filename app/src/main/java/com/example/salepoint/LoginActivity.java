package com.example.salepoint;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtPhoneNumber;
    private Button btnSendCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
        edtPhoneNumber = findViewById(R.id.phoneNumberEditText);
        btnSendCode = findViewById(R.id.sendCodeButton);
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = edtPhoneNumber.getText().toString();
                // Kiểm tra kết nối Internet khi Activity được tạo
                if (checkInternetConnection()) {
                    sendVerificationCode(phoneNumber);
                } else {
                    //Xuất ra màn hình
                }

            }
        });
    }


    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84 " + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // Xuất ra Toast thông báo
                        Toast.makeText(LoginActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
// Xử lý khi có lỗi xảy ra trong quá trình xác thực
                        System.out.println(e.getMessage());
                        Toast.makeText(LoginActivity.this, "Xác thực thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        // Mã đã được gửi, chuyển người dùng đến màn hình nhập mã và xác thực
                        Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                        intent.putExtra("verificationId", s);
                        intent.putExtra("mobile", edtPhoneNumber.getText().toString());
                        startActivity(intent);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                // Có kết nối Internet
                return true;
            } else {
                // Không có kết nối Internet
                return false;
            }
        }
        return false;
    }
}
