package com.example.salepoint.server;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.dao.impl.ServiceDAOImpl;
import com.example.salepoint.model.Service;
import com.example.salepoint.response.ServiceResponse;
import com.example.salepoint.ui.adapter.ServiceAdapter;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {

    public static ServiceAdapter serviceAdapter;
    private RecyclerView rcvListService;
    private ServiceDAOImpl serviceDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management_menu);

        MaterialButton btnAddService = findViewById(R.id.btnAddService);
        rcvListService = findViewById(R.id.recyclerView);

        // Khởi tạo ServiceDAOImpl
        serviceDAO = new ServiceDAOImpl();

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, AddServiceActivity.class);
                intent.putExtra("mode", "create");
                startActivity(intent);
            }
        });

        setDataOnListView();
    }

    public void setDataOnListView() {
        // Sử dụng ServiceDAOImpl để gọi API
        Call<ServiceResponse> call = serviceDAO.getServices();
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if (response.isSuccessful()) {
                    ServiceResponse serviceResponse = response.body();
                    List<Service> serviceList = serviceResponse.getServices();
                    // Update RecyclerView with the new data
                    serviceAdapter = new ServiceAdapter(serviceList);
                    rcvListService.setLayoutManager(new LinearLayoutManager(ServiceActivity.this));
                    rcvListService.setAdapter(serviceAdapter);
                } else {
                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }
}
