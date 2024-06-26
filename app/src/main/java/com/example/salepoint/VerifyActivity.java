package com.example.salepoint;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.salepoint.model.User;
import com.example.salepoint.server.AdminActivity;
import com.example.salepoint.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity {

    public FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Button btnGetOTP;
    private EditText number1;
    private EditText number2;
    private EditText number3;
    private EditText number4;
    private EditText number5;
    private EditText number6;
    private TextInputEditText txtPassword;
    private Button btnLogin;
    private LinearLayout initialLayout;
    private LinearLayout passwordLayout;
    private Button resend;
    private String verificationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        Intent intent = getIntent();
        verificationId = intent.getStringExtra("verificationId");
        String phoneNumber = intent.getStringExtra("mobile");
//        mToken = intent.getStringExtra("token");

        // Truy cập firebase
        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);

        btnGetOTP = findViewById(R.id.getCodeButton);
        btnLogin = findViewById(R.id.loginButton);
        initialLayout = findViewById(R.id.initialLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        txtPassword = findViewById(R.id.passwordEditText);
        number1 = findViewById(R.id.NumberOTP_1);
        number2 = findViewById(R.id.NumberOTP_2);
        number3 = findViewById(R.id.NumberOTP_3);
        number4 = findViewById(R.id.NumberOTP_4);
        number5 = findViewById(R.id.NumberOTP_5);
        number6 = findViewById(R.id.NumberOTP_6);

        resend = findViewById(R.id.txtResendOTP);

        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP_number = number1.getText().toString() + number2.getText().toString() + number3.getText().toString() + number4.getText().toString() + number5.getText().toString() + number6.getText().toString();
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, OTP_number);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                String userUid = currentUser.getUid();
                                // Sử dụng userUid như một UUID trong hệ thống của bạn
                                DatabaseReference usersRef = database.getReference("users");

                                // Kiểm tra xem người dùng có tồn tại hay không
                                usersRef.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Người dùng đã tồn tại, xử lý theo cách bạn muốn
                                            String userId = dataSnapshot.getKey();
                                            boolean isStaff = dataSnapshot.child("isStaff").getValue(Boolean.class);
                                            if (isStaff) {
                                                // Người dùng là admin
                                                Intent intentAdmin = new Intent(VerifyActivity.this, AdminActivity.class);
                                                startActivity(intentAdmin);
                                                finish();
                                            } else {
                                                // Người dùng không phải là admin
                                                Intent intentHome = new Intent(VerifyActivity.this, MainActivity.class);
                                                intentHome.putExtra("mobile", phoneNumber);
                                                intentHome.putExtra("userId", userId);
                                                intentHome.putExtra("action", "loginWithPhone");
                                                startActivity(intentHome);
                                                finish();
                                            }
                                        } else {
                                            // Ẩn layout ban đầu
                                            initialLayout.setVisibility(View.GONE);
                                            // Hiển thị layout nhập password
                                            passwordLayout.setVisibility(View.VISIBLE);
                                            Toast.makeText(VerifyActivity.this, "Nhập OTP thành công!", Toast.LENGTH_SHORT).show();

                                            btnLogin.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String Password = txtPassword.getText().toString();
                                                    // Khởi tạo định dạng cho thời gian
                                                    @SuppressLint("SimpleDateFormat")
                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                    // Lấy thời gian hiện tại
                                                    Date currentTime = new Date();
                                                    // Biến đổi thời gian thành chuỗi theo định dạng đã định
                                                    String formattedTime = dateFormat.format(currentTime);

                                                    // Người dùng không tồn tại
                                                    User newUser = new User(phoneNumber, phoneNumber, Password, "", "", formattedTime, formattedTime, "", "");
                                                    usersRef.child(userUid).setValue(newUser);

                                                    // Tạo mã QR code từ chuỗi JSON
                                                    Bitmap bitmap = null;
                                                    try {
                                                        bitmap = Utils.encodeAsBitmap(userUid);
                                                        String fileName = userUid + ".jpg";
                                                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                                        StorageReference imageRef = storageRef.child("qr_code/" + fileName);
                                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                        byte[] data = baos.toByteArray();
                                                        UploadTask uploadTask = imageRef.putBytes(data);

                                                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {
                                                                            String imageUrl = uri.toString();
                                                                            updateImageUrlForUser(userUid, imageUrl);
                                                                            Intent intentHome = new Intent(VerifyActivity.this, MainActivity.class);
                                                                            intentHome.putExtra("mobile", phoneNumber);
                                                                            intentHome.putExtra("userId", userUid);
                                                                            intentHome.putExtra("action", "loginWithPhone");
                                                                            startActivity(intentHome);
                                                                            finish();
                                                                        }
                                                                    });
                                                                } else {
                                                                    Toast.makeText(VerifyActivity.this, "Sửa ảnh không thành công!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    } catch (WriterException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Toast.makeText(VerifyActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        System.out.println("Database error: " + error.getMessage());
                                    }
                                });
                            } else {
                                // Người dùng chưa đăng nhập
                            }
                        } else {
                            Toast.makeText(VerifyActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra kết nối Internet khi Activity được tạo
                if (Utils.checkInternetConnection(getApplicationContext())) {
                    resendVerificationCode(phoneNumber, LoginActivity.mToken);
                    System.out.println("verificationId: " + verificationId);
                } else {
                    //Xuất ra màn hình
                }
            }
        });

        resend.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_HOVER_ENTER:
                        resend.setTextColor(ContextCompat.getColor(VerifyActivity.this, R.color.teal_200));
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        resend.setTextColor(ContextCompat.getColor(VerifyActivity.this, R.color.red));
                        break;
                }
                return true;
            }
        });
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken mToken) {
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth);
        builder.setPhoneNumber("+84 " + phoneNumber);
        builder.setTimeout(60L, TimeUnit.SECONDS);
        builder.setForceResendingToken(mToken);
        builder.setActivity(this);
        builder.setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Xuất ra Toast thông báo
                Toast.makeText(VerifyActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
// Xử lý khi có lỗi xảy ra trong quá trình xác thực
                System.out.println(e.getMessage());
                Toast.makeText(VerifyActivity.this, "Xác thực thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // Mã đã được gửi, chuyển người dùng đến màn hình nhập mã và xác thực
                verificationId = s;
            }
        });
        PhoneAuthOptions options = builder
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void updateImageUrlForUser(String phoneNumber, String imageUrl) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Tạo một HashMap để cập nhật thông tin hình ảnh
        HashMap<String, Object> update = new HashMap<>();
        update.put("link", imageUrl);

        // Cập nhật thông tin hình ảnh của người dùng
        usersRef.child(phoneNumber).updateChildren(update)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Cập nhật thành công
                        Toast.makeText(VerifyActivity.this, "Cập nhật hình ảnh thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi khi cập nhật không thành công
                        Toast.makeText(VerifyActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
