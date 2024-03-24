package com.example.salepoint;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.salepoint.model.User;
import com.example.salepoint.util.Utils;
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

        NameUser = findViewById(R.id.textViewName);
        PhoneNumber = findViewById(R.id.textViewPhoneNumber);
        Point = findViewById(R.id.textViewPoint);
        Qrcode = findViewById(R.id.imageViewQr);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userId");
        String phone = intent.getStringExtra("mobile");
        String action = intent.getStringExtra("action");
        if(action.equalsIgnoreCase("login") && userID != null)
        {
            getUserDataFromFirebase(userID, phone);
        }


    }

    public void getUserDataFromLogin()
    {
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

                        NameUser.setText(name);
                        PhoneNumber.setText(phone);
                        Point.setText(String.valueOf(point));
                        Picasso.get().load(link).into(Qrcode);

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