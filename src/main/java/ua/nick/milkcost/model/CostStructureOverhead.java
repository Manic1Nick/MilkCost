package ua.nick.milkcost.model;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

public class CostStructureOverhead extends CostStructure {

    public CostStructureOverhead(YearMonth period, Set<String> accounts, String filePath) {
        this.period = period;
        this.type = TypeCosts.OVERHEAD;
        //fileNamePath = Constants.COST_OVERHEAD_EXCEL_FILE_LOCATION_PATH;
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

