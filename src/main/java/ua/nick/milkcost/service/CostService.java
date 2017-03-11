package ua.nick.milkcost.service;

import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.FileDescription;
import ua.nick.milkcost.model.TypeCosts;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface CostService {

    void createCostStructures(List<FileDescription> newFiles);
    CostStructure saveNewCostStructure(CostStructure costStructure);
    CostStructure getCostStructure(TypeCosts type, Date date);
    TypeCosts findTypeCostsByString(String typeStr);
    Date getDateFromString(String date);

    List<FileDescription> getNewFiles();
    void saveNewFiles(List<FileDescription> newFiles);
    List<FileDescription> getAllFilesFromDB();
    List<File> getAllFilesFromWorkFolder();

    CostStructure saveCostsFromCostStructure(CostStructure costStructure);
}
