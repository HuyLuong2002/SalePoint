package com.example.salepoint;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.model.User;
import com.example.salepoint.server.AdminActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
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

public class VerifyActivity extends AppCompatActivity {

    public FirebaseUser currentUser;
    private Button btnGetOTP;
    private EditText number1;
    private EditText number2;
    private EditText number3;
    private EditText number4;
    private EditText number5;
    private EditText number6;
    private EditText txtPassword;
    private Button btnLogin;
    private LinearLayout initialLayout;
    private LinearLayout passwordLayout;
    private int flag = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        Intent intent = getIntent();
        String verificationId = intent.getStringExtra("verificationId");
        String phoneNumber = intent.getStringExtra("mobile");
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
                                            flag = 1;
                                            // Người dùng đã tồn tại, xử lý theo cách bạn muốn
                                            String userId = dataSnapshot.getKey();
                                            boolean isStaff = dataSnapshot.child("isStaff").getValue(Boolean.class);
                                            if (isStaff) {
                                                // Người dùng là admin
                                                Intent intentAdmin = new Intent(VerifyActivity.this, AdminActivity.class);
                                                startActivity(intentAdmin);
                                            } else {
                                                // Người dùng không phải là admin

                                                Intent intentHome = new Intent(VerifyActivity.this, MainActivity.class);
                                                intentHome.putExtra("mobile", phoneNumber);
                                                intentHome.putExtra("userId", userId);
                                                intentHome.putExtra("action", "login");
                                                startActivity(intentHome);
                                            }
                                        } else {
                                            // Ẩn layout ban đầu
                                            initialLayout.setVisibility(View.GONE);
                                            // Hiển thị layout nhập password
                                            passwordLayout.setVisibility(View.VISIBLE);
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
                                                    User newUser = new User(phoneNumber, phoneNumber, Password,"","", formattedTime, formattedTime);
                                                    usersRef.child(userUid).setValue(newUser);

                                                    // Tạo mã QR code từ chuỗi JSON
                                                    Bitmap bitmap = null;
                                                    try {
                                                        bitmap = encodeAsBitmap(userUid);
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
                                                                            intentHome.putExtra("action", "login");
                                                                            startActivity(intentHome);
                                                                        }
                                                                    });
                                                                } else {
                                                                    //Update không thành công
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
                                if (flag == 1) {
                                    Toast.makeText(VerifyActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VerifyActivity.this, "Nhập OTP thành công!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Người dùng chưa đăng nhập
                            }
                        } else {
                            Toast.makeText(VerifyActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                flag = 0;
            }
        });
    }

    private Bitmap encodeAsBitmap(String data) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500, null);
        } catch (IllegalArgumentException e) {
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
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
