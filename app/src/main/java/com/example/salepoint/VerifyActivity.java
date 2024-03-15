package com.example.salepoint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.model.User;
import com.example.salepoint.server.AdminActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

public class VerifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        Intent intent = getIntent();
        String verificationId = intent.getStringExtra("verificationId");
        String phoneNumber = intent.getStringExtra("mobile");
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, "123456");
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersRef = database.getReference("users");

                    // Kiểm tra xem người dùng có tồn tại hay không
                    usersRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Người dùng đã tồn tại, xử lý theo cách bạn muốn, ví dụ thông báo cho người dùng
                                // Kiểm tra xem người dùng có là admin không
                                boolean isStaff = dataSnapshot.child("isStaff").getValue(Boolean.class);
                                Intent intentAdmin = null;
                                if (isStaff) {
                                    // Người dùng là admin, chuyển qua màn hình admin
                                    intentAdmin = new Intent(VerifyActivity.this, AdminActivity.class);

                                    startActivity(intentAdmin);
                                } else {
                                    // Người dùng không phải là admin, có thể làm gì đó khác ở đây

                                }
                            } else {
                                // Người dùng không tồn tại, thêm vào cơ sở dữ liệu
                                User newUser = new User(phoneNumber, "");
                                usersRef.child(phoneNumber).setValue(newUser);

                                // Chuyển đối tượng thành chuỗi JSON

                                Gson gson = new Gson();
                                String json = gson.toJson(newUser);

                                // Tạo mã QR code từ chuỗi JSON
                                Bitmap bitmap = null;
                                try {
                                    bitmap = encodeAsBitmap(json);
                                    String fileName = phoneNumber + ".jpg";
                                    // Tạo tham chiếu đến Firebase Storage
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                                    // Tham chiếu đến một đường dẫn (ví dụ: images/) và tên file
                                    StorageReference imageRef = storageRef.child("qr_code/" + fileName);

                                    // Chuyển đổi Bitmap thành byte array
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    // Upload byte array lên Firebase Storage
                                    UploadTask uploadTask = imageRef.putBytes(data);

                                    // Lắng nghe sự kiện hoàn thành upload
                                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                // Upload thành công, lấy đường dẫn URL của ảnh
                                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        System.out.println(imageUrl);
                                                        // Lưu URL của ảnh vào cơ sở dữ liệu Firebase Realtime Database hoặc Cloud Firestore nếu cần
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
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("Database error: " + error.getMessage());
                        }
                    });

                    Toast.makeText(VerifyActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
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
}
