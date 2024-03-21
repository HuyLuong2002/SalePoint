package com.example.salepoint.dao.impl;

import com.example.salepoint.RetrofitClient;
import com.example.salepoint.dao.IServiceDAO;
import com.example.salepoint.model.Service;
import com.example.salepoint.response.ServiceResponse;

import retrofit2.Call;

public class ServiceDAOImpl implements IServiceDAO {

    private final IServiceDAO apiService;

    public ServiceDAOImpl() {
        apiService = RetrofitClient.getClient().create(IServiceDAO.class);
    }
    @Override
    public Call<ServiceResponse> getServices() {
        return apiService.getServices();
    }

    @Override
    public Call<ServiceResponse> getClientServices() {
        return apiService.getClientServices();
    }

    @Override
    public Call<Void> createService(Service newService) {
        return apiService.createService(newService);
    }

    @Override
    public Call<Void> updateService(String serviceId, Service updatedService) {
        return apiService.updateService(serviceId, updatedService);
    }

    @Override
    public Call<Void> removeService(String serviceId) {
        return apiService.removeService(serviceId);
    }
}
