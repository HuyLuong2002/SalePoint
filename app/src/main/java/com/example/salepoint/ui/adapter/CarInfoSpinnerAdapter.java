package com.example.salepoint.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.salepoint.model.CarInfo;
import com.example.salepoint.server.PaymentActivity;

import java.util.List;

public class CarInfoSpinnerAdapter extends ArrayAdapter<CarInfo> {

    private List<CarInfo> carInfoList;
    private LayoutInflater inflater;

    public CarInfoSpinnerAdapter(Context context, int resource, List<CarInfo> carInfoList) {
        super(context, resource, carInfoList);
        this.carInfoList = carInfoList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView textView = view.findViewById(android.R.id.text1);
        CarInfo carInfo = carInfoList.get(position);
        String displayText = carInfo.getName() + " - " + carInfo.getLicense_plate() + " - " + PaymentActivity.customer.getPhone();
        textView.setText(displayText);
        return view;
    }
}
