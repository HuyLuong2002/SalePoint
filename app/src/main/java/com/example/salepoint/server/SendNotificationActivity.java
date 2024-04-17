package com.example.salepoint.server;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;

public class SendNotificationActivity extends AppCompatActivity {

    private MaterialButton btnSendNotificationNotInApp;

    private EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        btnSendNotificationNotInApp = findViewById(R.id.btnGetDeviceToken);

        editText = findViewById(R.id.edtDeviceToken);

        btnSendNotificationNotInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Lấy token của thiết bị để gửi thông báo
                FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    String deviceToken = task.getResult();
                                    System.out.println(deviceToken);
                                    editText.setText(deviceToken);
                                    editText.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
    }
}
