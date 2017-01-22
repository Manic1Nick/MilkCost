package ua.nick.milkcost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.CostStructureFactory;
import ua.nick.milkcost.model.FileDescription;
import ua.nick.milkcost.model.TypeCosts;
import ua.nick.milkcost.repository.CostStructureRepository;
import ua.nick.milkcost.repository.FileDataRepository;
import ua.nick.milkcost.utils.DateUtil;
import ua.nick.milkcost.utils.ListFilesUtil;

import java.io.File;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Service(value = "service")
public class CostServiceImpl implements CostService {

    @Autowired
    private CostStructureRepository costStructureRepository;

    @Autowired
    private FileDataRepository fileDataRepository;

    private CostStructureFactory factory;
    private DateUtil dateUtil; //need this here???
    private ListFilesUtil fUtil;

    public CostServiceImpl() {
        this.factory = new CostStructureFactory();
        dateUtil = new DateUtil();
        fUtil = new ListFilesUtil();
    }

    @Override
    public void createCostStructures(List<FileDescription> newFiles) {

        saveNewFiles(newFiles);
        factory.setNewFiles(newFiles);

        CostStructure costsDirect = factory.createNewCostStructure(TypeCosts.DIRECT);
        CostStructure costsOverhead = factory.createNewCostStructure(TypeCosts.OVERHEAD);
        CostStructure costsAdditional = factory.createNewCostStructure(TypeCosts.ADDITIONAL);

        CostStructure costsDirectWithOverheadWithAdditional =
                factory.createNewCostStructure(TypeCosts.TOTAL, costsDirect, costsOverhead, costsAdditional);
        saveNewCostStructure(costsDirectWithOverheadWithAdditional);
    }

    @Override
    public CostStructure saveNewCostStructure(CostStructure costStructure) {
        return costStructureRepository.save(costStructure);
    }

    @Override
    public CostStructure getCostStructure(TypeCosts typeCost, YearMonth monthYear) {
        return costStructureRepository.findByTypeCostsAndYearMonth(typeCost, monthYear);
    }

    @Override
    public TypeCosts findTypeCostsByString(String typeStr) {
        for (TypeCosts typeCosts : Arrays.asList(TypeCosts.values())) {
            if (typeStr.equals(typeCosts.toString()))
                return typeCosts;
        }
        return null;
    }

    @Override
    public YearMonth getYearMonthFromString(String monthPointYear) {
        return dateUtil.getYearMonthFromString(monthPointYear);
    }

    @Override
    public List<FileDescription> getNewFiles() {
        List<FileDescription> filesInDB = getAllFilesFromDB();
        List<File> filesInWorkFolder = getAllFilesFromWorkFolder();

        if (filesInWorkFolder.size() > 0) {
            return fUtil.getNewFileDescriptions(filesInDB, filesInWorkFolder);
        }

        return null;
    }

    @Override
    public void saveNewFiles(List<FileDescription> newFiles) {
        for (FileDescription fileDescription : newFiles) {
            fileDataRepository.save(fileDescription);
        }
    }

    @Override
    public List<FileDescription> getAllFilesFromDB() {
        return fileDataRepository.findAll();
    }

    @Override
    public List<File> getAllFilesFromWorkFolder() {
        return Arrays.asList(fUtil.getfList());
    }
}