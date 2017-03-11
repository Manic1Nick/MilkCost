package ua.nick.milkcost.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "structures_total")
public class CostStructureTotal extends CostStructure {

    private CostStructure[] costStructures;

    public CostStructureTotal(CostStructure... args) {
        this.date = args[0].getDate();
        this.typeCosts = TypeCosts.TOTAL;
        fileNamePath = "";
        this.costStructures = args;
        this.costs = createSetCosts();
    }

    public Set<Cost> createSetCosts() {
        Set<Cost> costs = new HashSet<>(costStructures[0].getCosts());
        for (int i = 1; i < costStructures.length; i++) {
            Set<Cost> subCosts = new HashSet<>(costStructures[i].getCosts()); //create new copy
            costs = mergeCostSets(costs, subCosts);
        }
        return costs;
    }
}