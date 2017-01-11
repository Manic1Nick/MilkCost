package ua.nick.milkcost.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cost {

    private Date month;

    private double costDirect;
    private double costOverhead;
    private double costAdditional;
    private double costAdmin;

    private Map<String, Double> costStructure;

    public Cost() {
        this.costStructure = new HashMap<>();
    }

    public Cost(Map<String, Double> costDirectStructure,
                Map<String, Double> costOverheadStructure,
                Map<String, Double> costAdditionalStructure) {
        for (Double value : costDirectStructure.values()) {
            this.costDirect += value;
        }
        for (Double value : costOverheadStructure.values()) {
            this.costOverhead += value;
        }
        this.costAdmin = costAdditionalStructure.remove("ADMIN") +
                costAdditionalStructure.remove("K2_ADMIN_KV");

        for (Double value : costAdditionalStructure.values()) {
            this.costAdditional += value;
        }
        this.costStructure = createCostStructure(
                costDirectStructure, costOverheadStructure,
                costAdditionalStructure);
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public double getCostDirect() {
        return costDirect;
    }

    public void setCostDirect(Map<String, Double> costDirectStructure) {
        for (Double value : costDirectStructure.values()) {
            this.costDirect += value;
        }
    }

    public double getCostOverhead() {
        return costOverhead;
    }

    public void setCostOverhead(Map<String, Double> costOverheadStructure) {
        for (Double value : costOverheadStructure.values()) {
            this.costOverhead += value;
        }
    }

    public double getCostAdditional() {
        return costAdditional;
    }

    public void setCostAdditional(Map<String, Double> costAdditionalStructure) {
        for (Double value : costAdditionalStructure.values()) {
            this.costAdditional += value;
        }
    }

    public Map<String, Double> getCostStructure() {
        return costStructure;
    }

    public void setCostStructure(Map<String, Double> costDirectStructure,
                                 Map<String, Double> costOverheadStructure,
                                 Map<String, Double> costAdditionalStructure) {

        this.costStructure = createCostStructure(
                costDirectStructure, costOverheadStructure,
                costAdditionalStructure);
    }

    public double getCostTotal() {
        return costDirect + costOverhead + costAdditional + costAdmin;
    }

    public double getCostAdmin() {
        return costAdmin;
    }

    public void setCostAdmin(double costAdmin) {
        this.costAdmin = costAdmin;
    }

    private Map<String, Double> createCostStructure(Map<String, Double> costDirectStructure,
                                                    Map<String, Double> costOverheadStructure,
                                                    Map<String, Double> costAdditionalStructure) {
        for (String key : costOverheadStructure.keySet()) {
            Double value = costDirectStructure.get(key);
            value = value != null ?
                    value + costOverheadStructure.get(key) : costOverheadStructure.get(key);
            costDirectStructure.put(key, value);
        }
        for (String key : costAdditionalStructure.keySet()) {
            Double value = costDirectStructure.get(key);
            value = value != null ?
                    value + costAdditionalStructure.get(key) : costAdditionalStructure.get(key);
            costDirectStructure.put(key, value);
        }

        return costDirectStructure;
    }
}
