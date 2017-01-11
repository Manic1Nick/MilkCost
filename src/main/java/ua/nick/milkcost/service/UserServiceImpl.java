package ua.nick.milkcost.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.nick.milkcost.model.Constants;
import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.TypeCost;
import ua.nick.milkcost.repository.CostStructureRepository;
import ua.nick.milkcost.repository.UserRepository;
import ua.nick.milkcost.utils.ReadExcelFile;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service(value = "service")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CostStructureRepository costStructureRepository;

    public UserServiceImpl() {
    }

    @Override
    public CostStructure createNewCostStructure() throws ParseException {
        Set<String> accounts = ReadExcelFile.createAccountsListFromExcelData(
                Constants.ACCOUNTS_PROPERTY_EXCEL_FILE_LOCATION_PATH);
        String costDirectFileName = Constants.COST_DIRECT_EXCEL_FILE_LOCATION_PATH;
        String costOverheadFileName = Constants.COST_OVERHEAD_EXCEL_FILE_LOCATION_PATH;
        String additionalFileName = Constants.ADDITIONAL_EXCEL_FILE_LOCATION_PATH;

        Map<String, Double> costDirectStructure =
                ReadExcelFile.getMapFromRows(accounts, costDirectFileName);
        Map<String, Double> costOverheadStructure =
                ReadExcelFile.getMapFromRows(accounts, costOverheadFileName);
        Map<String, Double> costAdditionalStructure =
                ReadExcelFile.getMapFromColumns("COSTS", additionalFileName);

        String shortName = costDirectFileName.substring(
                costDirectFileName.lastIndexOf("/"), costDirectFileName.lastIndexOf("."));
        String year = getYearFromFileName(shortName);
        String month = getMonthFromFileName(shortName);

        CostStructure directCosts = new CostStructure();

        return null;
    }

    @Override
    public CostStructure saveNewCostStructure(CostStructure costStructure) {
        return costStructureRepository.save(costStructure);
    }

    @Override
    public CostStructure getCostStructure(TypeCost typeCost, Date monthYear) {
        return costStructureRepository.findByTypeCostAndDate(typeCost, monthYear);
    }

    @Transactional
    private String getYearFromFileName(String fileName) {

        String[] dateArray = fileName.split("_");

        if (dateArray.length < 3)
            return null;

        return dateArray[dateArray.length-2];
    }

    @Transactional
    private String getMonthFromFileName(String fileName) {

        String[] dateArray = fileName.split("_");

        if (dateArray.length < 3)
            return null;

        return dateArray[dateArray.length-1];
    }

}
