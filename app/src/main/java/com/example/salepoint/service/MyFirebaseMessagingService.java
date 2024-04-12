package com.example.salepoint.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.salepoint.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Kiểm tra nếu thông báo có thông tin nội dung
        if (remoteMessage.getNotification() != null && remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            System.out.println(title);
            // Thực hiện hành động hiển thị thông báo
            showNotification(getApplicationContext(), title, body);
        }
    }

    private void showNotification(Context context, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = new Random().nextInt(1000);

        // Kiểm tra nếu thiết bị đang chạy Android 8.0 trở lên, cần tạo Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(R.string.default_notification_channel_id), "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.notification) // Thiết lập icon cho thông báo
                .setContentTitle(title) // Thiết lập tiêu đề cho thông báo
                .setContentText(body) // Thiết lập nội dung cho thông báo
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); // Thiết lập mức độ ưu tiên của thông báo

        // Hiển thị thông báo
        notificationManager.notify(notificationId, builder.build());
    }
}