package com.example.salepoint.server;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.R;
import com.example.salepoint.RetrofitClient;
import com.example.salepoint.model.Service;
import com.example.salepoint.services.ServicesService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management_add);

        TextInputEditText edtServiceName = findViewById(R.id.edtServiceName);
        TextInputEditText edtServicePrice = findViewById(R.id.edtServicePrice);
        RadioButton rb_active = findViewById(R.id.rb_active);
        RadioButton rb_inactive = findViewById(R.id.rb_inactive);
        MaterialButton btnAddService = findViewById(R.id.btnAddService);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        switch (mode)
        {
            case "create":
                edtServiceName.setText("");
                edtServiceName.setText("");
                rb_active.setActivated(true);
                break;

            case "edit":
                // Lấy bundle từ intent
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    // Lấy đối tượng Service từ bundle
                    Service service = (Service) bundle.getSerializable("service");
                    // Xử lý đối tượng Service ở đây

                }
                break;
        }

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = String.valueOf(edtServiceName.getText());
                String servicePrice = String.valueOf(edtServicePrice.getText());
                Service service = new Service(serviceName, servicePrice);

//                ServicesService apiService = RetrofitClient.getClient().create(ServicesService.class);
//
//                Call<List<Service>> call = apiService.getServices();
//                call.enqueue(new Callback<List<Service>>() {
//                    @Override
//                    public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
//                        if (response.isSuccessful()) {
//                            List<Service> userList = response.body();
//                            // Xử lý dữ liệu nhận được
//                        } else {
//                            // Xử lý khi có lỗi từ máy chủ
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Service>> call, Throwable t) {
//                        // Xử lý khi gặp lỗi trong quá trình gọi API
//                    }
//                });

                //lưu vào firestore
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                db.collection("services").add(service)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d(TAG, "Service added with ID: " + documentReference.getId());
//                                Intent intent = new Intent(AddServiceActivity.this, ServiceActivity.class);
//                                startActivity(intent);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error adding service", e);
//                            }
//                        });
            }
        });

    }
}
