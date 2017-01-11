package ua.nick.milkcost.run;

import ua.nick.milkcost.model.Constants;
import ua.nick.milkcost.model.Cost;
import ua.nick.milkcost.model.Milk;
import ua.nick.milkcost.model.Revenue;
import ua.nick.milkcost.utils.ReadExcelFile;
import ua.nick.milkcost.utils.WriteExcelFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CalculateMilkCost {

    public static void main(String[] args) throws Exception {

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

        Cost costSept16 = new Cost(costDirectStructure, costOverheadStructure, costAdditionalStructure);

        Map<String, Double> revenueMap =
                ReadExcelFile.getMapFromColumns("REVENUE", additionalFileName);
        Revenue revenueSept16 = new Revenue(revenueMap.get("REVENUE"));

        Map<String, Double> beginDataMap =
                ReadExcelFile.getMapFromColumns("BEGIN", additionalFileName);
        Map<String, Double> quantityDataMap =
                ReadExcelFile.getMapFromColumns("QUANTITY", additionalFileName);

        Milk milkSept16 = new Milk(quantityDataMap, costSept16, revenueSept16);
        milkSept16.setBeginStructure(beginDataMap);
        milkSept16.setMonth(new Date());

        List<String> indicators = ReadExcelFile.createIndicatorsListFromExcelData(
                Constants.INDICATORS_EXCEL_FILE_LOCATION_PATH);

        WriteExcelFile.writeResultMapToExcelFileAsColumnes(
                Constants.RESULT_EXCEL_FILE_LOCATION_PATH, indicators, costSept16, milkSept16);
    }
}
