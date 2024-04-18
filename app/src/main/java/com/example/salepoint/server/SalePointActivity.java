package com.example.salepoint.server;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.dao.impl.ReceiptDAOImpl;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.response.ReceiptResponse;
import com.example.salepoint.ui.adapter.ReceiptAdapter;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalePointActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ReceiptDAOImpl receiptDAO;
    private List<Receipt> receiptList;

    public static ReceiptAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_point_management_list);

        // Khởi tạo ProgressBar
        progressBar = findViewById(R.id.progressBar);

        receiptDAO = new ReceiptDAOImpl();
        receiptList = new ArrayList<>();

        getAllReceiptForManager();
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
