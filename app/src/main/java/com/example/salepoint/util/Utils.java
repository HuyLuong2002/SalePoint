package com.example.salepoint.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.salepoint.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Utils {

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static String convertToVND(int amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    // Hàm để chuyển đổi số điện thoại về dạng bắt đầu bằng số 0
    public static String convertToZeroStartPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("+84")) {
            // Nếu số điện thoại bắt đầu bằng +84, thay thế +84 bằng 0
            return "0" + phoneNumber.substring(3);
        } else {
            // Nếu không bắt đầu bằng +84, không cần thay đổi, trả về số điện thoại ban đầu
            return phoneNumber;
        }
    }

}
