package ua.nick.milkcost.model;

import ua.nick.milkcost.utils.ReadExcelFileUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "structures")
public abstract class CostStructure {

    public Long id;
    public Date date;
    public TypeCosts typeCosts;
    public String fileNamePath;
    public Set<String> accounts;
    public Map<String, Double> mapCosts;
    public Set<Cost> costs;
    public ReadExcelFileUtil readXls = new ReadExcelFileUtil();
    //public List<File> newFiles;

    abstract Set<Cost> createSetCosts();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TypeCosts getTypeCosts() {
        return typeCosts;
    }

    public void setTypeCosts(TypeCosts typeCosts) {
        this.typeCosts = typeCosts;
    }

    @ManyToMany
    @JoinTable(name = "structures_costs", joinColumns = @JoinColumn(name = "structure_id"),
            inverseJoinColumns = @JoinColumn(name = "cost_id"))
    public Set<Cost> getCosts() {
        return costs;
    }

    public void setCosts(Set<Cost> costs) {
        this.costs = costs;
    }

    /*public List<File> getNewFiles() {
        return newFiles;
    }

    public void setNewFiles(List<File> newFiles) {
        this.newFiles = newFiles;
    }*/

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
