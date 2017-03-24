package ua.nick.milkcost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nick.milkcost.model.*;
import ua.nick.milkcost.repository.CostRepository;
import ua.nick.milkcost.repository.FileCostRepository;
import ua.nick.milkcost.repository.PeriodRepository;
import ua.nick.milkcost.utils.DateUtil;
import ua.nick.milkcost.utils.ListFilesUtil;
import ua.nick.milkcost.utils.ReadExcelFileUtil;

import java.io.File;
import java.util.*;

@Service(value = "service")
public class CostServiceImpl implements CostService {

    @Autowired
    private FileCostRepository fileCostRepository;

    @Autowired
    private CostRepository costRepository;

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private ListFilesUtil listFilesUtil;

    @Autowired
    private ReadExcelFileUtil readExcelFilesUtil;

    public CostServiceImpl() {
    }

    @Override
    public List<FileCost> saveNewCosts(List<FileCost> newFiles) {
        List<FileCost> files = addCostsToNewFiles(newFiles);
        saveNewFiles(files);

        saveCostsFromFiles(files);

        return files;
    }

    @Override
    public TypeCosts findTypeCostsByString(String typeStr) {
        for (TypeCosts typeCosts : Arrays.asList(TypeCosts.values()))
            if (typeStr.equals(typeCosts.toString()))
                return typeCosts;

        return null;
    }

    @Override
    public Date getDateFromString(String date) {
        return dateUtil.getDateFromString(date);
    }

    @Override
    public List<FileCost> getNewFiles() {
        List<FileCost> result = new ArrayList<>();

        List<FileCost> filesInDB = getAllFilesFromDB();
        List<File> filesInWorkFolder = getAllFilesFromWorkFolder();

        List<File> newFiles = new ArrayList<>();
        if (filesInWorkFolder.size() > 0) {
            for (File file : filesInWorkFolder) {
                if (!file.getName().startsWith("DB"))
                    newFiles.add(file);
            }
            result = listFilesUtil.getNewFileCosts(filesInDB, newFiles);
        }
        updateOldFilesInDB(filesInDB);

        return result;
    }

    @Override
    public List<FileCost> getNewFilesForUpdateDB() {
        List<FileCost> result = new ArrayList<>();

        List<FileCost> filesWithUpdatedDB = getAllFilesUpdatedDB();
        List<File> filesInWorkFolder = getAllFilesFromWorkFolder();

        List<File> filesWithUpdatedDBInWorkFolder = new ArrayList<>();
        if (filesInWorkFolder.size() > 0) {
            for (File file : filesInWorkFolder) {
                if (file.getName().startsWith("DB"))
                    filesWithUpdatedDBInWorkFolder.add(file);
            }
            result = listFilesUtil.getNewFileCosts(filesWithUpdatedDB, filesWithUpdatedDBInWorkFolder);
        }

        if (result.size() == 0)
            updateAllPeriodsInDB(false);

        return result;
    }

    @Override
    public void saveNewFiles(List<FileCost> newFiles) {
        for (FileCost file : newFiles) {
            if (file.getTypeCosts() != null)
                file = updatePeriodInFile(file);

            fileCostRepository.save(file);
        }
    }

    @Override
    public List<FileCost> getAllFilesWithTypeCostsFromDB() {
        return fileCostRepository.findByTypeCostsIsNotNull();
    }

    @Override
    public List<FileCost> getAllFilesFromDB() {
        return fileCostRepository.findAll();
    }

    @Override
    public List<File> getAllFilesFromWorkFolder() {
        return Arrays.asList(listFilesUtil.getfList());
    }

    @Override
    public List<FileCost> saveNewCostsForUpdateDB(List<FileCost> newFiles) {
        List<FileCost> files = addCostsToNewFilesForUpdateDB(newFiles);
        saveNewFiles(files);

        saveCostsFromFiles(files);

        return files;
    }

    @Override
    public List<Period> getAllPeriodsFromDB() {
        return periodRepository.findAll();
    }

