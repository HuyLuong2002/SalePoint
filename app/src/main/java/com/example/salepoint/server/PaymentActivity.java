package com.example.salepoint.server;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.dao.impl.CarInfoDAOImpl;
import com.example.salepoint.dao.impl.ServiceDAOImpl;
import com.example.salepoint.model.CarInfo;
import com.example.salepoint.model.Service;
import com.example.salepoint.model.User;
import com.example.salepoint.response.CarInfoResponse;
import com.example.salepoint.ui.adapter.CarInfoSpinnerAdapter;
import com.example.salepoint.ui.adapter.SelectedServiceAdapter;
import com.example.salepoint.ui.dialog.AddServiceDialog;
import com.example.salepoint.util.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
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

public class PaymentActivity extends AppCompatActivity {

    public static List<Service> selectedServiceList = new ArrayList<>();
    private RecyclerView recyclerViewServices;
    private SelectedServiceAdapter selectedServiceAdapter;
    private DatabaseReference mDatabase;

    private Spinner spinner;
    private List<String> emptyList;

    private List<CarInfo> carInfoList;

    private CarInfoDAOImpl carInfoDAO;

    public static User customer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        carInfoDAO = new CarInfoDAOImpl();
        MaterialButton btnAddService = findViewById(R.id.btnAddService);
        MaterialButton btnAddCarInfo = findViewById(R.id.btnAddCarInfo);
        TextInputEditText editText = findViewById(R.id.textView8);
        TextInputEditText editText1 = findViewById(R.id.textView16);
        TextInputEditText editText2 = findViewById(R.id.textView18);

        
        btnAddCarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
        String userID = intent.getStringExtra("userID");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Truy vấn dữ liệu người dùng từ Firebase Realtime Database
        mDatabase.child("users").child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                // Xử lý dữ liệu người dùng ở đây
                                editText.setText(user.getPhone());
                                editText1.setText(user.getName());
                                editText2.setText(String.valueOf(user.getPoint()));
                                customer = user;
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

        //Đọc data vào list car info nếu data rỗng thì thêm dữ liêu dô empty list, nguược lại thì list đó hiển thị


        // Khởi tạo danh sách rỗng
        emptyList = new ArrayList<>();
        getDataUserCarInfo(userID);


    }

    private void getDataUserCarInfo(String userID) {
        Call<CarInfoResponse> call = carInfoDAO.getCarsOfUser(userID);
        call.enqueue(new Callback<CarInfoResponse>() {
            @Override
            public void onResponse(Call<CarInfoResponse> call, Response<CarInfoResponse> response) {
                if (response.isSuccessful()) {
                    CarInfoResponse carInfos = response.body();
                    carInfoList = carInfos.getCarInfos();
                    spinner = findViewById(R.id.spinner1);
                    if (!carInfoList.isEmpty()) {

                        // Thiết lập Adapter cho Spinner với danh sách xe
                        CarInfoSpinnerAdapter adapter = new CarInfoSpinnerAdapter(PaymentActivity.this, android.R.layout.simple_spinner_item, carInfoList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        // Xử lý sự kiện khi người dùng chọn một bản ghi từ Spinner
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Hiển thị dữ liệu của bản ghi được chọn trong LinearLayout
                                showSelectedCarInfo(carInfoList.get(position));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Không cần xử lý trong trường hợp này
                            }
                        });

                    } else {
                        emptyList.add("Không có dữ liệu");
                        // Thiết lập Adapter cho Spinner với danh sách rỗng
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(PaymentActivity.this, android.R.layout.simple_spinner_item, emptyList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                } else {
                    // Xử lý khi có lỗi từ phía server
                    System.out.println("goi fail");
                }
            }

            @Override
            public void onFailure(Call<CarInfoResponse> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình gửi yêu cầu
                System.out.println(t.getMessage());
            }
        });
    }

    private void showSelectedCarInfo(CarInfo selectedCarInfo) {
        // Hiển thị LinearLayout và thiết lập dữ liệu
        LinearLayout linearLayout = findViewById(R.id.linearLayoutCarInfo);
        linearLayout.setVisibility(View.VISIBLE);

        TextView textViewCarName = findViewById(R.id.textView45);
        TextView textViewLicensePlate = findViewById(R.id.textView37);
        TextView textViewCarCompany = findViewById(R.id.textView33);
        TextView textViewCarBlockDivision = findViewById(R.id.textView35);
        TextView textViewCarSpeedometer = findViewById(R.id.textView39);
        TextView textViewCarNumberOfChangeOil = findViewById(R.id.textView41);


        textViewCarName.setText(selectedCarInfo.getName());
        textViewLicensePlate.setText(selectedCarInfo.getLicense_plate());
        textViewCarCompany.setText(selectedCarInfo.getCar_company());
        textViewCarBlockDivision.setText(String.valueOf(selectedCarInfo.getBlock_division()));
        textViewCarSpeedometer.setText(String.valueOf(selectedCarInfo.getSpeedometer()));
        textViewCarNumberOfChangeOil.setText(String.valueOf(selectedCarInfo.getNumber_of_oil_changes()));

    }

}
