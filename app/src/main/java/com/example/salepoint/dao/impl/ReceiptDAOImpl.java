package com.example.salepoint.dao.impl;

import com.example.salepoint.RetrofitClient;
import com.example.salepoint.dao.IReceiptDAO;
import com.example.salepoint.dao.IServiceDAO;
import com.example.salepoint.model.Point;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.response.PointResponse;

import retrofit2.Call;

public class ReceiptDAOImpl implements IReceiptDAO {

    private final IReceiptDAO apiService;

    public ReceiptDAOImpl() {
        apiService = RetrofitClient.getClient().create(IReceiptDAO.class);
    }

    @Override
    public Call<PointResponse> getPointByUserId(String userId) {
        return apiService.getPointByUserId(userId);
    }

    @Override
    public Call<Void> createReceipt(Receipt receipt) {
        return apiService.createReceipt(receipt);
    }
}
