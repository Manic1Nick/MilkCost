package ua.nick.milkcost.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "structures_overhead")
public class CostStructureOverhead extends CostStructure {

    public CostStructureOverhead(Date date, Set<String> accounts, String filePath) {
        this.date = date;
        this.typeCosts = TypeCosts.OVERHEAD;
        fileNamePath = filePath;
        this.accounts = accounts;
        this.costs = createSetCosts();
    }

    public Set<Cost> createSetCosts() {
        mapCosts = readXls.getMapFromRows(accounts, fileNamePath);
        Set<Cost> costs = new HashSet<>();
        for (String item : mapCosts.keySet())
            costs.add(new Cost(item, mapCosts.get(item)));

        return costs;
    }
}

