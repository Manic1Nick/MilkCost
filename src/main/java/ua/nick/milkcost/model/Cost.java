package ua.nick.milkcost.model;

import javax.persistence.*;

@Entity
@Table(name = "costs")
public class Cost {

    private Long id;
    private String item;
    private double sum;

    public Cost() {
    }

    public Cost(String item, double sum) {
        this.item = item;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void addSum(double sum) {
        this.sum += sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cost cost = (Cost) o;

        if (Double.compare(cost.sum, sum) != 0) return false;

        return item != null ? item.equals(cost.item) : cost.item == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = item != null ? item.hashCode() : 0;
        temp = Double.doubleToLongBits(sum);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