    @Override
    public void saveCostsFromFiles(List<FileCost> newFiles) {
        for (FileCost file : newFiles) {
            Long periodId = file.getPeriodId();

            Set<Cost> costs = file.getCosts();
            for (Cost cost : costs) {
                double sum = cost.getSum();
                cost.setSum((double) (Math.round(sum * 100)) / 100);//round #.##

                if (cost.getPeriodId() == null)
                    cost.setPeriodId(periodId);

                saveNewCost(cost);

                if (file.getFileName().startsWith("DB"))
                    updatePeriodInDB(cost.getPeriodId(), true);
            }
            if (periodId != null)
                updatePeriodInDB(periodId, true);
        }
    }

    @Override
    public Cost saveNewCost(Cost cost) {
        return costRepository.save(cost);
    }

    @Override
    public List<Cost> getListCostsByPeriodId(Long id) {
        return costRepository.findByPeriodId(id);
    }

    @Override
    public Map<String, Double> getMapCostsByPeriodIdAndTypeCosts(Long id, TypeCosts type) {
        List<List<Cost>> lists = new ArrayList<>();

        for (TypeCosts typeCosts : Arrays.asList(TypeCosts.values()))
            lists.add(costRepository.findByPeriodIdAndTypeCosts(id, typeCosts));

        for (List<Cost> list : lists) {
            if (list.size() > 0 && type == list.get(0).getTypeCosts())
                return createMapCostsFromListCosts(list);
        }

        return sumListCostsToMapCosts(lists);
    }

    @Override
    public Map<String, Double> getMapCostsByListCosts(List<Cost> list) {

        Map<String, Double> map = new HashMap<>();
        for (Cost cost : list) {
            double sum = cost.getSum();
            sum = (double) (Math.round(sum * 100)) / 100;//round #.##
            map.put(cost.getNameCosts().name(), sum);
        }
        return map;
    }

    @Override
    public Map<String, Double> sumListCostsToMapCosts(List<List<Cost>> lists) {

        Map<String, Double> map = new HashMap<>();
        for (NameCosts nameCosts : Arrays.asList(NameCosts.values()))
            map.put(nameCosts.name(), 0.0);

        for (List<Cost> costs : lists) {
            for (Cost cost : costs) {
                String name = cost.getNameCosts().name();
                double sum = cost.getSum();

                if (map.containsKey(name))
                    sum += map.get(name);

                sum = (double) (Math.round(sum * 100)) / 100;//round #.##
                map.put(name, sum);
            }
        }
        return map;
    }

    @Override
    public Map<String, Double> createTotalMapFromMaps(Map<String, Map<String, Double>> mapOfMaps) {

        Map<String, Double> totalMap = new HashMap<>();
        for (NameCosts nameCosts : Arrays.asList(NameCosts.values()))
            totalMap.put(nameCosts.name(), 0.0);

        for (Map<String, Double> map : mapOfMaps.values()) {
            for (String name : map.keySet()) {
                double sum = map.get(name);

                if (totalMap.containsKey(name))
                    sum += totalMap.get(name);

                sum = (double) (Math.round(sum * 100)) / 100;//round #.##
                totalMap.put(name, sum);
            }
        }
        return totalMap;
    }

    @Override
    public List<Cost> createCostStructure(Period period) {

        List<List<Cost>> lists = new ArrayList<>();
        for (TypeCosts typeCosts : Arrays.asList(TypeCosts.values()))
            lists.add(costRepository.findByPeriodIdAndTypeCosts(period.getId(), typeCosts));

        List<Cost> costs = sumListCosts(lists);
        costs = addPercentagesToCosts(costs);
        costs = addColorsToCosts(costs);

        return costs;
    }

    @Override
    public List<Cost> createTotalCostStructure(List<Period> periods) {
        List<List<Cost>> lists = new ArrayList<>();

        for (Period period : periods)
            lists.add(costRepository.findByPeriodId(period.getId()));

        List<Cost> costs = sumListCosts(lists);
        costs = addPercentagesToCosts(costs);
        costs = addColorsToCosts(costs);

        return costs;
    }

