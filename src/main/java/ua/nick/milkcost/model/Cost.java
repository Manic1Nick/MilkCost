package ua.nick.milkcost.model;

import javax.persistence.*;

@Entity
@Table(name = "costs")
public class Cost {

    private Long id;
    private Long periodId;
    private TypeCosts typeCosts;
    private NameCosts nameCosts;
    private double sum;

    private String color;
    private int share;

    public Cost() {
    }

    public Cost(Long periodId, NameCosts nameCosts, TypeCosts typeCosts, double sum) {
        this.periodId = periodId;
        this.typeCosts = typeCosts;
        this.nameCosts = nameCosts;
        this.sum = sum;
    }

    public Cost(NameCosts nameCosts, TypeCosts typeCosts, double sum) {
        this.typeCosts = typeCosts;
        this.nameCosts = nameCosts;
        this.sum = sum;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public NameCosts getNameCosts() {
        return nameCosts;
    }

    public void setNameCosts(NameCosts nameCosts) {
        this.nameCosts = nameCosts;
    }

    public TypeCosts getTypeCosts() {
        return typeCosts;
    }

    public void setTypeCosts(TypeCosts typeCosts) {
        this.typeCosts = typeCosts;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Transient
    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    @Transient
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Cost add(Cost cost) {
        if (cost != null)
            this.sum += cost.getSum();

        return this;
    }

    public Cost addSum(double sum) {
        this.sum += sum;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cost cost = (Cost) o;

        if (periodId != null ? !periodId.equals(cost.periodId) : cost.periodId != null) return false;
        if (typeCosts != cost.typeCosts) return false;
        return nameCosts == cost.nameCosts;

    }

    @Override
    public int hashCode() {
        int result = typeCosts != null ? typeCosts.hashCode() : 0;
        result = 31 * result + (nameCosts != null ? nameCosts.hashCode() : 0);
        return result;
    }
}
