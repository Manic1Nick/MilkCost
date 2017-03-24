package ua.nick.milkcost.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "periods")
public class Period implements Comparable<Period> {

    private Long id;
    private String year;
    private String month;
    private String monthName;
    private boolean updated;

    public Period() {
    }

    public Period(String year, String month) {
        this.year = year;
        this.month = month;
        this.monthName = Constants.MONTHS_MAP.get(this.month);
    }

    public Period(String fileName) {
        this.year = determineYearOfFile(fileName);
        this.month = determineMonthOfFile(fileName);
        this.monthName = Constants.MONTHS_MAP.get(this.month);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthName() {
        if (monthName == null)
            setMonthName(Constants.MONTHS_MAP.get(this.month));

        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    @Override
    public int compareTo(Period o) {
        return this.inLocalDate().compareTo(o.inLocalDate());
    }

    public String formatYYYYdashMM() {
        return this.getYear() + "-" + this.getMonth();
    }

    public LocalDate inLocalDate() {
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
    }

    private String determineYearOfFile(String fileName) {

        String[] array = fileName.substring(0, fileName.lastIndexOf(".")).split("_");

        if (array.length >= 3)
            return array[array.length-2];

        return "";
    }

    private String determineMonthOfFile(String fileName) {

        String[] array = fileName.substring(0, fileName.lastIndexOf(".")).split("_");

        if (array.length >= 3)
            return array[array.length-1];

        return "";
    }
}
