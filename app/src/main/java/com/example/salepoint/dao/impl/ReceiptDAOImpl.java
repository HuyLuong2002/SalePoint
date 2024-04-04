package com.example.salepoint.dao.impl;

import com.example.salepoint.RetrofitClient;
import com.example.salepoint.dao.IReceiptDAO;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.response.PointListResponse;
import com.example.salepoint.response.PointResponse;
import com.example.salepoint.response.ReceiptCarInfoResponse;
import com.example.salepoint.response.ReceiptResponse;

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
    public Call<PointListResponse> getAllPoints() {
        return apiService.getAllPoints();
    }

    @Override
    public Call<ReceiptResponse> getAllReceiptForManager() {
        return apiService.getAllReceiptForManager();
    }

    @Override
    public Call<Void> createReceipt(Receipt receipt) {
        return apiService.createReceipt(receipt);
    }

    @Override
    public Call<Void> removeReceipt(String serviceId) {
        return apiService.removeReceipt(serviceId);
    }
}
