package com.example.salepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.model.Receipt;
import com.example.salepoint.ui.adapter.DetailHistoryAdapter;
import com.example.salepoint.util.Utils;

public class DetailHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static DetailHistoryAdapter detailHistoryAdapter;
    private Receipt receipt;
    private TextView txtViewTongTien;
    private TextView txtViewDiemSuDung;
    private TextView txtViewTongTienCanThanhToan;
    private TextView txtViewDiemTichLuy;
    private TextView txtBackMainActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        Intent intent = getIntent();
        receipt = (Receipt) intent.getSerializableExtra("receipt");

        txtViewTongTienCanThanhToan = findViewById(R.id.txtViewTongTienCanThanhToan);
        txtViewDiemSuDung = findViewById(R.id.txtViewDiemSuDung);
        txtViewDiemTichLuy = findViewById(R.id.txtViewDiemTichLuy);
        txtViewTongTien = findViewById(R.id.txtViewTongTien);
        txtBackMainActivity = findViewById(R.id.txtBackMainActivity);

        System.out.println("Receipt: " + receipt);
        txtViewTongTien.setText(Utils.convertToVND(receipt.getTotalPrice() + receipt.getExchange_points()));
        txtViewDiemTichLuy.setText(String.valueOf("+ " + Utils.convertToVND(receipt.getTotalPrice()/1000)));
        if(receipt.getExchange_points() > 0) {
            txtViewDiemSuDung.setText(String.valueOf("- " + Utils.convertToVND(receipt.getExchange_points())));
        } else {
            txtViewDiemSuDung.setText("0");
        }
        txtViewTongTienCanThanhToan.setText(String.valueOf(Utils.convertToVND(receipt.getTotalPrice()) + "đ"));

        recyclerView = findViewById(R.id.RVDetailHistory);
        detailHistoryAdapter = new DetailHistoryAdapter(receipt);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(detailHistoryAdapter);

        txtBackMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHistoryActivity.this, MainActivity.class);
                intent.putExtra("destination", "history");
                startActivity(intent);
                finish(); // Kết thúc DetailHistoryActivity để tránh tạo ra nhiều activity trùng lặp
            }
        });
    }
}
