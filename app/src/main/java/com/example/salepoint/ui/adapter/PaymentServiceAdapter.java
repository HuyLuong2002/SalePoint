package com.example.salepoint.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.Service;
import com.example.salepoint.server.PaymentActivity;
import com.example.salepoint.ui.dialog.AddServiceDialog;
import com.example.salepoint.util.Utils;

import java.util.List;

public class PaymentServiceAdapter extends RecyclerView.Adapter<PaymentServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;

    public PaymentServiceAdapter(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dialog_listview_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service);

        if (!PaymentActivity.selectedServiceList.isEmpty()) {
            // Kiểm tra xem dịch vụ đã được chọn hay không
            for (Service service1 :
                    PaymentActivity.selectedServiceList) {
                if (service.getId().equals(service1.getId())) {
                    holder.checkBox.setChecked(true);
                    holder.checkBox.setEnabled(false); // Không cho phép người dùng click nếu đã được chọn
                }
            }
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    PaymentActivity.selectedServiceList.add(serviceList.get(holder.getAdapterPosition()));
                } else
                    PaymentActivity.selectedServiceList.remove(serviceList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewServiceName;
        private TextView textViewServicePrice;
        private CheckBox checkBox;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewServiceName = itemView.findViewById(R.id.textView28);
            textViewServicePrice = itemView.findViewById(R.id.textView29);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(Service service) {
            textViewServiceName.setText(service.getName());
            textViewServicePrice.setText(Utils.convertToVND(service.getPrice()));

        }
    }
}
