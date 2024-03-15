package com.example.salepoint.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.Service;
import com.example.salepoint.ui.adapters.ServiceAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private ServiceAdapter serviceAdapter;
    private RecyclerView rcvListService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management_menu);

        MaterialButton btnAddService = findViewById(R.id.btnAddService);
        rcvListService = findViewById(R.id.recyclerView);

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, AddServiceActivity.class);
                startActivity(intent);
            }
        });

        setDataOnListView();
    }

    public void setDataOnListView() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("services");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Service> serviceList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Service service = dataSnapshot.getValue(Service.class);
                    serviceList.add(service);
                }
                System.out.println(serviceList.size());
                // Update RecyclerView with the new data
                serviceAdapter = new ServiceAdapter(serviceList);
                rcvListService.setLayoutManager(new LinearLayoutManager(ServiceActivity.this));
                rcvListService.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
