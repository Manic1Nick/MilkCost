package ua.nick.milkcost.service;

import ua.nick.milkcost.model.*;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CostService {

    List<FileCost> saveNewCosts(List<FileCost> newFiles);

    TypeCosts findTypeCostsByString(String typeStr);
    Date getDateFromString(String date);

    List<FileCost> getNewFiles();
    void saveNewFiles(List<FileCost> newFiles);
    List<FileCost> getAllFilesFromDB();
    List<FileCost> getAllFilesWithTypeCostsFromDB();
    List<File> getAllFilesFromWorkFolder();

    List<FileCost> getNewFilesForUpdateDB();
    List<FileCost> saveNewCostsForUpdateDB(List<FileCost> newFiles);

    List<Period> getAllPeriodsFromDB();

    Cost saveNewCost(Cost cost);
    void saveCostsFromFiles(List<FileCost> newFiles);
    List<Cost> getListCostsByPeriodId(Long id);
    Map<String, Double> getMapCostsByListCosts(List<Cost> list);
    Map<String, Double> getMapCostsByPeriodIdAndTypeCosts(Long id, TypeCosts typeCosts);
    Map<String, Double> sumListCostsToMapCosts(List<List<Cost>> lists);
    Map<String, Double> createTotalMapFromMaps(Map<String, Map<String, Double>> mapOfMaps);
    List<Cost> createCostStructure(Period period);
    List<Cost> createTotalCostStructure(List<Period> periods);
    List<Cost> getAllCostsByName(NameCosts nameCosts);
    Cost getCostByPeriodIdAndNameCostsAndTypeCosts(Long periodId, NameCosts nameCosts, TypeCosts typeCosts);

    Period saveNewPeriod(Period period);
    Period getPeriodById(Long id);
    Period getPeriodByYearAndMonth(String year, String month);
    List<Period> getPeriodsByYear(String year);

    int[] createDynamicArrayOfCost(String costName);
    double calculateSumTotalFromListCosts(List<Cost> costs);

    Map<String, Double> getMapDifferenceCostsFor2Periods(Map<String, Map<String, Double>> mapCosts);
}
