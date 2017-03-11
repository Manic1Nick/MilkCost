package ua.nick.milkcost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.FileDescription;
import ua.nick.milkcost.model.TypeCosts;
import ua.nick.milkcost.service.CostService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    private CostService costService;

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(@ModelAttribute("message") String message, Model model) {

        if (message.equals(""))
            message = null;

        model.addAttribute("message", message);

        return "welcome";
    }

    @RequestMapping(value = "/costs/add", method = RequestMethod.GET)
    public String addNewCosts(@ModelAttribute("message") String message, Model model, Principal principal) {

        //check new files in work folder
        List<FileDescription> newFiles = costService.getNewFiles();
        if (newFiles == null || newFiles.size() == 0)
            model.addAttribute("message", "There are no new files in work folder");
        else
            costService.createCostStructures(newFiles);
            model.addAttribute("message", "New files was added to data base");

        return "welcome";
    }

    @RequestMapping(value = "/costs/get", method = RequestMethod.GET)
    public String getCosts(Model model, HttpServletRequest req) {

        String typeCostsStr = req.getParameter("type");
        TypeCosts typeCosts = costService.findTypeCostsByString(typeCostsStr);

        String text = req.getParameter("period");
        Date period = costService.getDateFromString(text);

        //need this or not?
        if (typeCosts == null || period == null){
            model.addAttribute("message", "Costs getting error");
            return "welcome";
        }

        CostStructure costStructure = costService.getCostStructure(typeCosts, period);
        model.addAttribute("costStructure", costStructure);

        return "costs";
    }
}
