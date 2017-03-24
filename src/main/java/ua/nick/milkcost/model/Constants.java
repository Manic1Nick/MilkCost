package ua.nick.milkcost.model;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    private Constants(){}

    /*//take accounts and indicators
    //accounts for report
    public static final String ACCOUNTS_PROPERTY_EXCEL_FILE_LOCATION_PATH =
            "/home/jessy/IdeaProjects/MilkCost/files/accounts_property.xls";
    //indicators what we want get
    public static final String INDICATORS_EXCEL_FILE_LOCATION_PATH =
            "/home/jessy/IdeaProjects/MilkCost/indicators_test.xls";

    //read costs
    //direct costs
    public static final String COST_DIRECT_EXCEL_FILE_LOCATION_PATH =
            "/home/jessy/IdeaProjects/data_xls/2311_2016_10.xls";
    //overhead costs
    public static final String COST_OVERHEAD_EXCEL_FILE_LOCATION_PATH =
            "/home/jessy/IdeaProjects/data_xls/912_2016_10.xls";
    //additional costs, and other data
    public static final String ADDITIONAL_EXCEL_FILE_LOCATION_PATH =
            "/home/jessy/IdeaProjects/data_xls/additional_2016_10.xls";

    //write data
    public static final String RESULT_EXCEL_FILE_LOCATION_PATH =
            "/home/jessy/IdeaProjects/data_xls/result_2016_10.xls";*/

    public static final String FOLDER_WITH_FILES_FOR_MILK_COST =
            "/home/jessy/IdeaProjects/MilkCost2/files";

    public static final String[] COLORS_12_FOR_DIAGRAM =
            {"#50ADF5", "#FF7965", "#FFCB45", "#6877e5", "#6FB07F", "#e07d26",
                    "#29f1d2", "#e22bef", "#e43e29", "#29bfe4", "#29e45c", "#ebea2f"};

    public static final Map<String, String> MONTHS_MAP = new HashMap<String, String>()
    {{
        put("1", "January");
        put("2", "February");
        put("3", "Mart");
        put("4", "April");
        put("5", "May");
        put("6", "June");
        put("7", "Jule");
        put("8", "August");
        put("9", "September");
        put("10", "October");
        put("11", "November");
        put("12", "December");

        put("TOTAL", "TOTAL");

        put("01", "January");
        put("02", "February");
        put("03", "Mart");
        put("04", "April");
        put("05", "May");
        put("06", "June");
        put("07", "Jule");
        put("08", "August");
        put("09", "September");
    }};
    }
