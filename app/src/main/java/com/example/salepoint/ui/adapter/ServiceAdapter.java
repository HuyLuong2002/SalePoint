package com.example.salepoint.ui.adapter;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.dao.impl.ServiceDAOImpl;
import com.example.salepoint.model.Service;
import com.example.salepoint.response.ServiceResponse;
import com.example.salepoint.server.AddServiceActivity;
import com.example.salepoint.server.ServiceActivity;
import com.example.salepoint.util.Utils;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
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

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        private ServiceDAOImpl         // Khởi tạo ServiceDAOImpl
                serviceDAO = new ServiceDAOImpl();
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
                        Intent intent = new Intent(itemView.getContext(), AddServiceActivity.class);
                        intent.putExtra("mode", "edit");
                        intent.putExtra("serviceId", service.getId());
                        intent.putExtra("serviceName", service.getName());
                        intent.putExtra("servicePrice", service.getPrice());
                        intent.putExtra("isActive", service.isActive());
                        itemView.getContext().startActivity(intent);
                    }
                }
            });

            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Service service = serviceList.get(position); // Lấy đối tượng Service tại vị trí được nhấn
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle(R.string.dialog_title);
                        // Add the buttons.
                        builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User taps OK button.
                                removeService(service.getId());
                            }
                        });
                        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancels the dialog.
                                dialog.cancel();
                            }
                        });
                        // Set other dialog properties.

                        // Create the AlertDialog.
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(Service service) {
            nameTextView.setText(service.getName());
            priceTextView.setText(Utils.convertToVND(service.getPrice()) + " vnđ");
            isActiveTextView.setText(service.isActive() ? "Active" : "Inactive");
            // Trong phương thức bind của ReceiptViewHolder
            Drawable drawable;
            if (service.isActive()) {
                drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.remove);
            } else {
                drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.undo);
            }
            removeIcon.setImageDrawable(drawable);
        }

        public void removeService(String serviceID) {
            Call<Void> call = serviceDAO.removeService(serviceID);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Cập nhật dịch vụ thành công
                        // Cập nhật giao diện người dùng nếu cần
                        refreshServiceData();
                        System.out.println("Delete succes");

                    } else {
                        // Xử lý lỗi khi cập nhật dịch vụ
                        System.out.println("Delete fail");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Xử lý lỗi khi gọi API
                }
            });
        }

        public void refreshServiceData() {
            Call<ServiceResponse> call = serviceDAO.getServices();
            call.enqueue(new Callback<ServiceResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                    if (response.isSuccessful()) {
                        List<Service> newServiceList = response.body().getServices();
                        // Cập nhật dữ liệu trong RecyclerView
                        serviceList.clear();
                        serviceList.addAll(newServiceList);
                        notifyDataSetChanged();
                        System.out.println("Data refreshed successfully");
                    } else {
                        System.out.println("Failed to refresh data");
                    }
                }

                @Override
                public void onFailure(Call<ServiceResponse> call, Throwable t) {
                    // Xử lý lỗi khi gọi API
                    System.out.println("Failed to refresh data");
                }
            });
        }

    }
}
