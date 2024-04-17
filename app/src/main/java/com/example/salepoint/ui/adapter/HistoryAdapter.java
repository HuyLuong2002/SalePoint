package com.example.salepoint.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.History;
import com.example.salepoint.model.Receipt;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private Context context;
    private List<History> historyList;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_history_list_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = historyList.get(position);

        holder.textViewDate.setText(history.getDate());

        ChildHistoryAdapter childHistoryAdapter = new ChildHistoryAdapter(history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.rcv_nested_rv.setLayoutManager(linearLayoutManager);
        holder.rcv_nested_rv.setAdapter(childHistoryAdapter);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        RecyclerView rcv_nested_rv;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.TextViewDate);

            rcv_nested_rv = itemView.findViewById(R.id.nested_rv);
        }
    }
}
