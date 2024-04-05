package com.example.salepoint.server;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.DetailReceipt;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.ui.adapter.DetailReceiptAdapter;
import com.example.salepoint.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ViewSalePointActivity extends AppCompatActivity {

    private List<DetailReceipt> detailReceiptList;
    private Receipt receipt;
    private RecyclerView recyclerView;
    private DetailReceiptAdapter adapter;
    // Khai báo các TextView để lưu trữ giá trị từ layout
    private TextView textView31, textView32, textView29, textView35, textView1, textView3, textView30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_sale_point);

        // Nhận dữ liệu từ intent
        Intent intent = getIntent();
        if (intent != null) {
            receipt = (Receipt) intent.getSerializableExtra("selected_receipt");
            detailReceiptList = receipt.getDetailReceipt();
        }

        // Ánh xạ các TextView từ layout
        textView31 = findViewById(R.id.textView31);
        textView32 = findViewById(R.id.textView32);
        textView29 = findViewById(R.id.textView29);
        textView35 = findViewById(R.id.textView35);
        textView1 = findViewById(R.id.textView1);
        textView3 = findViewById(R.id.textView3);
        textView30 = findViewById(R.id.textView30);

        textView31.setText(receipt.getId());
        textView32.setText(receipt.getCustomer());
        textView29.setText(String.valueOf(receipt.getExchange_points()));
        textView35.setText(receipt.getCar_id());
        textView1.setText(Utils.convertToVND(receipt.getTotalPrice()));
        textView3.setText(String.valueOf(receipt.getTotalQuantity()));
        textView30.setText(receipt.isActive() ? "Active" : "Inactive");

        // Khởi tạo RecyclerView và thiết lập layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter và thiết lập cho RecyclerView
        adapter = new DetailReceiptAdapter(this, detailReceiptList);
        recyclerView.setAdapter(adapter);

        // Cập nhật adapter
        adapter.notifyDataSetChanged();
    }


}
