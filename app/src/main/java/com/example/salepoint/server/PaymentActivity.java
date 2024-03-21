package com.example.salepoint.server;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.R;
import com.example.salepoint.model.Service;
import com.example.salepoint.ui.dialog.AddServiceDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    public static List<Service> selectedServiceList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        MaterialButton btnAddService = findViewById(R.id.btnAddService);
        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of AddServiceDialog
                AddServiceDialog addServiceDialog = new AddServiceDialog();
                // Show the dialog
                addServiceDialog.show(getSupportFragmentManager(), "AddServiceDialog");
            }
        });


    }

}
