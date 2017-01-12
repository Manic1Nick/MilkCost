package ua.nick.milkcost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.nick.milkcost.model.Constants;
import ua.nick.milkcost.model.Cost;
import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.TypeCosts;
import ua.nick.milkcost.repository.CostStructureRepository;
import ua.nick.milkcost.utils.ReadExcelFile;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service(value = "service")
public class CostServiceImpl implements CostService {

    @Autowired
    private CostStructureRepository costStructureRepository;

    public CostServiceImpl() {
    }

    @Override
    public void createCostStructures() {
        Set<String> accounts = ReadExcelFile.createAccountsListFromExcelData(
                Constants.ACCOUNTS_PROPERTY_EXCEL_FILE_LOCATION_PATH);
        String costDirectFileName = Constants.COST_DIRECT_EXCEL_FILE_LOCATION_PATH;
        String costOverheadFileName = Constants.COST_OVERHEAD_EXCEL_FILE_LOCATION_PATH;
        String additionalFileName = Constants.ADDITIONAL_EXCEL_FILE_LOCATION_PATH;

        Map<String, Double> mapCostsDirect = ReadExcelFile.getMapFromRows(accounts, costDirectFileName);
        Map<String, Double> mapCostsOverhead = ReadExcelFile.getMapFromRows(accounts, costOverheadFileName);
        Map<String, Double> mapCostsAdditional = ReadExcelFile.getMapFromColumns("COSTS", additionalFileName);

        String shortName = costDirectFileName.substring(
                costDirectFileName.lastIndexOf("/"), costDirectFileName.lastIndexOf("."));
        YearMonth period = getMonthYearFromFileName(shortName);

        CostStructure costsDirect =
                createNewCostStructure(period, TypeCosts.DIRECT, mapCostsDirect);
        CostStructure costsOverhead =
                createNewCostStructure(period, TypeCosts.OVERHEAD, mapCostsOverhead);
        CostStructure costsAdditional =
                createNewCostStructure(period, TypeCosts.ADDITIONAL, mapCostsAdditional);

        CostStructure costsDirectWithOverheadWithAdditional =
                mergeCostStructures(TypeCosts.TOTAL, costsDirect, costsOverhead, costsAdditional);

        saveNewCostStructure(costsDirectWithOverheadWithAdditional);
    }

    @Override
    public CostStructure saveNewCostStructure(CostStructure costStructure) {
        return costStructureRepository.save(costStructure);
    }

    @Override
    public CostStructure getCostStructure(TypeCosts typeCost, Date monthYear) {
        return costStructureRepository.findByTypeCostAndDate(typeCost, monthYear);
    }

    @Transactional
    private CostStructure createNewCostStructure(YearMonth mmYYYY, TypeCosts type, Map<String, Double> map) {

        Set<Cost> costs = new HashSet<>();
        for (String item : map.keySet())
            costs.add(new Cost(item, map.get(item)));

        return new CostStructure(mmYYYY, type, costs);
    }

    @Transactional
    private CostStructure mergeCostStructures(TypeCosts type, CostStructure... args) {

        CostStructure costStructure = null;

        if (args.length > 0 && type != null) {
            costStructure = new CostStructure(args[0].getPeriod(), type, args[0].getCosts());
            Set<Cost> costs = costStructure.getCosts();

            for (int i = 0; i < args.length; i++) {
                Set<Cost> subCosts = args[i].getCosts();
                mergeCostSets(costs, subCosts);
            }
        }

        return costStructure;
    }

    @Transactional
    private Set<Cost> mergeCostSets(Set<Cost> costs, Set<Cost> addCosts) {

        int size1 = costs.size();
        int size2 = addCosts.size();

        if (size1 > 0 && size2 == 0)
            return costs;

        if (size1 == 0 && size2 > 0)
            return addCosts;

        Set<Cost> mergedCosts = new HashSet<>(costs);

        if (size1 > 0 && size2 > 0)	{
            mergedCosts.addAll(addCosts);

            for (Cost mergedCost : mergedCosts) {
                String name = mergedCost.getItem();
                Cost cost1 = costs.stream()
                        .filter(cost -> name.equals(cost.getItem())).findAny().orElse(null);
                Cost cost2 = addCosts.stream()
                        .filter(cost -> name.equals(cost.getItem())).findAny().orElse(null);

                if (cost1 != null && cost2 != null)
                    mergedCost.setSum(cost1.getSum() + cost2.getSum());
            }
        }

        return mergedCosts;
    }

    @Transactional
    private boolean addCostsToCostStructures(CostStructure costStructure, Cost... args) {

        if (args.length >= 0) {
            for (int i = 0; i < args.length; i++)
                costStructure.addCost(args[i]);

            return true;
        }

        return false;
    }

    @Transactional
    private YearMonth getMonthYearFromFileName(String fileName) {

        YearMonth mmYYYY = null;

        String[] dateArray = fileName.split("_");

        if (dateArray.length >= 3) {
            String date = dateArray[dateArray.length-1] + "." + dateArray[dateArray.length-2];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.yyyy");
            mmYYYY = YearMonth.parse(date, formatter);
        }

        return mmYYYY;
    }



}
