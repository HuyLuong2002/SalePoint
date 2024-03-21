package com.example.salepoint.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.Service;
import com.example.salepoint.util.Utils;

import java.util.List;

public class SelectedServiceAdapter extends RecyclerView.Adapter<SelectedServiceAdapter.ViewHolder>{

    private List<Service> selectedServiceList;

    public SelectedServiceAdapter(List<Service> selectedServiceList) {
        this.selectedServiceList = selectedServiceList;
    }
    @NonNull
    @Override
    public SelectedServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_payment_listview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedServiceAdapter.ViewHolder holder, int position) {
        Service item = selectedServiceList.get(position);
        holder.textView28.setText(item.getName());
        holder.textView29.setText(Utils.convertToVND(item.getPrice()));

        // Xử lý sự kiện khi người dùng nhấn vào biểu tượng remove
        holder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                // Xử lý logic khi người dùng nhấn vào biểu tượng remove ở vị trí này
                // Ví dụ: xóa mục khỏi danh sách
                selectedServiceList.remove(selectedServiceList.remove(holder.getAdapterPosition()));
                notifyDataSetChanged(); // Cập nhật RecyclerView
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedServiceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView28, textView29;
        ImageView removeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView28 = itemView.findViewById(R.id.textView28);
            textView29 = itemView.findViewById(R.id.textView29);
            removeIcon = itemView.findViewById(R.id.removeIcon);
        }
    }
}
