package ua.nick.milkcost.model;

import ua.nick.milkcost.utils.DateUtil;
import ua.nick.milkcost.utils.ReadExcelFileUtil;

import java.time.YearMonth;
import java.util.List;
import java.util.Set;

public class CostStructureFactory {

    private YearMonth period;
    private Set<String> accounts;
    private ReadExcelFileUtil readFilesUtil;
    private DateUtil dateUtil;
    private List<FileDescription> newFiles;

    public CostStructureFactory() {
        period = getYearMonth();
        accounts = createAccountsList();
        readFilesUtil = new ReadExcelFileUtil();
        dateUtil = new DateUtil();
    }

    public void setNewFiles(List<FileDescription> newFiles) {
        this.newFiles = newFiles;
    }

    public CostStructure createNewCostStructure(TypeCosts type, CostStructure... args) {
        CostStructure costStructure = null;
        String filePath = getFilePath(type);

        if (type == TypeCosts.DIRECT)
            costStructure = new CostStructureDirect(period, accounts, filePath);

        else if (type == TypeCosts.OVERHEAD)
            costStructure = new CostStructureOverhead(period, accounts, filePath);

        else if (type == TypeCosts.ADDITIONAL)
            costStructure = new CostStructureAdditional(period, accounts, filePath);

        else if (type == TypeCosts.TOTAL && args.length > 0)
            costStructure = new CostStructureTotal(args);

        return costStructure;
    };

    private YearMonth getYearMonth() {
        String filePath = Constants.COST_DIRECT_EXCEL_FILE_LOCATION_PATH;

        YearMonth mmYYYY = null;

        String[] filePathArray = filePath.split("/");
        String fileName = filePathArray[filePathArray.length - 1];
        String[] dateArray = fileName.substring(0, fileName.lastIndexOf(".")).split("_");

        if (dateArray.length >= 3)
            mmYYYY = dateUtil.getYearMonthFromString(String.format("%s.%s",
                            dateArray[dateArray.length-1],
                            dateArray[dateArray.length-2]));

        return mmYYYY;
    }

    private Set<String> createAccountsList() {
        return readFilesUtil.createAccountsListFromExcelData(
                Constants.ACCOUNTS_PROPERTY_EXCEL_FILE_LOCATION_PATH);
    }

    private String getFilePath(TypeCosts typeCosts) {
        for (FileDescription fileDescription : newFiles) {
            if (fileDescription.getTypeCosts() == typeCosts)
                return fileDescription.getFile().getAbsolutePath();
        }
        return null;
    }
}
