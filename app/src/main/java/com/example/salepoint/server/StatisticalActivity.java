package com.example.salepoint.server;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.salepoint.R;
import com.example.salepoint.dao.impl.ReceiptDAOImpl;
import com.example.salepoint.dao.impl.StatisticalDAOImpl;
import com.example.salepoint.model.Point;
import com.example.salepoint.model.Revenue;
import com.example.salepoint.model.RevenueByMonth;
import com.example.salepoint.model.User;
import com.example.salepoint.model.UserPoint;
import com.example.salepoint.response.PointResponse;
import com.example.salepoint.response.Top5CustomerMaxPointResponse;
import com.example.salepoint.response.TotalRevenueResponse;
import com.example.salepoint.util.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticalActivity extends AppCompatActivity {

    private BarChart barChart;

    private StatisticalDAOImpl statisticalDAO = new StatisticalDAOImpl();

    private List<BarEntry> entries;

    private List<RevenueByMonth> revenueByMonths;

    private List<UserPoint> userPoints;

    private TextView totalRevenue, point1, point2, point3, point4, point5,
            customer1, customer2, customer3, customer4, customer5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);

        barChart = findViewById(R.id.bar_chart1);
        totalRevenue = findViewById(R.id.totalRevenue);
        point1 = findViewById(R.id.point1);
        point2 = findViewById(R.id.point2);
        point3 = findViewById(R.id.point3);
        point4 = findViewById(R.id.point4);
        point5 = findViewById(R.id.point5);
        customer1 = findViewById(R.id.customer1);
        customer2 = findViewById(R.id.customer2);
        customer3 = findViewById(R.id.customer3);
        customer4 = findViewById(R.id.customer4);
        customer5 = findViewById(R.id.customer5);

        // Dummy data for demonstration (replace this with your API data later)
        entries = new ArrayList<>();
        revenueByMonths = new ArrayList<>();
        userPoints = new ArrayList<>();

        getTotalRevenue();
        getTop5CustomerMaxPoint();

    }

    private void getTotalRevenue() {
        // Sử dụng ServiceDAOImpl để gọi API
        Call<TotalRevenueResponse> call = statisticalDAO.getTotalRevenue();
        call.enqueue(new Callback<TotalRevenueResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<TotalRevenueResponse> call, Response<TotalRevenueResponse> response) {
                if (response.isSuccessful()) {
                    TotalRevenueResponse totalRevenueResponse = response.body();
                    Revenue revenue = totalRevenueResponse.getRevenue();
                    revenueByMonths = revenue.getRevenueByMonth();
                    totalRevenue.setText(Utils.convertToVND(revenue.getTotalrevenue()) + " vnđ");
                    updateBarChart();
                } else {
                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<TotalRevenueResponse> call, Throwable t) {
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }

    private void getTop5CustomerMaxPoint() {
        // Sử dụng ServiceDAOImpl để gọi API
        Call<Top5CustomerMaxPointResponse> call = statisticalDAO.getTop5CustomerMaxPoint();
        call.enqueue(new Callback<Top5CustomerMaxPointResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Top5CustomerMaxPointResponse> call, Response<Top5CustomerMaxPointResponse> response) {
                if (response.isSuccessful()) {
                    Top5CustomerMaxPointResponse top5CustomerMaxPointResponse = response.body();
                    userPoints = top5CustomerMaxPointResponse.getTopCustomer();
                    updateTop5Customers();

                } else {
                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<Top5CustomerMaxPointResponse> call, Throwable t) {
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }

    private void updateTop5Customers() {
        int size = Math.min(userPoints.size(), 5);

        for (int i = 0; i < size; i++) {
            UserPoint userPoint = userPoints.get(i);
            String customerCode = userPoint.getCustomer();
            // Gọi phương thức để lấy thông tin người dùng từ mã khách hàng
            getUserInfoFromDatabase(customerCode, i);
        }
    }

    private void updateCustomerInfo(User user, int index) {
        switch (index) {
            case 0:
                customer1.setText(user.getName());
                point1.setText(String.valueOf(userPoints.get(index).getTotalPoints()));
                break;
            case 1:
                customer2.setText(user.getName());
                point2.setText(String.valueOf(userPoints.get(index).getTotalPoints()));
                break;
            case 2:
                customer3.setText(user.getName());
                point3.setText(String.valueOf(userPoints.get(index).getTotalPoints()));
                break;
            case 3:
                customer4.setText(user.getName());
                point4.setText(String.valueOf(userPoints.get(index).getTotalPoints()));
                break;
            case 4:
                customer5.setText(user.getName());
                point5.setText(String.valueOf(userPoints.get(index).getTotalPoints()));
                break;
            default:
                break;
        }
    }


    private void getUserInfoFromDatabase(String customerCode, int index) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(customerCode);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    // Cập nhật thông tin người dùng vào TextView tương ứng
                    updateCustomerInfo(user, index);
                } else {
                    // Handle the case where the user does not exist in the database
                    System.out.println("User does not exist in the database");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                System.out.println("Database error: " + databaseError.getMessage());
            }
        });
    }


    private void updateBarChart() {
        entries.clear(); // Clear existing entries

        // Tạo danh sách các doanh thu cho tất cả các tháng, mặc định ban đầu là 0
        List<Integer> allMonthsRevenue = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            allMonthsRevenue.add(0);
        }

        // Điền doanh thu cho các tháng có dữ liệu
        for (RevenueByMonth revenueByMonth : revenueByMonths) {
            // Parse date string to get the month
            String dateString = revenueByMonth.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            Date date;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                continue; // Skip this entry if unable to parse date
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH); // Month is zero-based

            // Thêm doanh thu vào danh sách tương ứng với tháng
            allMonthsRevenue.set(month, revenueByMonth.getTotalRevenue());
        }

        // Thêm dữ liệu từ danh sách doanh thu của tất cả các tháng vào danh sách BarEntry
        for (int i = 0; i < allMonthsRevenue.size(); i++) {
            entries.add(new BarEntry(i + 1, allMonthsRevenue.get(i))); // Tháng bắt đầu từ 1, không phải từ 0
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo tháng");
        dataSet.setColor(Color.rgb(51, 153, 255)); // Set color for the bars

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Customize the appearance of the chart
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return String.valueOf((int) value); // Display month numbers
            }
        });

        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        // Customize Y-axis as needed

        barChart.getDescription().setEnabled(false); // Disable description label
        barChart.setFitBars(true); // Make the bars fit the screen width

        barChart.invalidate(); // Refresh chart
    }

}
