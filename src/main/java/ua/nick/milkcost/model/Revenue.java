package ua.nick.milkcost.model;

import java.util.Date;
import java.util.Map;

public class Revenue {

    private Date month;

    private double revenue;

    private Map revenueStructure;

    public Revenue(double revenue) {
        this.revenue = revenue;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
