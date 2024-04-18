package com.example.salepoint.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.DetailReceipt;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.util.Utils;

import java.util.List;

public class DetailHistoryAdapter extends RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder> {
    Receipt receipt;
    private List<DetailReceipt> detailReceiptList;

    public DetailHistoryAdapter(Receipt receipt) {
        this.receipt = receipt;
    }

//    public DetailHistoryAdapter(List<DetailReceipt> detailReceiptList) {
//        this.detailReceiptList = detailReceiptList;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_detail_history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHistoryAdapter.ViewHolder holder, int position) {
        detailReceiptList = receipt.getDetailReceipt();

        holder.textViewName.setText(detailReceiptList.get(position).getItem());
        holder.textViewQuantity.setText(String.valueOf("x " + detailReceiptList.get(position).getQuantity()));
        holder.textViewPrice.setText(String.valueOf(Utils.convertToVND(detailReceiptList.get(position).getPrice()) + " đ"));
    }

    @Override
    public int getItemCount() {
        return receipt.getDetailReceipt().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewQuantity;
        TextView textViewPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.txtViewName);
            textViewQuantity = itemView.findViewById(R.id.txtViewQuantity);
            textViewPrice = itemView.findViewById(R.id.txtViewPrice);
        }
    }
}
