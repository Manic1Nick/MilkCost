package ua.nick.milkcost.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class DateUtil {
    //DateTimeFormatter formatter;

    public DateUtil() {
    }

    /*public YearMonth getYearMonthFromString(String monthPointYear) {
        formatter = DateTimeFormatter.ofPattern("MM.yyyy");
        return YearMonth.parse(monthPointYear, formatter);
    }

    public YearMonth getYearMonthFromFilePath(String filePath) {
        YearMonth mmYYYY = null;

        String[] filePathArray = filePath.split("/");
        String fileName = filePathArray[filePathArray.length - 1];
        String[] dateArray = fileName.substring(0, fileName.lastIndexOf(".")).split("_");

        if (dateArray.length >= 3)
            mmYYYY = getYearMonthFromString(String.format("%s.%s",
                    dateArray[dateArray.length-1],
                    dateArray[dateArray.length-2]));

        return mmYYYY;
    }*/

    public Date getDateFromFilePath(String filePath) {

        String[] filePathArray = filePath.split("/");
        String fileName = filePathArray[filePathArray.length - 1];
        String[] dateArray = fileName.substring(0, fileName.lastIndexOf(".")).split("_");

        return getDateFromArray(dateArray);
    }

    public Date getDateFromString(String text) {

        return getDateFromArray(text.split("-"));
    }

    private Date getDateFromArray(String[] array) {

        Date date = null;

        if (array.length >= 3) {
            LocalDate localDate = LocalDate.of(
                    Integer.parseInt(array[array.length-2]),
                    Integer.parseInt(array[array.length-1]),
                    1);
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        return date;
    }
}
