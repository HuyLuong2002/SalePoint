package com.example.salepoint.server;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.dao.impl.ReceiptDAOImpl;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.request.ReceiptFilterRequest;
import com.example.salepoint.response.ReceiptResponse;
import com.example.salepoint.ui.adapter.ReceiptAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalePointActivity extends AppCompatActivity {

    private CardView cardView;
    private ProgressBar progressBar;
    private ReceiptDAOImpl receiptDAO;
    private List<Receipt> receiptList;

    public static ReceiptAdapter adapter;

    private boolean isActive;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_point_management_list);

        // Khởi tạo ProgressBar
        progressBar = findViewById(R.id.progressBar);
        cardView = findViewById(R.id.cardView);
        LinearLayout layout = findViewById(R.id.linearLayout3);
        ImageView imageView = findViewById(R.id.imageView);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        TextInputEditText startDateEditText = findViewById(R.id.startDateEditText);
        TextInputEditText endDateEditText = findViewById(R.id.endDateEditText);
        MaterialButton btnRemoveFilter = findViewById(R.id.btnRemoveFilter);
        MaterialButton btnFilter = findViewById(R.id.btnFilter);

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        Chip chip = findViewById(R.id.chip);
        Chip chip1 = findViewById(R.id.chip2);

        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");

        MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        MaterialDatePicker<Long> materialDatePicker1 = materialDateBuilder.build();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiptFilterRequest request = new ReceiptFilterRequest();
                request.setActive(isActive);
                request.setStartDate(startDateEditText.getText().toString());
                request.setEndDate(endDateEditText.getText().toString());
                filterReceipt(request);

                cardView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        });

        btnRemoveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa dữ liệu trong các TextInputEditText
                startDateEditText.setText("");
                endDateEditText.setText("");

                // Xóa dữ liệu trong ChipGroup
                chipGroup.clearCheck();

                // Reset trạng thái isActive
                isActive = false;

                // Gọi lại phương thức để lấy tất cả các hóa đơn mà không có bộ lọc
                getAllReceiptForManager();

                cardView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        });

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialDatePicker1.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER_1");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long aLong) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(aLong);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                @SuppressLint("DefaultLocale") String selectedDateString = String.format("%04d-%02d-%02d", year, month, day);
                startDateEditText.setText(selectedDateString);
            }
        });

        materialDatePicker1.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long aLong) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(aLong);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                @SuppressLint("DefaultLocale") String selectedDateString = String.format("%04d-%02d-%02d", year, month, day);
                endDateEditText.setText(selectedDateString);
            }
        });

        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                // Duyệt qua danh sách các id của các chip đã được chọn
                for (Integer chipId : list) {
                    // Lấy chip tương ứng với id
                    if (chip.getId() == chipId) {
                        isActive = true;
                    } else if (chip1.getId() == chipId) {
                        isActive = false;
                    }
                }
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                constraintLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_gray));
                cardView.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                cardView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        });


        receiptDAO = new ReceiptDAOImpl();

        receiptList = new ArrayList<>();

        getAllReceiptForManager();

    }


    private void filterReceipt(ReceiptFilterRequest request) {
        Call<ReceiptResponse> call = receiptDAO.filterReceipt(request);
        call.enqueue(new Callback<ReceiptResponse>() {
            @Override
            public void onResponse(Call<ReceiptResponse> call, Response<ReceiptResponse> response) {
                if (response.isSuccessful()) {
                    ReceiptResponse pointResponse = response.body();
                    receiptList = pointResponse.getReceiptList();
                    // Thiết lập adapter cho RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    adapter = new ReceiptAdapter(SalePointActivity.this, receiptList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SalePointActivity.this));
                    // Thiết lập DividerItemDecoration với màu sắc mong muốn
//                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SalePointActivity.this, LinearLayoutManager.VERTICAL);
//                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(SalePointActivity.this, R.drawable.divider_drawable));
//                    recyclerView.addItemDecoration(dividerItemDecoration);
                    recyclerView.setAdapter(adapter);
                    // Ẩn ProgressBar khi nhận được kết quả từ API
                    progressBar.setVisibility(View.GONE);
                } else {

                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<ReceiptResponse> call, Throwable t) {
                // Ẩn ProgressBar khi gặp lỗi
                progressBar.setVisibility(View.GONE);
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }

    private void getAllReceiptForManager() {
        // Hiển thị ProgressBar khi bắt đầu gọi API
        progressBar.setVisibility(View.VISIBLE);
        // Sử dụng ServiceDAOImpl để gọi API
        Call<ReceiptResponse> call = receiptDAO.getAllReceiptForManager();
        call.enqueue(new Callback<ReceiptResponse>() {
            @Override
            public void onResponse(Call<ReceiptResponse> call, Response<ReceiptResponse> response) {
                if (response.isSuccessful()) {
                    ReceiptResponse pointResponse = response.body();
                    receiptList = pointResponse.getReceiptList();
                    // Thiết lập adapter cho RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    adapter = new ReceiptAdapter(SalePointActivity.this, receiptList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SalePointActivity.this));
                    // Thiết lập DividerItemDecoration với màu sắc mong muốn
//                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SalePointActivity.this, LinearLayoutManager.VERTICAL);
//                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(SalePointActivity.this, R.drawable.divider_drawable));
//                    recyclerView.addItemDecoration(dividerItemDecoration);
                    recyclerView.setAdapter(adapter);
                    // Ẩn ProgressBar khi nhận được kết quả từ API
                    progressBar.setVisibility(View.GONE);
                } else {

                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<ReceiptResponse> call, Throwable t) {
                // Ẩn ProgressBar khi gặp lỗi
                progressBar.setVisibility(View.GONE);
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }

}
