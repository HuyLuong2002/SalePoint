package com.example.salepoint;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.salepoint.dao.impl.ReceiptDAOImpl;
import com.example.salepoint.model.Point;
import com.example.salepoint.model.User;
import com.example.salepoint.response.PointResponse;
import com.example.salepoint.util.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.salepoint.databinding.ActivityMainBinding;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ReceiptDAOImpl receiptDAO;
    private TextView NameUser;
    private TextView PhoneNumber;
    private TextView Point;
    private ImageView Qrcode;
    private EditText NameProfile;
    private TextView PhoneProfile;
    private EditText EmailProfile;
    private EditText DOBProfile;
    private EditText AddressProfile;
    private Button btnUpdateInfo;
    private Button btnLogOut;
    private CircularProgressIndicator circularProgressIndicator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = binding.navView;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // Passing each menu ID as a set of Ids because each

        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        receiptDAO = new ReceiptDAOImpl();

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userId");
        String phone = intent.getStringExtra("mobile");
        String action = intent.getStringExtra("action");

        circularProgressIndicator = findViewById(R.id.progressBar);

        // Lắng nghe sự kiện khi điều hướng đến trang navigation_profile
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_profile) {
                // Ở đây bạn có thể gọi phương thức để thiết lập dữ liệu cho các EditText trong trang profile
                setProfileData(userID, phone);
            } else if (destination.getId() == R.id.navigation_home) {
                if (action.equalsIgnoreCase("loginWithPhone") && userID != null) {
//                    getPointByUserId(userID);
                    circularProgressIndicator.setVisibility(View.VISIBLE);
                    getUserDataFromFirebase(userID, phone);

                } else if (action.equalsIgnoreCase("loginWithEmail") && userID != null) {
                    String email = intent.getStringExtra("loginWithEmail");
                    getUserDataFromFirebase(userID, email);
                }
            }
        });

    }

    public void getUserDataFromLogin() {
        // Lấy thông tin người dùng hiện tại
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User đã đăng nhập, bạn có thể lấy thông tin của user từ đây
            String uid = user.getUid();
            String phoneNumber = user.getPhoneNumber(); // Lấy số điện thoại của người dùng
            String ConvertPhoneNumber = Utils.convertToZeroStartPhoneNumber(phoneNumber);
            System.out.println("Phone Number: " + phoneNumber);
            System.out.println("Convert phone: " + ConvertPhoneNumber);

            // và nhiều thông tin khác tùy thuộc vào việc bạn đã cung cấp thông tin khi đăng ký tài khoản
            Log.d(TAG, "User ID: " + uid);
            Log.d(TAG, "Phone Number: " + phoneNumber);
        } else {
            // User chưa đăng nhập
            Log.d(TAG, "User is not logged in");
        }
    }

    private void getUserDataFromFirebase(String uid, String phone) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        // Thực hiện truy vấn để lấy dữ liệu của người dùng dựa trên số điện thoại
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Dữ liệu của người dùng tồn tại
                    // Lấy thông tin của người dùng từ dataSnapshot
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String name = user.getName();
                        String phone = user.getPhone();
                        boolean isStaff = user.getIsStaff();
                        String link = user.getLink();
                        int point = user.getPoint();

                        // Trang Home
                        NameUser = findViewById(R.id.textViewName);
                        PhoneNumber = findViewById(R.id.textViewPhoneNumber);
                        Point = findViewById(R.id.textViewPoint);
                        Qrcode = findViewById(R.id.imageViewQr);

                        NameUser.setText(name);
                        PhoneNumber.setText(phone);
