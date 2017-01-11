package ua.nick.milkcost.model;

import java.util.Date;
import java.util.Map;

public class Milk {

    private Date month;

    private double quantityProduce;
    private double quantitySold;
    private Cost cost;
    private Revenue revenue;

    private Map<String, Double> beginStructure;

    public Milk(int quantityProduce, int quantitySold, Cost costTotal, Revenue revenueTotal) {
        this.quantityProduce = quantityProduce;
        this.quantitySold = quantitySold;
        this.cost = costTotal;
        this.revenue = revenueTotal;
    }

    public Milk(Map<String, Double> quantityMap, Cost costTotal, Revenue revenueTotal) {
        for (String key : quantityMap.keySet()) {
            if (key.contains("PRODUCE")) {
                this.quantityProduce = quantityMap.get(key);
            }
            if (key.contains("SOLD")) {
                this.quantitySold = quantityMap.get(key);
            }
        }
        this.cost = costTotal;
        this.revenue = revenueTotal;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public double getQuantityProduce() {
        return quantityProduce;
    }

    public void setQuantityProduce(double quantityProduce) {
        this.quantityProduce = quantityProduce;
    }

    public double getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(double quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost costTotal) {
        this.cost = costTotal;
    }

    public Revenue getRevenue() {
        return revenue;
    }

    public void setRevenue(Revenue revenueTotal) {
        this.revenue = revenueTotal;
    }

    public Map<String, Double> getBeginStructure() {
        return beginStructure;
    }

    public double getCostMilkUnitProduceFull() {
        return (cost.getCostDirect() + cost.getCostOverhead() +
                cost.getCostAdditional()) / quantityProduce;
    }

    public double getCostMilkUnitProduceWithoutAdmin() {
        return (cost.getCostDirect() + cost.getCostOverhead()) / quantityProduce;
    }

    public double getCostMilkUnitProduceDirect() {
        return cost.getCostDirect() / quantityProduce;
    }

    public double getCostMilkUnitSoldWithoutAdmin() {
        return (cost.getCostTotal() - cost.getCostAdmin() +
                beginStructure.get("SUMM_BEGIN")) /
                (quantityProduce + beginStructure.get("QUANT_BEGIN"));
    }

    public double getCostMilkUnitSoldFull() {
        return (cost.getCostTotal() + beginStructure.get("SUMM_BEGIN")) /
                (quantityProduce + beginStructure.get("QUANT_BEGIN"));
    }

    public double getPriceMilkUnit() {
        return revenue.getRevenue() / quantitySold;
    }

    public void setBeginStructure(Map<String, Double> beginStructure) {
        this.beginStructure = beginStructure;
    }

    public double getCostSoldWithoutAdmin() {
        return getCostMilkUnitSoldWithoutAdmin() * quantitySold;
    }
}