    @Override
    public Cost getCostByPeriodIdAndNameCostsAndTypeCosts(
            Long periodId, NameCosts nameCosts, TypeCosts typeCosts) {
        Cost resultCost = null;

        List<Cost> list = costRepository.findByPeriodIdAndNameCosts(periodId, nameCosts);

        for (Cost cost : list) {
            if (cost.getTypeCosts() == typeCosts)
                resultCost = cost;
        }

        if (resultCost == null && list.size() > 0) {
            resultCost = list.get(0);
            resultCost.setSum(calculateSumTotalFromListCosts(list));
            resultCost.setTypeCosts(typeCosts);
        }

        return resultCost;
    }

    @Override
    public List<Cost> getAllCostsByName(NameCosts nameCosts) {
        return costRepository.findByNameCosts(nameCosts);
    }

    @Override
    public Period saveNewPeriod(Period period) {
        return periodRepository.save(period);
    }

    @Override
    public Period getPeriodById(Long id) {
        return periodRepository.findById(id);
    }

    @Override
    public Period getPeriodByYearAndMonth(String year, String month) {
        return periodRepository.findByYearAndMonth(year, month);
    }

    @Override
    public List<Period> getPeriodsByYear(String year) {
        return periodRepository.findByYear(year);
    }

    @Override
    public int[] createDynamicArrayOfCost(String costName) {

        NameCosts name = null;
        for (NameCosts nameCosts : Arrays.asList(NameCosts.values()))
            if (costName.contains(nameCosts.name()))
                name = nameCosts;

        List<Cost> costs = costRepository.findByNameCosts(name);

        int[] sums = new int[costs.size()];
        for (int i = 0; i < sums.length; i++)
            sums[i] = (int) costs.get(i).getSum();

        return sums;
    }

    @Override
    public double calculateSumTotalFromListCosts(List<Cost> costs) {
        double sum = 0.0;
        for (Cost cost : costs)
            sum += cost.getSum();

        sum = (double) (Math.round(sum * 100)) / 100;//round #.##

        return sum;
    }

    @Override
    public Map<String, Double> getMapDifferenceCostsFor2Periods(Map<String, Map<String, Double>> mapCosts) {
        Map<String, Double> differenceCosts = new HashMap<>();

        List<String> periods = new ArrayList<>(mapCosts.keySet());
        List<String> names = new ArrayList<>(mapCosts.get(periods.get(0)).keySet());

        for (String period : periods) {
            Map<String, Double> map = mapCosts.get(period);

            for (String name : names) {
                if (periods.indexOf(period) == 0) {
                    differenceCosts.put(name, map.get(name));

                } else {
                    double sum = differenceCosts.get(name) - map.get(name);
                    sum = (double) (Math.round(sum * 100)) / 100;//round #.##
                    differenceCosts.put(name, sum);
                }
            }
        }
        return differenceCosts;
    }

    private List<FileCost> getAllFilesUpdatedDB() {
        List<FileCost> allFilesInDB = getAllFilesFromDB();

        List<FileCost> filesInDB = new ArrayList<>();
        for (FileCost file : allFilesInDB) {
            if (file.getFileName().startsWith("DB"))
                filesInDB.add(file);
        }
        return filesInDB;
    }

    public List<File> getAllFilesWithUpdatedDBFromWorkFolder() {
        List<File> allFilesInWorkFolder = getAllFilesFromWorkFolder();

        List<File> filesInWorkFolder = new ArrayList<>();
        for (File file : allFilesInWorkFolder) {
            if (file.getName().startsWith("DB"))
                filesInWorkFolder.add(file);
        }
        return filesInWorkFolder;
    }

    private Map<String, Double> createMapCostsFromListCosts(List<Cost> costs) {
        Map<String, Double> map = new HashMap<>();

        for (Cost cost : costs) {
            double sum = (double) (Math.round(cost.getSum() * 100)) / 100;//round #.##
            map.put(cost.getNameCosts().name(), sum);
        }
        return map;
    }

    private List<Cost> sumListCosts(List<List<Cost>> lists) {

        if (lists.size() == 1)
            return lists.get(0);

        Map<String, Cost> map = new HashMap<>();

        for (NameCosts nameCosts : Arrays.asList(NameCosts.values()))
            map.put(nameCosts.name(), null);

        for (List<Cost> costs : lists) {
            for (Cost cost : costs) {
                String name = cost.getNameCosts().name();

                if (map.containsKey(name))
                    cost.add(map.get(name));

                map.put(name, cost);
            }
        }
        List<Cost> list = new ArrayList<>(map.values());
        list.removeAll(Collections.singleton(null)); //remove all nulls from list

        return list;
    }

