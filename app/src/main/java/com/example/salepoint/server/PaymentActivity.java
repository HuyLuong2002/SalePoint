package com.example.salepoint.server;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.Service;
import com.example.salepoint.model.User;
import com.example.salepoint.ui.adapter.SelectedServiceAdapter;
import com.example.salepoint.ui.dialog.AddServiceDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    public static List<Service> selectedServiceList = new ArrayList<>();
    private RecyclerView recyclerViewServices;
    private SelectedServiceAdapter selectedServiceAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        MaterialButton btnAddService = findViewById(R.id.btnAddService);
        TextInputEditText editText = findViewById(R.id.textView8);
        TextInputEditText editText1 = findViewById(R.id.textView16);
        TextInputEditText editText2 = findViewById(R.id.textView18);

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of AddServiceDialog
                AddServiceDialog addServiceDialog = new AddServiceDialog();
                // Show the dialog
                addServiceDialog.show(getSupportFragmentManager(), "AddServiceDialog");
            }
        });

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneUserNumber");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Truy vấn dữ liệu người dùng từ Firebase Realtime Database
        mDatabase.child("users").child(phoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                // Xử lý dữ liệu người dùng ở đây
                                editText.setText(phoneNumber);
                                editText1.setText(user.getName());
                                editText2.setText(String.valueOf(user.getPoint()));
                            }
                        } else {
                            Toast.makeText(PaymentActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(PaymentActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        // Khởi tạo RecyclerView
        recyclerViewServices = findViewById(R.id.recyclerView);
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        selectedServiceAdapter = new SelectedServiceAdapter(selectedServiceList);
        recyclerViewServices.setAdapter(selectedServiceAdapter);
    }

}
