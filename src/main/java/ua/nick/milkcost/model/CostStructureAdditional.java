package ua.nick.milkcost.model;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

public class CostStructureAdditional extends CostStructure {

    public CostStructureAdditional(YearMonth period, Set<String> accounts, String filePath) {
        this.period = period;
        this.type = TypeCosts.ADDITIONAL;
        //fileNamePath = Constants.ADDITIONAL_EXCEL_FILE_LOCATION_PATH;
        fileNamePath = filePath;
        this.accounts = accounts;
        this.costs = createSetCosts();
    }

    public Set<Cost> createSetCosts() {
        mapCosts = readXls.getMapFromColumns("COSTS", fileNamePath);
        Set<Cost> costs = new HashSet<>();
        for (String item : mapCosts.keySet())
            costs.add(new Cost(item, mapCosts.get(item)));

        return costs;
    }
}
