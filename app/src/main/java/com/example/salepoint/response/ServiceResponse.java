package com.example.salepoint.response;

import com.example.salepoint.model.Service;

import java.util.List;

public class ServiceResponse {

    private boolean success;
    private List<Service> services;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
