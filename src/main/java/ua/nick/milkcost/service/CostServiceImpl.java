package ua.nick.milkcost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nick.milkcost.model.*;
import ua.nick.milkcost.repository.CostRepository;
import ua.nick.milkcost.repository.CostStructureRepository;
import ua.nick.milkcost.repository.FileDataRepository;
import ua.nick.milkcost.utils.DateUtil;
import ua.nick.milkcost.utils.ListFilesUtil;
import ua.nick.milkcost.utils.ReadExcelFileUtil;

import java.io.File;
import java.util.*;

@Service(value = "service")
public class CostServiceImpl implements CostService {

    @Autowired
    private CostStructureRepository costStructureRepository;

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private CostRepository costRepository;

    @Autowired
    private CostStructureFactory factory;

    @Autowired
    private DateUtil dateUtil; //todo need this here???

    @Autowired
    private ListFilesUtil listFilesUtil;

    @Autowired
    private ReadExcelFileUtil readExcelFilesUtil;

    public CostServiceImpl() {
    }

    @Override
    public void createCostStructures(List<FileDescription> newFiles) {

        saveNewFiles(newFiles);
        factory.setNewFiles(newFiles);
        factory.setAccounts(readExcelFilesUtil.getAccountsFromNewFiles(newFiles));

        CostStructure costsDirect = factory.createNewCostStructure(TypeCosts.DIRECT);
        costsDirect = saveCostsFromCostStructure(costsDirect);

        CostStructure costsOverhead = factory.createNewCostStructure(TypeCosts.OVERHEAD);
        costsOverhead = saveCostsFromCostStructure(costsOverhead);

        CostStructure costsAdditional = factory.createNewCostStructure(TypeCosts.ADDITIONAL);
        costsAdditional = saveCostsFromCostStructure(costsAdditional);

        CostStructure costsDirectWithOverheadWithAdditional =
                factory.createNewCostStructure(TypeCosts.TOTAL, costsDirect, costsOverhead, costsAdditional);
        costsDirectWithOverheadWithAdditional =
                saveCostsFromCostStructure(costsDirectWithOverheadWithAdditional);

        saveNewCostStructure(costsDirectWithOverheadWithAdditional);
    }

    @Override
    public CostStructure saveNewCostStructure(CostStructure costStructure) {
        return costStructureRepository.save(costStructure);
    }

    @Override
    public CostStructure getCostStructure(TypeCosts typeCost, Date date) {
        return costStructureRepository.findByTypeCostsAndDate(typeCost, date);
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
    public List<FileDescription> getNewFiles() {
        List<FileDescription> filesInDB = getAllFilesFromDB();
        List<File> filesInWorkFolder = getAllFilesFromWorkFolder();

        if (filesInWorkFolder.size() > 0)
            return listFilesUtil.getNewFileDescriptions(filesInDB, filesInWorkFolder);

        return null;
    }

    @Override
    public void saveNewFiles(List<FileDescription> newFiles) {
        for (FileDescription fileDescription : newFiles)
            fileDataRepository.save(fileDescription);
    }

    @Override
    public List<FileDescription> getAllFilesFromDB() {
        return fileDataRepository.findAll();
    }

    @Override
    public List<File> getAllFilesFromWorkFolder() {
        return Arrays.asList(listFilesUtil.getfList());
    }

    @Override
    public CostStructure saveCostsFromCostStructure(CostStructure costStructure) {
        Set<Cost> costs = costStructure.getCosts();
        Set<Cost> newCosts = new HashSet<>();
        for (Cost cost : costs)
            newCosts.add(costRepository.save(cost));

        costStructure.setCosts(newCosts);

        return costStructure;
    }
}