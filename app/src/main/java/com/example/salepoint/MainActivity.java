package com.example.salepoint;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salepoint.dao.impl.HistoryDAOImpl;

import com.example.salepoint.dao.impl.ReceiptDAOImpl;
import com.example.salepoint.model.History;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.model.User;
import com.example.salepoint.response.HistoryResponse;
import com.example.salepoint.response.PointResponse;
import com.example.salepoint.ui.adapter.HistoryAdapter;
import com.example.salepoint.util.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.net.CronetProviderInstaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.chromium.net.CronetEngine;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String SERVER_TOKEN;

    private double latitue = 10.7600;
    private double longtitue = 106.6823;

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
    private HistoryDAOImpl historyDAO;
    private List<History> Histories;
    RecyclerView recyclerView;

    public static HistoryAdapter adapter;
    private AdView adView;
    private GoogleMap mMap;

    private ActivityMainBinding binding;


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
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        receiptDAO = new ReceiptDAOImpl();
        historyDAO = new HistoryDAOImpl();
        Histories = new ArrayList<>();

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userId");
        String phone = intent.getStringExtra("mobile");
        String action = intent.getStringExtra("action");
        String destinationIntent = intent.getStringExtra("destination");

        if (destinationIntent != null && destinationIntent.equals("history")) {
            navController.navigate(R.id.navigation_history);
            getHistoryByUserId(userID);
        }

        // Lắng nghe sự kiện khi điều hướng đến trang navigation_profile
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            if (destination.getId() == R.id.navigation_profile) {
                // Ở đây bạn có thể gọi phương thức để thiết lập dữ liệu cho các EditText trong trang profile
                setProfileData(userID, phone);
            } else if (destination.getId() == R.id.navigation_home) {
                if (action.equalsIgnoreCase("loginWithPhone") && userID != null) {

                    getUserDataFromFirebase(userID, phone);

                    //circularProgressIndicator.setVisibility(View.VISIBLE);
                } else if (action.equalsIgnoreCase("loginWithEmail") && userID != null) {
                    String email = intent.getStringExtra("loginWithEmail");
                    getUserDataFromFirebase(userID, email);
                }


            } else if (destination.getId() == R.id.navigation_history) {
                getHistoryByUserId(userID);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = new SupportMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
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
                        String link = user.getLink();
//                        String point = Utils.convertToVND(user.getPoint());

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
                        Log.d(TAG, "User ID home: " + uid);
                        Log.d(TAG, "Phone Number: " + phone);
                        loadBanner();
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
                        Log.d(TAG, "User ID profile: " + uid);
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
                    Point.setText(Utils.convertToVND(point.getPoint()));
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

    private void getHistoryByUserId(String userId) {
        Call<HistoryResponse> call = historyDAO.getHistoryByUserId(userId);

        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if (response.isSuccessful()) {
                    HistoryResponse historyResponse = response.body();
                    Histories = historyResponse.getHistories();
//                    System.out.println("List History: " + Histories);

                    // Thiết lập adapter cho RecyclerView
                    recyclerView = findViewById(R.id.RVParentHistory);
                    adapter = new HistoryAdapter(MainActivity.this, Histories);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                } else {
                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng currentLocation = new LatLng(latitue, longtitue);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Đặt mức độ thu phóng ở đây (15 là mặc định)

    }
}