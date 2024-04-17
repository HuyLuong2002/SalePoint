package com.example.salepoint.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.DetailReceipt;
import com.example.salepoint.model.History;
import com.example.salepoint.model.Receipt;

import java.util.List;

public class ChildHistoryAdapter extends RecyclerView.Adapter<ChildHistoryAdapter.ViewHolder> {

    History history;
    List<Receipt> receiptList;

    public ChildHistoryAdapter(History historyList) {
        this.history = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_history_child_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        History history = historyList.get(position);

//        receiptList = history.getReceipts();
//        for (Receipt receipt : receiptList) {
//            holder.textViewCreateAt.setText(receipt.getCreatedAt());
//            holder.textViewDiemTichLuy.setText(String.valueOf((receipt.getExchange_points() + receipt.getTotalPrice()) / 1000)); // Lấy giá của detailReceipt đầu tiên
//        }
        holder.textViewCreateAt.setText(history.getReceipts().get(position).getCreatedAt());
        int exchange_points = history.getReceipts().get(position).getExchange_points();
        if(exchange_points > 0) {
            holder.textViewDiemQuyDoi.setText(String.valueOf("- " + history.getReceipts().get(position).getExchange_points()));
        } else {
            holder.textViewDiemQuyDoi.setText("0");
        }
        holder.textViewDiemTichLuy.setText(String.valueOf("+ " + (history.getReceipts().get(position).getExchange_points() + history.getReceipts().get(position).getTotalPrice()) / 1000));
    }

    @Override
    public int getItemCount() {
        return history.getReceipts().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCreateAt;
        TextView textViewDiemTichLuy;
        TextView textViewDiemQuyDoi;

        CardView CardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCreateAt = itemView.findViewById(R.id.TextViewCreateAt);
            textViewDiemTichLuy = itemView.findViewById(R.id.TextViewDiemTichLuy);
            textViewDiemQuyDoi = itemView.findViewById(R.id.TextViewDiemQuyDoi);
            CardView = itemView.findViewById(R.id.CardHistoryChild);
        }
    }
}
