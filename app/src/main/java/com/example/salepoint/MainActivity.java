package com.example.salepoint;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.salepoint.model.User;
import com.example.salepoint.util.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.salepoint.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView NameUser;
    private TextView PhoneNumber;
    private TextView Point;
    private ImageView Qrcode;
    private EditText NameProfile;
    private TextView PhoneProfile;
    private EditText EmailProfile;
    private EditText DOBProfile;
    private EditText AddressProfile;

    private AdView adView;
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        Intent intent = getIntent();
        String userID = intent.getStringExtra("userId");
        String phone = intent.getStringExtra("mobile");
        String action = intent.getStringExtra("action");


        // Lắng nghe sự kiện khi điều hướng đến trang navigation_profile
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_profile) {
                // Ở đây bạn có thể gọi phương thức để thiết lập dữ liệu cho các EditText trong trang profile
                setProfileData(userID, phone);
            } else if (destination.getId() == R.id.navigation_home) {
                if(action.equalsIgnoreCase("login") && userID != null)
                {
                    getUserDataFromFirebase(userID, phone);
                }
            }
        });

        loadBanner();

    }

    private void loadBanner() {

        // Create a new ad view.
        adView = findViewById(R.id.adView);

        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adView.loadAd(adRequest);
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
                        Point.setText(String.valueOf(point));
                        Picasso.get().load(link).into(Qrcode);

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

    private void setProfileData(String uid, String phone){
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

}