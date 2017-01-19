package ua.nick.milkcost.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class WriteExcelFile {

    public static void writeMapToExcelFileAsLines(String fileName, Map<String, Double> costsList) throws Exception{

        Workbook workbook = createWorkbookForFile(fileName);
        Sheet sheet = workbook.createSheet();

        Row row1 = sheet.createRow(1);
        Row row2 = sheet.createRow(2);
        int columnIndex = 0;
        double total = 0.0;
        for (String key : costsList.keySet()) {
            row1.createCell(columnIndex).setCellValue(key);
            row2.createCell(columnIndex++).setCellValue(costsList.get(key));
            total += costsList.get(key);
        }

        //cell total summ
        row1.createCell(columnIndex).setCellValue("TOTAL");
        row2.createCell(columnIndex).setCellValue(total);

        //lets write the excel data to file now
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        System.out.println(fileName + " written successfully");
    }

    public static void writeMapToExcelFileAsColumnes(String fileName, Map<String, Double> costsList)
            throws Exception{

        Workbook workbook = createWorkbookForFile(fileName);

        //costs
        Sheet sheet = workbook.createSheet("Costs");

        int rowIndex = 0;
        double total = 0.0;
        for (String key : costsList.keySet()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(key);
            row.createCell(1).setCellValue(costsList.get(key));
            total += costsList.get(key);
        }

        //cell total summ
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue("TOTAL");
        row.createCell(1).setCellValue(total);

        //lets write the excel data to file now
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        System.out.println(fileName + " written successfully");
    }

    /*public static void writeResultMapToExcelFileAsColumnes(String fileName, List<String> indicators,
                                                           Cost cost, Milk milk) throws Exception{

        Map<String, Double> costMap = cost.getCostStructure();
        Map<String, Double> milkMap = createMapDataForResultFile(milk);

        Workbook workbook = createWorkbookForFile(fileName);
        Sheet sheet = workbook.createSheet("Result");

        int indexBegin = indicators.indexOf("INDICATORS:") + 1;
        int indexEnd = indicators.indexOf("COSTS:");
        sheet = addIndicatorsToExcelColumn(
                indicators.subList(indexBegin, indexEnd), sheet, milkMap, costMap, indexBegin);

        indexBegin = indexEnd + 1;
        indexEnd = indicators.indexOf("END");
        sheet = addCostsToExcelColumn(
                indicators.subList(indexBegin, indexEnd), sheet, milkMap, costMap, indexBegin, true);

        //test row
        Row row = sheet.createRow(sheet.getLastRowNum() + 2);
        row.createCell(0).setCellValue("TEST");
        row.createCell(1).setCellValue(
                milk.getCost().getCostTotal() - milk.getCost().getCostAdmin() -
                (cost.getCostTotal() - cost.getCostAdmin()));

        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        System.out.println(fileName + " written successfully");
    }

    private static Map<String, Double> createMapDataForResultFile(Milk milk) {

        Map<String, Double> mapData = new HashMap<>();

        mapData.put("QUANTITY_PRODUCE", milk.getQuantityProduce());
        mapData.put("COST_PRODUCE_WITHOUT_ADMIN",
                milk.getCost().getCostTotal() - milk.getCost().getCostAdmin());
        mapData.put("QUANTITY_SOLD", milk.getQuantitySold());
        mapData.put("COST_SOLD_WITHOUT_ADMIN", milk.getCostSoldWithoutAdmin());
        mapData.put("COST_SOLD_UNIT_WITHOUT_ADMIN", milk.getCostMilkUnitSoldWithoutAdmin());
        mapData.put("ADMIN", milk.getCost().getCostAdmin());
        mapData.put("COST_SOLD_FULL", milk.getCostSoldWithoutAdmin() + milk.getCost().getCostAdmin());
        mapData.put("COST_SOLD_UNIT_FULL", milk.getCostMilkUnitSoldFull());
        mapData.put("PRICE_UNIT", milk.getPriceMilkUnit());
        mapData.put("PROFABILITY_%", (
                milk.getPriceMilkUnit() - milk.getCostMilkUnitSoldFull()) * 100 / milk.getCostMilkUnitSoldFull());

        return mapData;
    }*/

    private static Workbook createWorkbookForFile(String fileName) throws Exception {

        Workbook workbook = null;
        if(fileName.endsWith("xlsx")){
            workbook = new XSSFWorkbook();
        }else if(fileName.endsWith("xls")){
            workbook = new HSSFWorkbook();
        }else{
            throw new Exception("invalid file name, should be xls or xlsx");
        }

        return workbook;
    }

    private static Sheet addIndicatorsToExcelColumn(
            List<String> indicators, Sheet sheet, Map<String, Double> milkMap,
            Map<String, Double> costMap, int rowIndex) {

        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue("INDICATORS:");

        for (String key : indicators) {
            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(key);

            Double value = milkMap.get(key);
            for (String addKey : costMap.keySet()) {
                if (addKey.contains(key) && !addKey.equals(key)) {
                    value += costMap.get(addKey);
                }
            }
            row.createCell(1).setCellValue(value);
        }

        return sheet;
    }

    private static Sheet addCostsToExcelColumn(
            List<String> costs, Sheet sheet, Map<String, Double> milkMap,
            Map<String, Double> costMap, int rowIndex, boolean totalSumm) {

        Row row = sheet.createRow(rowIndex++);
        row.createCell(row.getLastCellNum() + 1).setCellValue("COSTS:");

        double total = 0.0;
        for (String key : costs) {
            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(key);

            Double value = costMap.get(key);
            for (String addKey : costMap.keySet()) {
                if (addKey.contains(key) && !addKey.equals(key)) {
                    value += costMap.get(addKey);
                }
            }
            row.createCell(1).setCellValue(value);
            total += value;
        }

        if (totalSumm) {
            //cell total summ
            row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue("TOTAL");
            row.createCell(1).setCellValue(total);
        }

        return sheet;
    }
}
