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
import com.example.salepoint.util.Utils;

import java.util.List;

public class DetailReceiptAdapter extends RecyclerView.Adapter<DetailReceiptAdapter.DetailReceiptViewHolder> {

    private Context mContext;
    private List<DetailReceipt> mDetailReceiptList;

    public DetailReceiptAdapter(Context context, List<DetailReceipt> detailReceiptList) {
        mContext = context;
        mDetailReceiptList = detailReceiptList;
    }

    @NonNull
    @Override
    public DetailReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_detail_receipt_listview_item, parent, false);
        return new DetailReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailReceiptViewHolder holder, int position) {
        holder.bind(mDetailReceiptList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDetailReceiptList.size();
    }

    public class DetailReceiptViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewServiceName;
        private TextView textViewQuantity;
        private TextView textViewUnitPrice;

        public DetailReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewServiceName = itemView.findViewById(R.id.textView28);
            textViewQuantity = itemView.findViewById(R.id.textView29);
            textViewUnitPrice = itemView.findViewById(R.id.textView30);
        }

        public void bind(DetailReceipt detailReceipt) {
            textViewServiceName.setText(detailReceipt.getItem());
            textViewQuantity.setText(String.valueOf(detailReceipt.getQuantity()));
            textViewUnitPrice.setText(Utils.convertToVND(detailReceipt.getPrice()));
        }
    }
}
