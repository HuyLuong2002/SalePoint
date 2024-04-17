package com.example.salepoint.server;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.LoginActivity;
import com.example.salepoint.R;
import com.example.salepoint.dao.impl.ReceiptDAOImpl;
import com.example.salepoint.model.Point;
import com.example.salepoint.model.User;
import com.example.salepoint.response.PointListResponse;
import com.example.salepoint.ui.adapter.UserAdapter;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private List<Point> pointList;
    private ReceiptDAOImpl receiptDAO = new ReceiptDAOImpl();
    private CircularProgressIndicator circularProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management_list);

        recyclerView = findViewById(R.id.recyclerView);
        circularProgressBar = findViewById(R.id.progressBar);
        userList = new ArrayList<>();

        circularProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        getAllPoint();


        userAdapter = new UserAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Thiết lập DividerItemDecoration với màu sắc mong muốn
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_drawable));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(userAdapter);


    }

    private void readUserDataFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey(); // Lấy ID của record
                        User user = snapshot.getValue(User.class);
                        user.setId(userId); // Lưu ID vào đối tượng User
                        if (!user.getId().equals(LoginActivity.currentUser.getId())) {
                            userList.add(user);
                        }

                    }
                    for (User user : userList) {
                        for (Point point : pointList) {
                            if (user.getId().equals(point.getCustomer())) {
                                user.setPoint(point.getPoint());
                            }
                        }
                    }
                    userAdapter.notifyDataSetChanged();
                    circularProgressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey(); // Lấy ID của record
                        User user = snapshot.getValue(User.class);
                        user.setId(userId); // Lưu ID vào đối tượng User
                        if (!user.getId().equals(currentUser.getUid())) {
                            userList.add(user);
                        }

                    }
                    for (User user : userList) {
                        for (Point point : pointList) {
                            if (user.getId().equals(point.getCustomer())) {
                                user.setPoint(point.getPoint());
                            }
                        }
                    }
                    userAdapter.notifyDataSetChanged();
                    circularProgressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllPoint() {
        // Sử dụng ServiceDAOImpl để gọi API
        Call<PointListResponse> call = receiptDAO.getAllPoints();
        call.enqueue(new Callback<PointListResponse>() {
            @Override
            public void onResponse(Call<PointListResponse> call, Response<PointListResponse> response) {
                if (response.isSuccessful()) {
                    PointListResponse pointListResponse = response.body();
                    pointList = pointListResponse.getPointData();

                    // Gọi phương thức để đọc dữ liệu từ Realtime Database
                    readUserDataFromDatabase();

                } else {
                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<PointListResponse> call, Throwable t) {
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }
}
