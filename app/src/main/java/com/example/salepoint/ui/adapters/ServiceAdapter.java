package com.example.salepoint.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.Service;
import com.example.salepoint.server.AddServiceActivity;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>{
    private List<Service> serviceList;

    // Constructor
    public ServiceAdapter(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_service_listview_item, parent, false);
        return new ServiceViewHolder(view, serviceList);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView priceTextView;
        private TextView isActiveTextView;
        private ImageView editIcon;

        private ImageView removeIcon;
        private List<Service> serviceList; // Thêm biến serviceList

        public ServiceViewHolder(@NonNull View itemView, List<Service> serviceList) {
            super(itemView);
            this.serviceList = serviceList; // Khởi tạo serviceList
            nameTextView = itemView.findViewById(R.id.textView28);
            priceTextView = itemView.findViewById(R.id.textView29);
            isActiveTextView = itemView.findViewById(R.id.textView30);
            editIcon = itemView.findViewById(R.id.editIcon);
            removeIcon = itemView.findViewById(R.id.removeIcon);

            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Service service = serviceList.get(position); // Lấy đối tượng Service tại vị trí được nhấn
                        // Tạo một bundle và đưa đối tượng Service vào
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("service", (Serializable) service);
                        Intent intent = new Intent(itemView.getContext(), AddServiceActivity.class);
                        intent.putExtra("mode", "edit");
                        intent.putExtras(bundle); // Truyền đối tượng Service vào intent
                        itemView.getContext().startActivity(intent);
                    }

                }
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(Service service) {
            nameTextView.setText(service.getName());
            priceTextView.setText(service.getPrice() + " vnđ");
            isActiveTextView.setText(service.isActive() ? "Active" : "Inactive");
        }
    }
}
