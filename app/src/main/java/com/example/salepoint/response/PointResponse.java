package com.example.salepoint.response;

import com.example.salepoint.model.Point;

public class PointResponse {
    private boolean success;

    private Point pointData;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Point getPointData() {
        return pointData;
    }

    public void setPointData(Point pointData) {
        this.pointData = pointData;
    }
}
