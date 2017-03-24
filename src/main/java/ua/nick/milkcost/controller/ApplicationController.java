package ua.nick.milkcost.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.nick.milkcost.model.*;
import ua.nick.milkcost.service.CostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
public class ApplicationController {

    @Autowired
    private CostService costService;

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(@ModelAttribute("message") String message, Model model) {

        if (message.equals(""))
            message = null;

        model.addAttribute("message", message);

        List<Period> periods = costService.getAllPeriodsFromDB();
        model.addAttribute("periods", sortListPeriods(periods));

        return "welcome";
    }

    @RequestMapping(value = "/costs/add", method = RequestMethod.GET)
    public String addNewCosts(@ModelAttribute("message") String message, Model model) {

        //check new files in work folder
        List<FileCost> newFiles = costService.getNewFiles();
        if (newFiles == null || newFiles.size() == 0) {
            model.addAttribute("message", "There are no new files in work folder");
        } else {
            costService.saveNewCosts(newFiles);
            model.addAttribute("message", "New files was added to data base");
        }

        List<Period> periods = costService.getAllPeriodsFromDB();
        model.addAttribute("periods", sortListPeriods(periods));

        return "welcome";
    }

    @RequestMapping(value = "/costs/get/all", method = RequestMethod.GET)
    public String getCosts(Model model, HttpServletRequest req) {

        Map<String, Map<String, Double>> mapCosts = new HashMap<>();

        String year = req.getParameter("year");
        List<Period> periods = costService.getPeriodsByYear(year);

        for (Period period : periods)
            mapCosts.put(period.formatYYYYdashMM(), costService.getMapCostsByPeriodIdAndTypeCosts(
                    period.getId(), TypeCosts.TOTAL));

        model.addAttribute("costs", mapCosts);
        model.addAttribute("names", new ArrayList<>(mapCosts.get(periods.get(0).formatYYYYdashMM()).keySet()));

        //row TOTAL COSTS
        Map<String, Double> totalCosts = costService.createTotalMapFromMaps(mapCosts);
        model.addAttribute("totalCosts", totalCosts);

        return "costs";
    }

    @RequestMapping(value = "/costs/get/one", method = RequestMethod.GET)
    public String getCostDiagram(Model model, HttpServletRequest req) {

        String[] yearMonth = req.getParameter("period").split("-");

        Period period = costService.getPeriodByYearAndMonth(yearMonth[0], yearMonth[1]);
        List<Cost> costStructure = costService.createCostStructure(period);

        Map<String, Map<String, Double>> mapCosts = new HashMap<>();
        mapCosts.put(period.formatYYYYdashMM(), costService.getMapCostsByListCosts(costStructure));

        model.addAttribute("period", period);
        model.addAttribute("structure", costStructure);
        model.addAttribute("costs", mapCosts);
        model.addAttribute("names", new ArrayList<>(mapCosts.get(period.formatYYYYdashMM()).keySet()));
        model.addAttribute("sumTotal", costService.calculateSumTotalFromListCosts(costStructure));

        return "cost_structure_vertical";
    }

    @RequestMapping(value = "/costs/get/total", method = RequestMethod.GET)
    public String getTotalCostDiagram(Model model, HttpServletRequest req) {

        String[] yearMonths = req.getParameter("period").split(",");
        List<Period> periods = new ArrayList<>();
        for (String period : yearMonths) {
            String[] yearMonth = period.split("-");
            periods.add(costService.getPeriodByYearAndMonth(yearMonth[0], yearMonth[1]));
        }

        List<Cost> costStructure = costService.createTotalCostStructure(periods);

        Map<String, Map<String, Double>> mapCosts = new HashMap<>();
        for (Period period : periods)
            mapCosts.put(period.formatYYYYdashMM(),
                    costService.getMapCostsByPeriodIdAndTypeCosts(period.getId(), TypeCosts.TOTAL));

        //row TOTAL COSTS
        Map<String, Double> totalCosts = costService.createTotalMapFromMaps(mapCosts);
        mapCosts = new HashMap<>();
        mapCosts.put("TOTAL", totalCosts);

        model.addAttribute("period", new Period(periods.get(0).getYear(), "TOTAL"));
        model.addAttribute("structure", costStructure);
        model.addAttribute("costs", mapCosts);
        model.addAttribute("names", new ArrayList<>(totalCosts.keySet()));
        model.addAttribute("sumTotal", costService.calculateSumTotalFromListCosts(costStructure));

        return "cost_structure_vertical";
    }

    @RequestMapping(value = "/cost/get/chart", method = RequestMethod.GET)
    public void getCostChartDynamic(HttpServletResponse resp, HttpServletRequest req)
            throws IOException {

        String costName = req.getParameter("name");
        int[] sums = costService.createDynamicArrayOfCost(costName);

        resp.getWriter().print(new Gson().toJson(sums));
    }

