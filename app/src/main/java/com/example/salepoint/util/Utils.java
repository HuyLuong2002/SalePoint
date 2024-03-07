package com.example.salepoint.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}
