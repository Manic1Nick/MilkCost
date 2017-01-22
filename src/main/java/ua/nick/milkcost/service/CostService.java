package ua.nick.milkcost.service;

import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.FileDescription;
import ua.nick.milkcost.model.TypeCosts;

import java.io.File;
import java.time.YearMonth;
import java.util.List;

public interface CostService {

    void createCostStructures(List<FileDescription> newFiles);
    CostStructure saveNewCostStructure(CostStructure costStructure);
    CostStructure getCostStructure(TypeCosts type, YearMonth monthYear);
    TypeCosts findTypeCostsByString(String typeStr);
    YearMonth getYearMonthFromString(String monthPointYear);

    List<FileDescription> getNewFiles();
    void saveNewFiles(List<FileDescription> newFiles);
    List<FileDescription> getAllFilesFromDB();
    List<File> getAllFilesFromWorkFolder();
}
