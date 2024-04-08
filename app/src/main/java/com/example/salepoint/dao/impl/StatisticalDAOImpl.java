package com.example.salepoint.dao.impl;

import com.example.salepoint.RetrofitClient;
import com.example.salepoint.dao.IServiceDAO;
import com.example.salepoint.dao.IStatisticalDAO;
import com.example.salepoint.response.Top5CustomerMaxPointResponse;
import com.example.salepoint.response.TotalRevenueResponse;

import retrofit2.Call;

public class StatisticalDAOImpl implements IStatisticalDAO {

    private final IStatisticalDAO apiService;

    public StatisticalDAOImpl() {
        apiService = RetrofitClient.getClient().create(IStatisticalDAO.class);
    }

    @Override
    public Call<TotalRevenueResponse> getTotalRevenue() {
        return apiService.getTotalRevenue();
    }

    @Override
    public Call<Top5CustomerMaxPointResponse> getTop5CustomerMaxPoint() {
        return apiService.getTop5CustomerMaxPoint();
    }
}
