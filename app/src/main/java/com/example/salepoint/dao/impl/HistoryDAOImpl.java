package com.example.salepoint.dao.impl;

import com.example.salepoint.RetrofitClient;
import com.example.salepoint.dao.IHistoryDAO;
import com.example.salepoint.response.HistoryResponse;

import retrofit2.Call;

public class HistoryDAOImpl implements IHistoryDAO{
    private final IHistoryDAO apiHistory;

    public HistoryDAOImpl() {
        apiHistory = RetrofitClient.getClient().create(IHistoryDAO.class);
    }

    @Override
    public Call<HistoryResponse> getHistoryByUserId(String userId) {
        return apiHistory.getHistoryByUserId(userId);
    }
}