//                        Point.setText(String.valueOf(point));
                        getPointByUserId(uid);
                        Glide.with(MainActivity.this).load(link).into(Qrcode);

                        // và nhiều thông tin khác tùy thuộc vào việc bạn đã cung cấp thông tin khi đăng ký tài khoản
                        Log.d(TAG, "User ID: " + uid);
                        Log.d(TAG, "Phone Number: " + phone);

                        // Xử lý thông tin người dùng theo nhu cầu của bạn
                        circularProgressIndicator.setVisibility(View.GONE);
                    }
                } else {
                    // Không tìm thấy dữ liệu cho số điện thoại đã cho
                    Log.d(TAG, "Không tìm thấy dữ liệu cho số điện thoại: " + phone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e(TAG, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
            }
        });
    }

    private void setProfileData(String uid, String phone) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Thực hiện truy vấn để lấy dữ liệu của người dùng dựa trên số điện thoại
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Dữ liệu của người dùng tồn tại
                    // Lấy thông tin của người dùng từ dataSnapshot
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String name = user.getName();
                        String phone = user.getPhone();
                        String email = user.getEmail();
                        String DOB = user.getDateBirth();
                        String address = user.getAddress();

                        // Trang Profile
                        NameProfile = findViewById(R.id.editTextName);
                        PhoneProfile = findViewById(R.id.editTextPhone);
                        EmailProfile = findViewById(R.id.editTextEmail);
                        AddressProfile = findViewById(R.id.editTextAddress);
                        DOBProfile = findViewById(R.id.editTextBirthday);

                        NameProfile.setText(name);
                        PhoneProfile.setText(phone);
                        EmailProfile.setText(email);
                        DOBProfile.setText(DOB);
                        AddressProfile.setText(address);

                        // Set button update
                        btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
                        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String currenText = btnUpdateInfo.getText().toString();
                                if (currenText.equals("Edit")) {
                                    NameProfile.setEnabled(true);
                                    EmailProfile.setEnabled(true);
                                    DOBProfile.setEnabled(true);
                                    AddressProfile.setEnabled(true);
                                    btnUpdateInfo.setText("Update");
                                } else {
                                    NameProfile.setEnabled(false);
                                    EmailProfile.setEnabled(false);
                                    DOBProfile.setEnabled(false);
                                    AddressProfile.setEnabled(false);

                                    String newName = NameProfile.getText().toString();
                                    String newEmail = EmailProfile.getText().toString();
                                    String newDOB = DOBProfile.getText().toString();
                                    String newAddress = AddressProfile.getText().toString();

                                    if (!newName.equals(name)) {
                                        user.setName(newName);
                                    }
                                    if (!newEmail.equals(email)) {
                                        user.setEmail(newEmail);
                                    }
                                    if (!newDOB.equals(DOB)) {
                                        user.setDateBirth(newDOB);
                                    }
                                    if (!newAddress.equals(address)) {
                                        user.setAddress(newAddress);
                                    }

                                    // Cập nhật dữ liệu mới lên Firebase
                                    usersRef.child(uid).setValue(user);

                                    btnUpdateInfo.setText("Edit");
                                }
                            }
                        });

                        btnLogOut = findViewById(R.id.btnLogOut);
                        btnLogOut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                                startActivity(intent);
                            }
                        });

                        // và nhiều thông tin khác tùy thuộc vào việc bạn đã cung cấp thông tin khi đăng ký tài khoản
                        Log.d(TAG, "User ID: " + uid);
                        Log.d(TAG, "Phone Number: " + phone);

                        // Xử lý thông tin người dùng theo nhu cầu của bạn
                    }
                } else {
                    // Không tìm thấy dữ liệu cho số điện thoại đã cho
                    Log.d(TAG, "Không tìm thấy dữ liệu cho số điện thoại: " + phone);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e(TAG, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
            }
        });
    }

    private void getPointByUserId(String userId) {
        // Sử dụng ServiceDAOImpl để gọi API
        Call<PointResponse> call = receiptDAO.getPointByUserId(userId);
        call.enqueue(new Callback<PointResponse>() {
            @Override
            public void onResponse(Call<PointResponse> call, Response<PointResponse> response) {
                if (response.isSuccessful()) {
                    PointResponse pointResponse = response.body();
                    com.example.salepoint.model.Point point = pointResponse.getPointData();
                    Point.setText(String.valueOf(point.getPoint()));
                    System.out.println("Point: " + point.getPoint());
                } else {
                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<PointResponse> call, Throwable t) {
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }
}