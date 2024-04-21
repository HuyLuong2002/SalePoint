package com.example.salepoint.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.dao.impl.ReceiptDAOImpl;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.response.ReceiptResponse;
import com.example.salepoint.server.ViewSalePointActivity;
import com.example.salepoint.util.Utils;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private Context mContext;
    private List<Receipt> mReceiptList;

    public ReceiptAdapter(Context context, List<Receipt> receiptList) {
        mContext = context;
        mReceiptList = receiptList;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_sale_point_listview_item, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        holder.bind(mReceiptList.get(position));

        // Hiển thị thông tin khách hàng, số điểm quy đổi, thông tin xe, tổng giá tiền, tổng số lượng hoặc bất kỳ thông tin nào khác tương tự ở đây
    }

    @Override
    public int getItemCount() {
        return mReceiptList.size();
    }

    public class ReceiptViewHolder extends RecyclerView.ViewHolder {
        public TextView textView31;
        public TextView textView32;
        public TextView textView29;

        public TextView textView35;

        public TextView textView1;

        public TextView textView3;
        public TextView textView30;

        public ImageView viewMoreIcon;


        private ReceiptDAOImpl         // Khởi tạo ReceiptDAOImpl
                receiptDAO = new ReceiptDAOImpl();


        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);

            textView31 = itemView.findViewById(R.id.textView31);
            textView32 = itemView.findViewById(R.id.textView32);
            textView29 = itemView.findViewById(R.id.textView29);
            textView35 = itemView.findViewById(R.id.textView35);
            textView1 = itemView.findViewById(R.id.textView1);
            textView3 = itemView.findViewById(R.id.textView3);
            textView30 = itemView.findViewById(R.id.textView30);
            viewMoreIcon = itemView.findViewById(R.id.viewMoreIcon);

            viewMoreIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy đối tượng Receipt tương ứng với vị trí hiện tại
                    Receipt selectedReceipt = mReceiptList.get(getAdapterPosition());
                    Intent intent = new Intent(itemView.getContext(), ViewSalePointActivity.class);
                    // Gửi thông tin về hóa đơn qua intent
                    intent.putExtra("selected_receipt", selectedReceipt);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

        private void showDeleteConfirmationDialog(List<Receipt> receiptList) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteSelectedReceipt(mReceiptList);
                }
            });
            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void deleteSelectedReceipt(List<Receipt> receiptList) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Receipt receipt = receiptList.get(position);
                Call<Void> call = receiptDAO.removeReceipt(receipt.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            refreshReceiptData();
                            System.out.println("Delete succes");

                        } else {
                            // Xử lý lỗi khi cập nhật dịch vụ
                            System.out.println("Delete fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xử lý lỗi khi gọi API
                        System.out.println(t.getMessage());
                    }
                });


            }
        }

        public void refreshReceiptData() {
            Call<ReceiptResponse> call = receiptDAO.getAllReceiptForManager();
            call.enqueue(new Callback<ReceiptResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<ReceiptResponse> call, Response<ReceiptResponse> response) {
                    if (response.isSuccessful()) {
                        List<Receipt> newReceiptList = response.body().getReceiptList();
                        // Cập nhật dữ liệu trong RecyclerView
                        mReceiptList.clear();
                        mReceiptList.addAll(newReceiptList);
                        notifyDataSetChanged();
                        System.out.println("Data refreshed successfully");
                    } else {
                        System.out.println("Failed to refresh data");
                    }
                }

                @Override
                public void onFailure(Call<ReceiptResponse> call, Throwable t) {
                    // Xử lý lỗi khi gọi API
                    System.out.println("Failed to refresh data");
                }
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(Receipt receipt) {
            textView31.setText(receipt.getId());
            textView32.setText(receipt.getCustomer());
            textView29.setText(String.valueOf(receipt.getExchange_points()));
            textView35.setText(receipt.getCar_id());
            textView1.setText(Utils.convertToVND(receipt.getTotalPrice()) + " vnđ");
            textView3.setText(String.valueOf(receipt.getTotalQuantity()));
            textView30.setText(receipt.isActive() ? "Active" : "Inactive");
            // Trong phương thức bind của ReceiptViewHolder


        }


    }
}

