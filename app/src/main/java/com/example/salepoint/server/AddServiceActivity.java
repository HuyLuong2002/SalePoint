package com.example.salepoint.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.R;
import com.example.salepoint.RetrofitClient;
import com.example.salepoint.dao.IServiceDAO;
import com.example.salepoint.dao.impl.ServiceDAOImpl;
import com.example.salepoint.model.Service;
import com.example.salepoint.response.ServiceResponse;
import com.example.salepoint.util.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceActivity extends AppCompatActivity {

    private ServiceDAOImpl serviceDAO;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management_add);

        // Khởi tạo ServiceDAOImpl
        serviceDAO = new ServiceDAOImpl();

        TextInputEditText edtServiceName = findViewById(R.id.edtServiceName);
        TextInputEditText edtServicePrice = findViewById(R.id.edtServicePrice);
        RadioButton rb_active = findViewById(R.id.rb_active);
        RadioButton rb_inactive = findViewById(R.id.rb_inactive);
        RadioGroup radioGroup = findViewById(R.id.rdg_active);
        MaterialButton btnAddService = findViewById(R.id.btnAddService);
        MaterialButton btnEditService = findViewById(R.id.btnEditService);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        switch (mode) {
            case "create":
                edtServiceName.setText("");
                edtServicePrice.setText("");
                rb_active.setChecked(true);
                rb_active.setClickable(false);
                rb_inactive.setClickable(false);
                break;

            case "edit":
                String id = intent.getStringExtra("serviceId");
                String name = intent.getStringExtra("serviceName");
                int price = intent.getIntExtra("servicePrice", 0);
                boolean active = intent.getBooleanExtra("isActive", false);
                edtServiceName.setText(name);
                edtServicePrice.setText(String.valueOf(price));
                btnAddService.setVisibility(View.INVISIBLE);
                btnEditService.setVisibility(View.VISIBLE);
                rb_active.setClickable(true);
                rb_inactive.setClickable(true);
                btnEditService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String serviceName = String.valueOf(edtServiceName.getText());
                        int servicePrice = Integer.parseInt(String.valueOf(edtServicePrice.getText()));
                        boolean active;
                        int radioButtonId = radioGroup.getCheckedRadioButtonId();
                        if(rb_active.getId() == radioButtonId)
                        {
                            active = true;
                        }
                        else {
                            active = false;
                        }
                        Service service = new Service(serviceName, servicePrice, active);
                        updateService(id, service);
                    }
                });
                if (active) {
                    rb_active.setChecked(true);
                } else {
                    rb_inactive.setChecked(true);
                }

                break;
        }

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = String.valueOf(edtServiceName.getText());
                int servicePrice = Integer.parseInt(String.valueOf(edtServicePrice.getText()));
                Service service = new Service(serviceName, servicePrice);
                createService(service);
            }
        });



    }

    public void createService(Service service) {
        Call<Void> call = serviceDAO.createService(service);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("Add succes");
                    Intent intent1 = new Intent(AddServiceActivity.this, ServiceActivity.class);
                    startActivity(intent1);
                } else {
                    // Xử lý lỗi khi thêm dịch vụ
                    System.out.println("Add failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi khi gọi API
            }
        });
    }


    public void updateService(String serviceID, Service updatedService) {
        Call<Void> call = serviceDAO.updateService(serviceID, updatedService);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cập nhật dịch vụ thành công
                    // Cập nhật giao diện người dùng nếu cần
                    System.out.println("Update succes");
                    Intent intent = new Intent(AddServiceActivity.this, ServiceActivity.class);
                    startActivity(intent);
                } else {
                    // Xử lý lỗi khi cập nhật dịch vụ
                    System.out.println("Update fail");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi khi gọi API
            }
        });
    }
}
