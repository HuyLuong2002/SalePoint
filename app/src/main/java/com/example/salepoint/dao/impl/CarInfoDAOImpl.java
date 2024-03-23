package com.example.salepoint.dao.impl;

import com.example.salepoint.RetrofitClient;
import com.example.salepoint.dao.ICafInfoDAO;
import com.example.salepoint.dao.IServiceDAO;
import com.example.salepoint.response.CarInfoResponse;
import com.example.salepoint.response.ServiceResponse;

import retrofit2.Call;

public class CarInfoDAOImpl implements ICafInfoDAO {

    private final ICafInfoDAO apiService;

    public CarInfoDAOImpl() {
        apiService = RetrofitClient.getClient().create(ICafInfoDAO.class);
    }
    @Override
    public Call<CarInfoResponse> getCarInfo() {
        return apiService.getCarInfo();
    }

    @Override
    public Call<CarInfoResponse> getCarsOfUser(String userId) {
        return apiService.getCarsOfUser(userId);
    }
}
