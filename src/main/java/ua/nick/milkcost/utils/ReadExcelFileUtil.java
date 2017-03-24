package ua.nick.milkcost.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ua.nick.milkcost.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Component
public class ReadExcelFileUtil {

    private ListFilesUtil fUtil;

    public ReadExcelFileUtil() {
        this.fUtil = new ListFilesUtil();
    }

    public List<File> getCurrentListOfAllFiles() {
        return Arrays.asList(fUtil.getfList());
    }

    public List<String> createIndicatorsListFromExcelData(String fileName) {

        List<String> resultForm = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(fileName);
            Sheet sheet = getSheetFromFile(fis, fileName);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                String currentCell = "";
                while (cellIterator.hasNext() && !currentCell.equals("END")) {
                    Cell cell = cellIterator.next();
                    currentCell = cell.toString().trim();
                    if (!currentCell.isEmpty()) {
                        resultForm.add(currentCell);
                    }
                }
            }
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultForm;
    }

    public Map<String, Double> getMapFromIndicatorsFile(String filePath) {

        Map<String, Double> mapData = new HashMap<>();

        try {
            FileInputStream fis = new FileInputStream(filePath);
            Sheet sheet = getSheetFromFile(fis, filePath);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String currentCell = cell.toString().trim();

                    if (currentCell.contains("COSTS") && !currentCell.equals("END")) {
                        while (rowIterator.hasNext()) {
                            row = rowIterator.next();
                            cellIterator = row.cellIterator();

                            while (cellIterator.hasNext() && !currentCell.equals("END")) {
                                cell = cellIterator.next();
                                currentCell = cell.toString().trim();

                                if (!currentCell.isEmpty()) {
                                    mapData.put(currentCell, 0.0);
                                }
                            }
                        }
                    } else if (currentCell.equals("END")) {
                        fis.close();
                        return mapData;
                    }
                }
            }
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapData;
    }

    public Set<Cost> getSetCostsFromRows(Set<String> setMarkers, String filePath) {
        String fileName = getFileNameFromFilePath(filePath);
        TypeCosts typeCosts = getTypeCostsFromFileName(fileName);

        List<List<String>> rows = readExcelData(filePath);

        Map<NameCosts, Cost> mapCosts = new HashMap<>();

        List<String> listMarkers = rows.get(0);

        for (int i = 1; i < rows.size(); i++) {

            while (rows.get(i).get(0).isEmpty())
                i++;

            List<String> currentRow = rows.get(i);
            double rowSumm = 0;

            for (String marker : listMarkers) {
                int point = marker.indexOf(".");
                if (point > 0 && setMarkers.contains(marker.substring(0, point))) {
                    String cell = currentRow.get(listMarkers.indexOf(marker));
                    rowSumm = cell.isEmpty()
                            ? rowSumm
                            : rowSumm + Double.parseDouble(cell);
                }
            }

            String currentMarker = currentRow.get(0);
            mapCosts = addCostToMapCosts(mapCosts, currentMarker, typeCosts, rowSumm);
        }
        return new HashSet<>(mapCosts.values());
    }

    private Map<NameCosts, Cost> addCostToMapCosts(Map<NameCosts, Cost> mapCosts,
                                                   String marker, TypeCosts typeCosts, double summ) {

        NameCosts nameCosts = castingStringToNameCosts(marker);

        if (nameCosts != null) {
            summ = (double) (Math.round(summ * 100)) / 100; //round #.##
            Cost cost = new Cost(nameCosts, typeCosts, summ);

            if (mapCosts.keySet().contains(nameCosts))
                cost = cost.add(mapCosts.get(nameCosts));

            mapCosts.put(nameCosts, cost);
        }
        return mapCosts;
    }

    public NameCosts castingStringToNameCosts(String marker) {

        List<NameCosts> allNameCosts = Arrays.asList(NameCosts.values());
        for (NameCosts item : allNameCosts) {
            if (item.name().contains(marker) || marker.contains(item.name()))
                return item;
        }
        return null;
    }

    public Set<Cost> getSetCostsFromColumns(String marker, String filePath) {
        String fileName = getFileNameFromFilePath(filePath);
        TypeCosts typeCosts = getTypeCostsFromFileName(fileName);

        List<List<String>> rows = readExcelData(filePath);

        Map<NameCosts, Cost> mapCosts = new HashMap<>();

        for (int i = 1; i < rows.size(); i++) {

            List<String> currentRow = rows.get(i);
            String firstCell = currentRow.get(0);
            while (firstCell.isEmpty())
                i++;

            if (firstCell.equals(marker)) {
                double value = Double.parseDouble(currentRow.get(currentRow.size()-1));

                //specific account
                value = currentRow.get(1).contains("KV") ? value / 3 : value ;
                value = currentRow.get(1).contains("REPAIRS") ? value * 0.4 : value ;

                String currentMarker = currentRow.get(1);
                mapCosts = addCostToMapCosts(mapCosts, currentMarker, typeCosts, value);
            }
            if (firstCell.equals("END"))
                i = rows.size();

        }
        return new HashSet<>(mapCosts.values());
    }

    public Set<Cost> getSetCostsFromFilesForUpdateDB(Map<String, Period> periods, String filePath) {
        Set<Cost> costs = new HashSet<>();

        List<List<String>> rows = readExcelData(filePath);
        List<String> costNames = rows.get(0);

        for (int i = 1; i < rows.size(); i++) {

            List<String> currentRow = rows.get(i);
            String firstCell = currentRow.get(0);
            while (firstCell.isEmpty())
                i++;

            String period = currentRow.get(0);

            for (String name : costNames.subList(1, currentRow.size())) {
                Cost cost = new Cost();
                cost.setTypeCosts(TypeCosts.TOTAL);
                cost.setPeriodId(periods.get(period).getId());
                cost.setNameCosts(NameCosts.valueOf(name));

                String doubleText = currentRow.get(costNames.indexOf(name));
                /*if (doubleText.contains("+")) {
                    String[] array = doubleText.split(""+"");
                    double sum = 0.0;
                    for (int j = 0; j < array.length; j++) {
                        sum += Double.parseDouble(array[i]);
                    }
                    doubleText = sum + "";
                }*/
                cost.setSum(Double.parseDouble(doubleText));
                costs.add(cost);
            }
            if (firstCell.equals("END"))
                i = rows.size();
        }
        return costs;
    }

    public List<Period> getPeriodsFromFilesForUpdateDB(String filePath) {
        List<Period> periods = new ArrayList<>();
        List<List<String>> rows = readExcelData(filePath);
        for (int i = 1; i < rows.size(); i++) {

            while (rows.get(i).get(0).isEmpty())
                i++;

            List<String> currentRow = rows.get(i);
            String firstCell = currentRow.get(0);

            if (!firstCell.equals("END")) {
                String[] yearMonth = firstCell.split("-");
                Period period = new Period(yearMonth[0], yearMonth[1]);
                periods.add(period);

            } else {
                i = rows.size();
            }
        }
        return periods;
    }

    public Set<String> createAccountsListFromExcelData(String fileName) {

        //create set unique accounts
        Set<String> accounts = new HashSet<>();

        try {
            if (!fileName.toLowerCase().contains("accounts") &&
                    !fileName.toLowerCase().contains("property")) {
                throw new FileNotFoundException("file doesn't have accounts property");
            }

            FileInputStream fis = new FileInputStream(fileName);
            Sheet sheet = getSheetFromFile(fis, fileName);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                List<String> rowList = new ArrayList<>(2);

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    rowList.add(getStringAccountFromCell(cellIterator.next()));

                    if (rowList.size() > 1 && rowList.get(1).equals("true"))
                        accounts.add(rowList.get(0));
                }
            }
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public Set<String> getAccountsFromNewFiles(List<FileCost> newFiles) {

        Set<String> accounts = new HashSet<>();

        for (FileCost file : newFiles) {
            if (file.getFileName().contains("accounts"))
                accounts = createAccountsListFromExcelData(file.getFile().getAbsolutePath());
        }
        return accounts;
    }

    public String getFilePath(TypeCosts typeCosts, List<FileCost> newFiles) {
        for (FileCost fileDescription : newFiles) {
            if (fileDescription.getTypeCosts() == typeCosts)
                return fileDescription.getFile().getAbsolutePath();
        }
        return "";
    }

    private Sheet getSheetFromFile(FileInputStream fis, String fileName) throws IOException {

        Workbook workbook = null;
        if(fileName.toLowerCase().endsWith("xlsx")){
            workbook = new XSSFWorkbook(fis);
        }else if(fileName.toLowerCase().endsWith("xls")){
            workbook = new HSSFWorkbook(fis);
        }

        return workbook.getSheetAt(0);
    }

    private String getStringAccountFromCell(Cell cell) {

        String cellStr = cell.toString().trim();
        return cellStr.contains(".") ? cellStr.substring(0, cellStr.indexOf(".")) : cellStr;
    }

    private List<List<String>> getCleanRows(int startRow, int startColumn, List<List<String>> rows){

        List<List<String>> rowsClean = new ArrayList<>();

        for (int i = startRow; i < rows.size(); i++) {
            List<String> currentRow = rows.get(i);
            List<String> newCleanRow = currentRow.subList(startColumn, currentRow.size());
            if (newCleanRow.size() > 0)
                rowsClean.add(newCleanRow);
        }
        return rowsClean;
    }

    private List<List<String>> readExcelData(String fileName) {

        List<List<String>> rows = new ArrayList<>();
        int startColumn = 0;
        int startRow = 0;

        try {
            FileInputStream fis = new FileInputStream(fileName);
            Sheet sheet = getSheetFromFile(fis, fileName);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                List<String> currentRow = new ArrayList<>();

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                String currentCell = "";
                while (cellIterator.hasNext() && !currentCell.equals("END")) {
                    Cell cell = cellIterator.next();
                    currentCell = cell.toString().trim();
                    currentRow.add(currentCell);

                    if (currentCell.equals("START")) {
                        startColumn = cell.getColumnIndex();
                        startRow = cell.getRowIndex();
                    }
                }
                rows.add(currentRow);
            }
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return getCleanRows(startRow, startColumn, rows);
    }

    public String getFileNameFromFilePath(String filePath) {

        String[] filePathArray = filePath.split("/");
        return filePathArray[filePathArray.length - 1];
    }

    public TypeCosts getTypeCostsFromFileName(String fileName) {
        for (TypeCosts typeCosts : Arrays.asList(TypeCosts.values()))
            if (fileName.toUpperCase().contains(typeCosts.toString()))
                return typeCosts;

        return null;
    }

    //test
    public static void main (String[] args){
        List<String> cells = Arrays.asList("123.45678", "0.12345678", "12345678");

        for (String cell : cells) {
            System.out.println(cell + " : " + Double.parseDouble(cell));
            System.out.println(cell + " : " + Double.parseDouble(cell) * 100);
            System.out.println(cell + " : " + Math.round(Double.parseDouble(cell) * 100));
            System.out.println(cell + " : " + (double) Math.round(Double.parseDouble(cell) * 100) / 100);
        }
    }
}
