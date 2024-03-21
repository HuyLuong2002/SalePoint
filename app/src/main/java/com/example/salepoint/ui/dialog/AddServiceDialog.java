package com.example.salepoint.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.dao.impl.ServiceDAOImpl;
import com.example.salepoint.model.Service;
import com.example.salepoint.response.ServiceResponse;
import com.example.salepoint.server.PaymentActivity;
import com.example.salepoint.server.ServiceActivity;
import com.example.salepoint.ui.adapter.PaymentServiceAdapter;
import com.example.salepoint.ui.adapter.ServiceAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceDialog extends AppCompatDialogFragment {

    private RecyclerView recyclerView;
    private PaymentServiceAdapter adapter;

    private ServiceDAOImpl serviceDAO;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_service, null);
        // Khởi tạo ServiceDAOImpl
        serviceDAO = new ServiceDAOImpl();
        recyclerView = view.findViewById(R.id.recyclerView);

        setDataOnListView();

        builder.setView(view)
                .setTitle(R.string.servide_dialog_title)
                .setPositiveButton(R.string.dialog_confirm, (dialogInterface, i) -> {

                    Intent intent = new Intent(view.getContext(), PaymentActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.dialog_cancel, (dialogInterface, i) -> {
                    if(PaymentActivity.selectedServiceList.isEmpty())
                    {
                        PaymentActivity.selectedServiceList.clear();
                    }

                    dialogInterface.cancel();
                    Intent intent = new Intent(view.getContext(), PaymentActivity.class);
                    startActivity(intent);
                });

        return builder.create();
    }


    public void setDataOnListView() {
        // Sử dụng ServiceDAOImpl để gọi API
        Call<ServiceResponse> call = serviceDAO.getClientServices();
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if (response.isSuccessful()) {
                    ServiceResponse serviceResponse = response.body();
                    List<Service> serviceList = serviceResponse.getServices();
                    // Update RecyclerView with the new data
                    adapter = new PaymentServiceAdapter(serviceList);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                } else {
                    // Handle error
                    System.out.println("failed");
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                // Handle failure
                System.out.println(t.getMessage());
            }
        });
    }
}
