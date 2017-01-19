package ua.nick.milkcost.model;

import ua.nick.milkcost.utils.ReadExcelFileUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.time.YearMonth;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "costs")
public abstract class CostStructure {

    public YearMonth period;
    public TypeCosts type;
    public String fileNamePath;
    public Set<String> accounts;
    public Map<String, Double> mapCosts;
    public Set<Cost> costs;
    public ReadExcelFileUtil readXls;
    public List<File> newFiles;

    abstract Set<Cost> createSetCosts();

    @Id
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

    public List<File> getNewFiles() {
        return newFiles;
    }

    public void setNewFiles(List<File> newFiles) {
        this.newFiles = newFiles;
    }

    public Set<Cost> mergeCostSets(Set<Cost> costs, Set<Cost> addCosts) { //get costs original, addCosts copy

        if (addCosts.size() > 0) {
            if (costs.size() > 0)
                costs = additionCostSets(costs, addCosts);
            else
                costs = addCosts;
        }
        return costs;
    }

    private Set<Cost> additionCostSets(Set<Cost> costs, Set<Cost> addCosts) { //get costs original, addCosts copy

        for (Cost cost1 : costs) {
            String name = cost1.getItem();

            Iterator<Cost> iterator = addCosts.iterator();
            boolean continueIterator = true;
            while (continueIterator && iterator.hasNext()) {
                Cost cost2 = iterator.next();
                if (name.equals(cost2.getItem())) {
                    cost1.addSum(cost2.getSum());
                    addCosts.remove(cost2);
                    continueIterator = false;
                }
            }
        }

        if (addCosts.size() > 0)
            costs.addAll(addCosts);

        return costs;
    }

    /*public boolean addCosts(Cost... args) {
        boolean result = false;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                result = costs.add(args[i]);
                if (!result)
                    return result;
            }
        }
        return result;
    }

    private boolean addCost(Cost newCost) {
        for (Cost cost : costs) {
            if (newCost.getItem().equals(cost.getItem())) {
                cost.addSum(newCost.getSum());
                return true;
            }
        }
        return costs.add(newCost);
    }*/
}