    @RequestMapping(value = "/costs/update/database", method = RequestMethod.GET)
    public String updateDatabase(@ModelAttribute("message") String message, Model model)
            throws IOException {

        //check new files in work folder
        List<FileCost> newFiles = costService.getNewFilesForUpdateDB();
        if (newFiles == null || newFiles.size() == 0) {
            model.addAttribute("message", "There are no new files for update DB in work folder");
        } else {
            costService.saveNewCostsForUpdateDB(newFiles);
            model.addAttribute("message", "New files for update BD was added to data base");
        }

        List<Period> periods = costService.getAllPeriodsFromDB();
        model.addAttribute("periods", sortListPeriods(periods));

        return "welcome";
    }

    @RequestMapping(value = "/costs/compare", method = RequestMethod.GET)
    public String compareCosts(@ModelAttribute("message") String message, HttpServletRequest req, Model model)
            throws IOException {

        String[] textPeriodIds = req.getParameterValues("comparingCosts");

        if (textPeriodIds == null || textPeriodIds.length != 2) {
            model.addAttribute("message", checkCostsCompare(textPeriodIds));
            return "redirect:/welcome";
        }

        Map<String, Map<String, Double>> mapCosts = new HashMap<>();
        for (String text : textPeriodIds) {
            Period period = costService.getPeriodById(Long.valueOf(text));
            List<Cost> costStructure = costService.createCostStructure(period);
            mapCosts.put(period.formatYYYYdashMM(), costService.getMapCostsByListCosts(costStructure));
        }

        List<String> periods = new ArrayList<>(mapCosts.keySet());

        model.addAttribute("costs", mapCosts);
        model.addAttribute("names", new ArrayList<>(mapCosts.get(periods.get(0)).keySet()));
        model.addAttribute("difference", costService.getMapDifferenceCostsFor2Periods(mapCosts));
        model.addAttribute("previous", new Gson().toJson(mapCosts.get(periods.get(1)).values()));
        model.addAttribute("next", new Gson().toJson(mapCosts.get(periods.get(0)).values()));

        return "costs_compare2";
    }

    @RequestMapping(value = "/cost/history", method = RequestMethod.GET)
    public String getCostHistory(Model model, HttpServletRequest req) {

        String name = req.getParameter("name");

        List<Period> periods = costService.getAllPeriodsFromDB();
        Collections.sort(periods);

        List<String> dates = new ArrayList<>(periods.size());
        List<Double> sums = new ArrayList<>(periods.size());
        for (Period period : periods) {
            Cost cost = costService.getCostByPeriodIdAndNameCostsAndTypeCosts(
                    period.getId(), NameCosts.valueOf(name), TypeCosts.TOTAL);

            dates.add(period.formatYYYYdashMM());
            sums.add(cost.getSum());
        }

        model.addAttribute("costName", name);
        model.addAttribute("dates", dates);
        model.addAttribute("sums", sums);

        return "cost_history";
    }

    @RequestMapping(value = "/cost/history/all", method = RequestMethod.GET)
    public String getAllCostsHistory(Model model, HttpServletRequest req) {

        List<Period> periods = costService.getAllPeriodsFromDB();
        Collections.sort(periods);

        Map<String, Map<String, List<Double>>> mapNamesColorsListsOfSums = new HashMap<>();
        List<NameCosts> allNames = Arrays.asList(NameCosts.values());

        for (NameCosts nameCosts : allNames) {
            Map<String, List<Double>> mapColorsListsOfSums = new HashMap<>();
            List<Double> list = new ArrayList<>();

            for (Period period : periods) {
                Cost cost = costService.getCostByPeriodIdAndNameCostsAndTypeCosts(
                        period.getId(), nameCosts, TypeCosts.TOTAL);
                list.add(cost.getSum());
            }
            mapColorsListsOfSums.put(Constants.COLORS_12_FOR_DIAGRAM[allNames.indexOf(nameCosts)], list);
            mapNamesColorsListsOfSums.put(nameCosts.name(), mapColorsListsOfSums);
        }

        model.addAttribute("minDate", periods.get(0).formatYYYYdashMM());
        model.addAttribute("mapCosts", mapNamesColorsListsOfSums);

        return "cost_history_all";
    }

    private List<Period> sortListPeriods(List<Period> periods) {
        Collections.sort(periods, (Period p1, Period p2) -> p2.getMonth().compareTo(p1.getMonth()));
        Collections.sort(periods, (Period p1, Period p2) -> p2.getYear().compareTo(p1.getYear()));

        return periods;
    }

    private String checkCostsCompare(String[] textPeriodIds) {
        String message = "";

        if (textPeriodIds == null || textPeriodIds.length == 0)
            message = "No selected periods";

        else if (textPeriodIds.length != 2)
            message = "Please select only 2 periods for compare";

        return message;
    }
}
