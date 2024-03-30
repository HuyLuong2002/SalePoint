package com.example.salepoint.ui.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.LoginActivity;
import com.example.salepoint.R;
import com.example.salepoint.dao.impl.CarInfoDAOImpl;
import com.example.salepoint.dao.impl.ServiceDAOImpl;
import com.example.salepoint.model.CarInfo;
import com.example.salepoint.model.Service;
import com.example.salepoint.response.CarInfoResponse;
import com.example.salepoint.response.ServiceResponse;
import com.example.salepoint.server.PaymentActivity;
import com.example.salepoint.server.ServiceActivity;
import com.example.salepoint.ui.adapter.PaymentServiceAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarInfoDialog extends AppCompatDialogFragment {

    private CarInfoDAOImpl carInfoDAO;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_carinfo, null);

        //Khởi tạo CarInfoDAOImpl
        carInfoDAO = new CarInfoDAOImpl();

        TextView textViewCustomer = view.findViewById(R.id.textView4);
        TextView textViewCarName = view.findViewById(R.id.textView8);
        TextView textViewCarCompany = view.findViewById(R.id.textView16);
        TextView textViewCarDivision = view.findViewById(R.id.textView18);
        TextView textViewCarLicensePlate = view.findViewById(R.id.textView20);
        TextView textViewCarSpeedometer = view.findViewById(R.id.textView22);
        TextView textViewCarNumberOfChangeOil = view.findViewById(R.id.textView2);
        textViewCustomer.setText(PaymentActivity.customer.getName());


        builder.setView(view)
                .setTitle(R.string.carinfo_dialog_title)
                .setPositiveButton(R.string.dialog_confirm, (dialogInterface, i) -> {
                    String carName = textViewCarName.getText().toString();
                    String carCompany = textViewCarCompany.getText().toString();
                    int carBlockDivision = Integer.parseInt(textViewCarDivision.getText().toString());
                    String carLicensePlate = textViewCarLicensePlate.getText().toString();
                    int carSpeedometer = Integer.parseInt(textViewCarSpeedometer.getText().toString());
                    int carNumberOfChangeOil = Integer.parseInt(textViewCarNumberOfChangeOil.getText().toString());
                    String userId = PaymentActivity.customer.getId();
                    CarInfo carInfo = new CarInfo(carName, carCompany, carBlockDivision, carLicensePlate, carSpeedometer, carNumberOfChangeOil, userId);
                    createCarInfo(carInfo);

                })
                .setNegativeButton(R.string.dialog_cancel, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

        return builder.create();
    }

    public void createCarInfo(CarInfo carInfo) {
        // Sử dụng ServiceDAOImpl để gọi API
        Call<Void> call = carInfoDAO.createCarInfo(carInfo);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    refreshServiceData(carInfo.getUser());

                } else {
                    // Handle error
                    System.out.println("Add failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }

    public void refreshServiceData(String userId) {
        Call<CarInfoResponse> call = carInfoDAO.getCarsOfUser(userId);
        call.enqueue(new Callback<CarInfoResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CarInfoResponse> call, Response<CarInfoResponse> response) {
                if (response.isSuccessful()) {
                    List<CarInfo> newCarInfoList = response.body().getCarInfos();
                    // Cập nhật dữ liệu trong RecyclerView
                    PaymentActivity.carInfoList.clear();
                    PaymentActivity.carInfoList.addAll(newCarInfoList);
                    PaymentActivity.carInfoSpinnerAdapter.notifyDataSetChanged();
                } else {
                    System.out.println("Failed to refresh data");
                }
            }

            @Override
            public void onFailure(Call<CarInfoResponse> call, Throwable t) {
                // Xử lý lỗi khi gọi API
                System.out.println("Failed to refresh data");
            }
        });
    }

}
