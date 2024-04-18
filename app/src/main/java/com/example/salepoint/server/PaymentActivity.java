package com.example.salepoint.server;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.PaymentInfoActivity;
import com.example.salepoint.R;
import com.example.salepoint.dao.IPaymentBanking;
import com.example.salepoint.dao.impl.CarInfoDAOImpl;
import com.example.salepoint.dao.impl.PaymentBankingImpl;
import com.example.salepoint.dao.impl.ReceiptDAOImpl;
import com.example.salepoint.model.CarInfo;
import com.example.salepoint.model.DetailReceipt;
import com.example.salepoint.model.Point;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.model.Service;
import com.example.salepoint.model.User;
import com.example.salepoint.response.CarInfoResponse;
import com.example.salepoint.response.PaymentResponse;
import com.example.salepoint.response.PointResponse;
import com.example.salepoint.ui.adapter.CarInfoSpinnerAdapter;
import com.example.salepoint.ui.adapter.SelectedServiceAdapter;
import com.example.salepoint.ui.dialog.AddCarInfoDialog;
import com.example.salepoint.ui.dialog.AddServiceDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
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


    private RecyclerView recyclerViewServices;
    public static SelectedServiceAdapter selectedServiceAdapter;
    public static List<Service> selectedServiceList = new ArrayList<>();

    public static List<CarInfo> carInfoList = new ArrayList<>();
    public static CarInfoSpinnerAdapter carInfoSpinnerAdapter;

    public static User customer;
    private DatabaseReference mDatabase;

    private Spinner spinner;
    private List<String> emptyList;

    private IPaymentBanking paymentBanking;
    private CarInfoDAOImpl carInfoDAO;
    private ReceiptDAOImpl receiptDAO;

    private CarInfo carInfo;

    private TextInputEditText editText2, editText3, editText4, editText5;

    private ProgressBar progressBar;
    private String stripeApiKey;
    private String userID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        selectedServiceList.clear();

        carInfoDAO = new CarInfoDAOImpl();
        receiptDAO = new ReceiptDAOImpl();
        // Initialize PaymentBankingImpl
        paymentBanking = new PaymentBankingImpl();

        MaterialButton btnAddService = findViewById(R.id.btnAddService);
        MaterialButton btnAddCarInfo = findViewById(R.id.btnAddCarInfo);
        MaterialButton btnPayment = findViewById(R.id.btnPayment);
        RadioButton rb_banking = findViewById(R.id.rb_banking);
        RadioButton rb_cash = findViewById(R.id.rb_cash);
        RadioButton rb_change = findViewById(R.id.rb_change);
        RadioButton rb_not_change = findViewById(R.id.rb_not_change);
        RadioGroup rg_payment = findViewById(R.id.rg_payment);
        RadioGroup rg_exchange_point = findViewById(R.id.rg_exchange_point);
        TextInputEditText editText = findViewById(R.id.textView8);
        TextInputEditText editText1 = findViewById(R.id.textView16);

        editText2 = findViewById(R.id.textView18);
        editText3 = findViewById(R.id.textView47);
        editText4 = findViewById(R.id.textView39);
        editText5 = findViewById(R.id.textView41);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();

        if(intent != null)
        {
            userID = intent.getStringExtra("userID");
        }

        getPointByUserId(userID);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        // Truy vấn dữ liệu người dùng từ Firebase Realtime Database
        mDatabase.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                // Xử lý dữ liệu người dùng ở đây
                                editText.setText(user.getPhone());
                                editText1.setText(user.getName());
                                customer = user;
                                customer.setId(userID);
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

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_banking.getId() == rg_payment.getCheckedRadioButtonId()) {
                    int totalPrice = 0;
                    List<DetailReceipt> detailReceiptList = new ArrayList<>();
                    for (Service service : selectedServiceList) {
                        // Assuming each CarInfo object has a getPrice() method to get its price
                        totalPrice += service.getPrice();
                        DetailReceipt detailReceipt = new DetailReceipt(service.getName(), service.getPrice(), 1);
                        detailReceiptList.add(detailReceipt);
                    }

                    if (validate() && validatePointInput()) {
                        Receipt receipt = new Receipt(carInfo.getId(), customer.getId(), "credit_card", totalPrice, selectedServiceList.size());
                        if (rb_change.getId() == rg_exchange_point.getCheckedRadioButtonId()) {
                            int point = Integer.parseInt(editText3.getText().toString());
                            receipt.setExchange_points(point);
                        }
                        receipt.setDetailReceipt(detailReceiptList);
                        createReceipt(receipt, totalPrice, true);

                        //lấy thông tin car info mới
                        CarInfo updatedCarInfo = carInfo;
                        updatedCarInfo.setSpeedometer(Integer.parseInt(editText4.getText().toString()));
                        updatedCarInfo.setNumber_of_oil_changes(Integer.parseInt(editText5.getText().toString()));
                        updatedCarInfo.setCreatedAt(null);
                        updatedCarInfo.setModified(null);
                        updateCarInfo(carInfo.getId(), updatedCarInfo);
                    }

                } else if (rb_cash.getId() == rg_payment.getCheckedRadioButtonId()) {
                    int totalPrice = 0;
                    List<DetailReceipt> detailReceiptList = new ArrayList<>();
                    for (Service service : selectedServiceList) {
                        // Assuming each CarInfo object has a getPrice() method to get its price
                        totalPrice += service.getPrice();
                        DetailReceipt detailReceipt = new DetailReceipt(service.getName(), service.getPrice(), 1);
                        detailReceiptList.add(detailReceipt);
                    }

                    if (validate() && validatePointInput()) {
                        Receipt receipt = new Receipt(carInfo.getId(), customer.getId(), "cash", totalPrice, selectedServiceList.size());
                        if (rb_change.getId() == rg_exchange_point.getCheckedRadioButtonId()) {
                            int point = Integer.parseInt(editText3.getText().toString());
                            receipt.setExchange_points(point);
                        }
                        receipt.setDetailReceipt(detailReceiptList);
                        progressBar.setVisibility(View.VISIBLE);
                        createReceipt(receipt, totalPrice, false);

                        //lấy thông tin car info mới
                        CarInfo updatedCarInfo = carInfo;
                        updatedCarInfo.setSpeedometer(Integer.parseInt(editText4.getText().toString()));
                        updatedCarInfo.setNumber_of_oil_changes(Integer.parseInt(editText5.getText().toString()));
                        updatedCarInfo.setCreatedAt(null);
                        updatedCarInfo.setModified(null);
                        updateCarInfo(carInfo.getId(), updatedCarInfo);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        btnAddCarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCarInfoDialog addCarInfoDialog = new AddCarInfoDialog();
                // Show the dialog
                addCarInfoDialog.show(getSupportFragmentManager(), "AddCarInfoDialog");
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

        rg_exchange_point.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton.getText().equals("Có")) {
                    // Nếu chọn "Có", hiển thị TextInputLayout
                    editText3.setVisibility(View.VISIBLE);
                } else {
                    // Nếu chọn "Không", ẩn TextInputLayout
                    editText3.setVisibility(View.GONE);
                }
            }
        });

        // Thêm sự kiện TextWatcher cho editText3 để kiểm tra người dùng nhập liệu
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePointInput();
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

    // Phương thức để kiểm tra nếu danh sách dịch vụ rỗng
    private boolean validate() {
        if (selectedServiceList.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm dịch vụ trước khi thanh toán", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateCarInfo(String carInfoId, CarInfo carInfo) {
        Call<Void> call = carInfoDAO.updateCarInfo(carInfoId, carInfo);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("Add success");
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

    private void createReceipt(Receipt receipt, int totalPrice, boolean isCard) {
        Call<Void> call = receiptDAO.createReceipt(receipt);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if(isCard)
                    {
                        payment(totalPrice);
                    }
                    Intent intent = new Intent(PaymentActivity.this, AdminActivity.class);
                    startActivity(intent);

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

    private void payment(int totalPrice) {
        // Call API to get stripeApiKey
        Call<PaymentResponse> apiKeyCall = paymentBanking.getStripeApiKey();

        apiKeyCall.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {

                if (response.isSuccessful()) {
                    PaymentResponse paymentResponse = response.body();
                    if (paymentResponse != null) {
                        stripeApiKey = paymentResponse.getClientSecret();

                        // Convert amount from String to Long
                        long amount = Long.parseLong(String.valueOf(totalPrice).trim());

                        // Pass necessary data to PaymentInfoActivity
                        Intent intent = new Intent(PaymentActivity.this, PaymentInfoActivity.class);
                        intent.putExtra("amount", amount);
                        intent.putExtra("stripeApiKey", stripeApiKey);
                        startActivity(intent);

                    }
                } else {
                    System.out.println("Failed to get Stripe API Key");
                    // Handle error when getting stripeApiKey
                    Toast.makeText(PaymentActivity.this, "Failed to get Stripe API Key!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                // Handle error
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(PaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getPointByUserId(String userId) {
        progressBar.setVisibility(View.VISIBLE);
        // Sử dụng ServiceDAOImpl để gọi API
        Call<PointResponse> call = receiptDAO.getPointByUserId(userId);
        call.enqueue(new Callback<PointResponse>() {
            @Override
            public void onResponse(Call<PointResponse> call, Response<PointResponse> response) {
                if (response.isSuccessful()) {
                    PointResponse pointResponse = response.body();
                    Point point = pointResponse.getPointData();

                    if (!String.valueOf(point.getPoint()).isEmpty()) {
                        editText2.setText(String.valueOf(point.getPoint()));
                    }
                    progressBar.setVisibility(View.GONE);
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

    // Định nghĩa một phương thức để kiểm tra điểm nhập vào
    private boolean validatePointInput() {
        String pointString = editText3.getText().toString();
        if (!pointString.isEmpty()) {
            int point = Integer.parseInt(pointString);
            int currentPoint = Integer.parseInt(editText2.getText().toString());
            if (point > currentPoint) {
                // Nếu điểm nhập vào lớn hơn điểm hiện có, thiết lập màu đỏ
                editText3.setError("Điểm nhập vào lớn hơn điểm hiện có");
                editText3.setTextColor(getResources().getColor(R.color.red)); // Thiết lập màu chữ
                return false;
            } else {
                // Nếu điểm nhập vào hợp lệ, thiết lập màu xanh lá cây
                editText3.setError(null); // Xóa thông báo lỗi nếu có
                editText3.setTextColor(getResources().getColor(R.color.green)); // Thiết lập màu chữ
                return true;
            }
        }
        return true;
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
                        carInfoSpinnerAdapter = new CarInfoSpinnerAdapter(PaymentActivity.this, android.R.layout.simple_spinner_item, carInfoList);
                        carInfoSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(carInfoSpinnerAdapter);

                        // Xử lý sự kiện khi người dùng chọn một bản ghi từ Spinner
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Hiển thị dữ liệu của bản ghi được chọn trong LinearLayout
                                carInfo = carInfoList.get(position);
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
                        carInfoSpinnerAdapter = new CarInfoSpinnerAdapter(PaymentActivity.this, android.R.layout.simple_spinner_item, carInfoList);
                        carInfoSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(carInfoSpinnerAdapter);
                        // Xử lý sự kiện khi người dùng chọn một bản ghi từ Spinner
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Hiển thị dữ liệu của bản ghi được chọn trong LinearLayout
                                carInfo = carInfoList.get(position);
                                showSelectedCarInfo(carInfoList.get(position));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // Không cần xử lý trong trường hợp này
                            }
                        });
                    }
                    progressBar.setVisibility(View.GONE);
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
