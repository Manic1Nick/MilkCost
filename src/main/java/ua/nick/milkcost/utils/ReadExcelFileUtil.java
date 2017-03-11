package ua.nick.milkcost.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ua.nick.milkcost.model.FileDescription;
import ua.nick.milkcost.model.TypeCosts;

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

    public Map<String, Double> getMapFromRows(Set<String> setMarkers, String fileName) {

        List<List<String>> rows = readExcelData(fileName);

        Map<String, Double> mapData = new HashMap<>();
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
                    rowSumm = cell.isEmpty() ? rowSumm : rowSumm + Double.parseDouble(cell);
                }
            }

            String currentMarker = currentRow.get(0);
            if (mapData.keySet().contains(currentMarker)) {
                rowSumm += mapData.get(currentMarker);
            }
            mapData.put(currentMarker, rowSumm);
        }

        return mapData;
    }

    public Map<String, Double> getMapFromColumns(String marker, String fileName) {

        List<List<String>> rows = readExcelData(fileName);

        Map<String, Double> mapData = new HashMap<>();

        for (int i = 1; i < rows.size(); i++) {

            while (rows.get(i).get(0).isEmpty()) {
                i++;
            }

            List<String> currentRow = rows.get(i);

            if (currentRow.get(0).equals(marker)) {
                Double value = Double.parseDouble(currentRow.get(currentRow.size()-1));

                //specific account
                value = currentRow.get(1).contains("KV") ? value / 3 : value ;
                value = currentRow.get(1).contains("REPAIRS") ? value * 0.4 : value ;

                mapData.put(currentRow.get(1), value);
            }
            if (currentRow.get(0).equals("END")) {
                i = rows.size();
            }
        }

        return mapData;
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

    public Set<String> getAccountsFromNewFiles(List<FileDescription> newFiles) {

        Set<String> accounts = new HashSet<>();

        for (FileDescription file : newFiles) {
            if (file.getFileName().contains("accounts"))
                accounts = createAccountsListFromExcelData(file.getFile().getAbsolutePath());
        }
        return accounts;
    }

    public String getFilePath(TypeCosts typeCosts, List<FileDescription> newFiles) {
        for (FileDescription fileDescription : newFiles) {
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
}
