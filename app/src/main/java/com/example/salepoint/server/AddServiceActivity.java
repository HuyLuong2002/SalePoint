package com.example.salepoint.server;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.R;
import com.example.salepoint.model.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management_add);

        TextInputEditText edtServiceName = findViewById(R.id.edtServiceName);
        TextInputEditText edtServicePrice = findViewById(R.id.edtServicePrice);
        MaterialButton btnAddService = findViewById(R.id.btnAddService);

        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = String.valueOf(edtServiceName.getText());
                String servicePrice = String.valueOf(edtServicePrice.getText());
                Service service = new Service(serviceName, servicePrice);

                //lưu vào realtime database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("services");
                String serviceId = databaseReference.push().getKey(); // Generate a unique key for the service
                databaseReference.child(serviceId).setValue(service);

                //lưu vào firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("services").add(service)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Service added with ID: " + documentReference.getId());
                                Intent intent = new Intent(AddServiceActivity.this, ServiceActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding service", e);
                            }
                        });
            }
        });

    }
}
