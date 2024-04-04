package com.example.salepoint.response;

import com.example.salepoint.model.Point;

import java.util.List;

public class PointListResponse {
    private boolean success;

    private List<Point> Points;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Point> getPointData() {
        return Points;
    }

    public void setPointData(List<Point> pointData) {
        this.Points = pointData;
    }
}
