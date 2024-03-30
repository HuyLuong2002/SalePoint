package com.example.salepoint.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salepoint.R;
import com.example.salepoint.model.User;
import com.example.salepoint.server.EditUserActivity;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_user_listview_item, parent, false);
        return new ViewHolder(view, userList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName, textViewPhone, textViewPoint, textViewPermission, textView5, textView6, textView10;
        private ImageView imageViewEditIcon;
        private ImageView imageViewRemoveIcon;



        public ViewHolder(@NonNull View itemView, List<User> userList) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView3);
            textViewPhone = itemView.findViewById(R.id.textView);
            textViewPoint = itemView.findViewById(R.id.textView2);
            imageViewEditIcon = itemView.findViewById(R.id.editIcon);
            imageViewRemoveIcon = itemView.findViewById(R.id.removeIcon);
            textViewPermission = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            textView10 = itemView.findViewById(R.id.textView10);

            imageViewEditIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        User user = userList.get(position); // Lấy đối tượng Service tại vị trí được nhấn
                        Intent intent = new Intent(itemView.getContext(), EditUserActivity.class);
                        intent.putExtra("userPhone", user.getPhone());
                        intent.putExtra("userName", user.getName());
                        intent.putExtra("userGender", user.getGender());
                        intent.putExtra("userDateBirth", user.getDateBirth());
                        intent.putExtra("userActive", user.getIsActive());
                        intent.putExtra("userId", user.getId());
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
            imageViewRemoveIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(User user) {
            textViewName.setText(user.getName());
            textViewPhone.setText(user.getPhone());
            textViewPoint.setText(String.valueOf(user.getPoint()));
            textViewPermission.setText(user.getIsStaff() ? "Quản lý" : "Khách hàng");
            textView5.setText(user.getGender().equals("") ? "Không có" : user.getGender());
            textView6.setText(user.getDateBirth().equals("") ? "Không có" : user.getDateBirth());
            textView10.setText(user.getIsActive() ? "Còn hoạt động" : "Hết hoạt động");
        }
    }
}