package ua.nick.milkcost.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "costs")
public class CostStructure {

    private Long id;

    private Date monthYear;

    private double costGas;
    private double costRepairs;
    private double costMaterials;
    private double costOther;
    private double costFood;
    private double costElecticity;
    private double costSalary;
    private double costPayrollTax;
    private double costAmortization;
    private double costServices;

    private TypeCost typeCost;

    public CostStructure() {
    }

    public CostStructure(Date monthYear, double costGas, double costRepairs, double costMaterials, double costOther, double costFood, double costElecticity, double costSalary, double costPayrollTax, double costAmortization, double costServices, TypeCost typeCost) {
        this.monthYear = monthYear;
        this.costGas = costGas;
        this.costRepairs = costRepairs;
        this.costMaterials = costMaterials;
        this.costOther = costOther;
        this.costFood = costFood;
        this.costElecticity = costElecticity;
        this.costSalary = costSalary;
        this.costPayrollTax = costPayrollTax;
        this.costAmortization = costAmortization;
        this.costServices = costServices;
        this.typeCost = typeCost;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(Date monthYear) {
        this.monthYear = monthYear;
    }

    public double getCostGas() {
        return costGas;
    }

    public void setCostGas(double costGas) {
        this.costGas = costGas;
    }

    public double getCostRepairs() {
        return costRepairs;
    }

    public void setCostRepairs(double costRepairs) {
        this.costRepairs = costRepairs;
    }

    public double getCostMaterials() {
        return costMaterials;
    }

    public void setCostMaterials(double costMaterials) {
        this.costMaterials = costMaterials;
    }

    public double getCostOther() {
        return costOther;
    }

    public void setCostOther(double costOther) {
        this.costOther = costOther;
    }

    public double getCostFood() {
        return costFood;
    }

    public void setCostFood(double costFood) {
        this.costFood = costFood;
    }

    public double getCostElecticity() {
        return costElecticity;
    }

    public void setCostElecticity(double costElecticity) {
        this.costElecticity = costElecticity;
    }

    public double getCostSalary() {
        return costSalary;
    }

    public void setCostSalary(double costSalary) {
        this.costSalary = costSalary;
    }

    public double getCostPayrollTax() {
        return costPayrollTax;
    }

    public void setCostPayrollTax(double costPayrollTax) {
        this.costPayrollTax = costPayrollTax;
    }

    public double getCostAmortization() {
        return costAmortization;
    }

    public void setCostAmortization(double costAmortization) {
        this.costAmortization = costAmortization;
    }

    public double getCostServices() {
        return costServices;
    }

    public void setCostServices(double costServices) {
        this.costServices = costServices;
    }

    public TypeCost getTypeCost() {
        return typeCost;
    }

    public void setTypeCost(TypeCost typeCost) {
        this.typeCost = typeCost;
    }
}
