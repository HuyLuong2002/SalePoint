package com.example.salepoint.server;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserActivity extends AppCompatActivity {

    private TextInputEditText edtUserName;
    private TextInputEditText edtUserPhone;

    private RadioButton rb_male;
    private RadioButton rb_female;
    private TextInputEditText edtUserDateBirth;

    private RadioGroup rdg_gender;
    private RadioGroup rdg_active;
    private RadioButton rb_active;
    private RadioButton rb_inactive;
    private MaterialButton btnEditService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management_add);

        edtUserName = findViewById(R.id.edtUserName);
        edtUserPhone = findViewById(R.id.edtUserPhone);
        rdg_gender = findViewById(R.id.rdg_gender);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        edtUserDateBirth = findViewById(R.id.edtUserDateBirth);
        rdg_active = findViewById(R.id.rdg_active);
        rb_active = findViewById(R.id.rb_active);
        rb_inactive = findViewById(R.id.rb_inactive);
        btnEditService = findViewById(R.id.btnEditService);

        Intent intent = getIntent();
        String userPhone = intent.getStringExtra("userPhone");
        String userName = intent.getStringExtra("userName");
        String userGender = intent.getStringExtra("userGender");
        String userDateBirth = intent.getStringExtra("userDateBirth");
        boolean userActive = intent.getBooleanExtra("userActive", false);

        String userId = intent.getStringExtra("userId");

        edtUserName.setText(userName);
        edtUserPhone.setText(userPhone);
        edtUserDateBirth.setText(userDateBirth);

        if(userGender.equals("Nam"))
        {
            rb_male.setChecked(true);
        }
        else if(userGender.equals("Nữ")) {
            rb_female.setChecked(true);
        }

        if(userActive)
        {
            rb_active.setChecked(true);
        }
        else
        {
            rb_inactive.setChecked(true);
        }

        btnEditService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtUserPhone.getText().toString();
                String name = edtUserName.getText().toString();
                String dateBirth = edtUserDateBirth.getText().toString();
                String gender = "";
                boolean active;

                if(rb_male.getId() == rdg_gender.getCheckedRadioButtonId())
                {
                    gender = "Nam";
                }
                else if(rb_female.getId() == rdg_gender.getCheckedRadioButtonId()) gender = "Nữ";

                if(rb_active.getId() == rdg_active.getCheckedRadioButtonId())
                {
                    active = true;
                }
                else active = false;
                // Gọi hàm sửa thông tin người dùng
                updateUser(userId, name, phone, dateBirth, gender, active);

            }
        });

    }

    private void updateUser(String userId, String name, String phone, String dateBirth, String gender, boolean active) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.child("name").setValue(name);
        databaseReference.child("dateBirth").setValue(dateBirth);
        databaseReference.child("gender").setValue(gender);
        databaseReference.child("isActive").setValue(active);
        databaseReference.child("phone").setValue(phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditUserActivity.this, "User information updated successfully", Toast.LENGTH_SHORT).show();
                        // Trả về kết quả thành công nếu cần
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditUserActivity.this, "Failed to update user information", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
