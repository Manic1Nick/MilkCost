package ua.nick.milkcost.model;

import java.util.HashSet;
import java.util.Set;

public class CostStructureTotal extends CostStructure {

    private CostStructure[] costStructures;

    public CostStructureTotal(CostStructure... args) {
        this.period = args[0].getPeriod();
        this.type = TypeCosts.TOTAL;
        fileNamePath = "";
        this.costStructures = args;
        this.costs = createSetCosts();
    }

    public Set<Cost> createSetCosts() {
        Set<Cost> costs = new HashSet<>(costStructures[0].getCosts());
        for (int i = 1; i < costStructures.length; i++) {
            Set<Cost> subCosts = new HashSet<>(costStructures[i].getCosts()); //create new copy
            costs = mergeCostSets(costs, subCosts); //maybe need without "costs = "
        }
        return costs;
    }
}
