package com.example.salepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.model.User;
import com.example.salepoint.util.Utils;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtPhoneNumber;
    private Button btnSendCode;
    private Button btnloginWithPasswordButton;
    private TextView txtLoginWithPassword;
    private TextView txtLoginOTP;
    private LinearLayout LayoutLoginWithOTPCode;
    private LinearLayout LayoutLoginWithPassword;
    private EditText edtPhoneNumber_Password;
    private EditText edtPassword;
    private String phoneNumber_Password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
        edtPhoneNumber = findViewById(R.id.phoneNumberEditText);

        btnSendCode = findViewById(R.id.sendCodeButton);
        btnloginWithPasswordButton = findViewById(R.id.btnLoginPassword);

        txtLoginWithPassword = findViewById(R.id.TextViewLoginPassword);
        txtLoginOTP = findViewById(R.id.LoginOTPText);

        LayoutLoginWithOTPCode = findViewById(R.id.LoginOTPLayout);
        LayoutLoginWithPassword = findViewById(R.id.loginPasswordLayout);

        edtPhoneNumber_Password = findViewById(R.id.phoneNumberLoginWithPassword);
        edtPassword = findViewById(R.id.passwordEditText);

        txtLoginWithPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutLoginWithOTPCode.setVisibility(View.GONE);
                LayoutLoginWithPassword.setVisibility(View.VISIBLE);
            }
        });

        txtLoginOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutLoginWithOTPCode.setVisibility(View.VISIBLE);
                LayoutLoginWithPassword.setVisibility(View.GONE);
            }
        });
        btnloginWithPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = edtPhoneNumber_Password.getText().toString();
                String password = edtPassword.getText().toString();

                // Kiểm tra xem số điện thoại đã bắt đầu bằng "0" hay không
                if (phoneNumber.startsWith("0")) {
                    // Nếu bắt đầu bằng "0", thêm "+84" vào đầu số điện thoại và loại bỏ số "0" ở đầu
                    phoneNumber_Password = "+84" + phoneNumber.substring(1);
                }

                // Truy cập Firebase Realtime Database để kiểm tra thông tin đăng nhập
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                usersRef.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (user != null && user.getPassword().equals(password)) {
                                    String userId = userSnapshot.getKey();
                                    // Nếu thông tin đăng nhập chính xác, chuyển hướng người dùng đến MainActivity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("mobile", phoneNumber);
                                    System.out.println("Phone Number: " + phoneNumber_Password);
                                    intent.putExtra("action", "login");
                                    startActivity(intent);
                                    finish(); // Đóng activity hiện tại để không quay lại nếu nhấn nút back
                                    return;
                                }
                            }
                            // Nếu password không chính xác
                            Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                        } else {
                            // Nếu không tìm thấy số điện thoại trong database
                            System.out.println("SĐT: " + phoneNumber_Password);
                            Toast.makeText(LoginActivity.this, "Số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra
                        Toast.makeText(LoginActivity.this, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = edtPhoneNumber.getText().toString();
                // Kiểm tra kết nối Internet khi Activity được tạo
                if (Utils.checkInternetConnection(getApplicationContext())) {
                    sendVerificationCode(phoneNumber);
                } else {
                    //Xuất ra màn hình
                }

            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth);
        builder.setPhoneNumber("+84 " + phoneNumber);
        builder.setTimeout(60L, TimeUnit.SECONDS);
        builder.setActivity(this);
        builder.setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
        });
        PhoneAuthOptions options = builder
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

}
