package ua.nick.milkcost.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.YearMonth;
import java.util.Set;

@Entity
@Table(name = "costs")
public class CostStructure {

    private YearMonth period;
    //http://stackoverflow.com/questions/23800477/java-8-time-api-how-to-parse-string-of-format-mm-yyyy-to-localdate

    private TypeCosts type;

    //one to many
    private Set<Cost> costs;

    public CostStructure() {
    }

    public CostStructure(TypeCosts type) {
        this.type = type;
    }

    public CostStructure(YearMonth mmYYYY, TypeCosts type) {
        this.period = mmYYYY;
        this.type = type;
    }

    public CostStructure(Set<Cost> costs) {
        this.costs = costs;
    }

    public CostStructure(YearMonth mmYYYY, TypeCosts type, Set<Cost> costs) {
        this.period = mmYYYY;
        this.type = type;
        this.costs = costs;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    public TypeCosts getType() {
        return type;
    }

    public void setType(TypeCosts type) {
        this.type = type;
    }

    public Set<Cost> getCosts() {
        return costs;
    }

    public void setCosts(Set<Cost> costs) {
        this.costs = costs;
    }

    public boolean addCost(Cost newCost) {
        for (Cost cost : costs) {
            if (newCost.getItem().equals(cost.getItem())) {
                cost.setSum(cost.getSum() + newCost.getSum());
                return true;
            }
        }
        return costs.add(newCost);
    }
}
