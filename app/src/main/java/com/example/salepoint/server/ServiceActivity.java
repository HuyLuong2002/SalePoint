package com.example.salepoint.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.RetrofitClient;
import com.example.salepoint.model.Service;
import com.example.salepoint.dao.IServiceDAO;
import com.example.salepoint.ui.adapters.ServiceAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                intent.putExtra("mode", "create");
                startActivity(intent);
            }
        });

        setDataOnListView();
    }

    public void setDataOnListView() {
        IServiceDAO apiService = RetrofitClient.getClient().create(IServiceDAO.class);
        Call<List<Service>> call = apiService.getServices();
        call.enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.isSuccessful()) {
                    List<Service> serviceList = response.body();
                    // Update RecyclerView with the new data
                    serviceAdapter = new ServiceAdapter(serviceList);
                    rcvListService.setLayoutManager(new LinearLayoutManager(ServiceActivity.this));
                    rcvListService.setAdapter(serviceAdapter);
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
