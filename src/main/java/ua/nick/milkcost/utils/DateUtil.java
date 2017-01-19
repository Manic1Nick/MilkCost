package ua.nick.milkcost.utils;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    DateTimeFormatter formatter;

    public DateUtil() {
    }

    public YearMonth getYearMonthFromString(String monthPointYear) {
        formatter = DateTimeFormatter.ofPattern("MM.yyyy");
        return YearMonth.parse(monthPointYear, formatter);
    }
}