    private List<Cost> addPercentagesToCosts(List<Cost> list) {

        double totalSum = 0.0;
        for (Cost cost : list)
            totalSum += cost.getSum();

        for (Cost cost : list)
            cost.setShare((int) Math.round(cost.getSum() / totalSum * 100));

        return list;
    }

    private List<Cost> addColorsToCosts(List<Cost> list) {

        String[] colors = Constants.COLORS_12_FOR_DIAGRAM;
        for (Cost cost : list)
            cost.setColor(colors[list.indexOf(cost)]);

        return list;
    }

    private List<FileCost> addCostsToNewFiles(List<FileCost> newFiles) {
        Set<String> accounts = readExcelFilesUtil.getAccountsFromNewFiles(newFiles);

        for (FileCost file : newFiles) {
            Set<Cost> costs = new HashSet<>();
            if (file.getTypeCosts() == TypeCosts.DIRECT
                    || file.getTypeCosts() == TypeCosts.OVERHEAD)
                costs = readExcelFilesUtil.getSetCostsFromRows(
                        accounts, file.getFile().getAbsolutePath());

            else if (file.getTypeCosts() == TypeCosts.ADDITIONAL)
                costs = readExcelFilesUtil.getSetCostsFromColumns(
                        "COSTS", file.getFile().getAbsolutePath());

            file.setCosts(costs);
            file.setChanged(true);
        }
        return newFiles;
    }

    private Map<String, Period> createMapPeriods(List<FileCost> newFiles) {
        Map<String, Period> mapPeriods = new HashMap<>();

        for (FileCost file : newFiles) {
            List<Period> periods = readExcelFilesUtil.getPeriodsFromFilesForUpdateDB(
                    file.getFile().getAbsolutePath());

            for (Period period : periods) {
                Period periodInDb = periodRepository.findByYearAndMonth(
                        period.getYear(), period.getMonth());

                period = periodInDb == null ?
                        periodRepository.save(period) :
                        periodInDb;

                mapPeriods.put(period.formatYYYYdashMM(), period);
            }
        }
        return mapPeriods;
    }

    private List<FileCost> addCostsToNewFilesForUpdateDB(List<FileCost> newFiles) {
        Map<String, Period> mapPeriods = createMapPeriods(newFiles);

        for (FileCost file : newFiles) {
            Set<Cost> costs = readExcelFilesUtil.getSetCostsFromFilesForUpdateDB(
                    mapPeriods, file.getFile().getAbsolutePath());

            file.setCosts(costs);
            file.setChanged(true);
        }
        return newFiles;
    }

    private FileCost findFileByName(String name) {
        return fileCostRepository.findByFileName(name);
    }

    private FileCost updatePeriodInFile(FileCost file) {
        Period period = new Period(file.getFileName());

        Long periodId = file.getPeriodId();
        if (periodId == null) {
            Period periodInDB = getPeriodByYearAndMonth(period.getYear(), period.getMonth());
            period = periodInDB != null ? periodInDB : saveNewPeriod(period);

        } else {
            period = getPeriodById(periodId);
        }
        updatePeriodInDB(period.getId(), true);
        file.setPeriodId(period.getId());

        return file;
    }

    private Period updatePeriodInDB(Long periodId, boolean updated) {
        Period period = periodRepository.findById(periodId);
        period.setUpdated(updated);

        return periodRepository.save(period);
    }

    private void updateAllPeriodsInDB(boolean updated) {
        List<Period> periods = periodRepository.findAll();
        for (Period period : periods) {
            period.setUpdated(updated);
            periodRepository.save(period);
        }
    }

    private List<FileCost> updateOldFilesInDB(List<FileCost> filesInDB) {

        for (FileCost file : filesInDB) {
            file.setChanged(false);
            fileCostRepository.save(file);

            Long periodId = file.getPeriodId();
            if (periodId != null)
                updatePeriodInDB(periodId, false);
        }
        return filesInDB;
    }
}