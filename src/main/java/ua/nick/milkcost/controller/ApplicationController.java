package ua.nick.milkcost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.TypeCost;
import ua.nick.milkcost.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ApplicationController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your userphone or password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(@ModelAttribute("message") String message, Model model, Principal principal) {

        if (message.equals(""))
            message = null;

        model.addAttribute("message", message);

        return "welcome";
    }

    @RequestMapping(value = "/costs/add", method = RequestMethod.GET)
    public String addNewCosts(@ModelAttribute("message") String message, Model model, Principal principal) {

        CostStructure costStructure = null;
        try {
            costStructure = userService.createNewCostStructure();
        } catch (ParseException e) {
            model.addAttribute("message", "Costs added error");
            return "welcome";
        }

        CostStructure newCostStructure = userService.saveNewCostStructure(costStructure);
        model.addAttribute("message", "Costs " + newCostStructure.getMonthYear() + " was added");

        return "welcome";
    }

    @RequestMapping(value = "/costs/get", method = RequestMethod.GET)
    public String getCosts(Model model, HttpServletRequest req) {

        String typeCostStr = req.getParameter("type");
        TypeCost typeCost = typeCostStr.equals("TOTAL") ? TypeCost.TOTAL :
                        typeCostStr.equals("OVERHEAD") ? TypeCost.OVERHEAD :
                        typeCostStr.equals("DIRECT") ? TypeCost.DIRECT : null ;

        String dateMonthYear = "2016-10";
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        try {
            date = df.parse(dateMonthYear);
        } catch (ParseException e) {
            model.addAttribute("message", "Costs getting error");
            return "welcome";
        }

        CostStructure costStructure = userService.getCostStructure(typeCost, date);
        model.addAttribute("costStructure", costStructure);

        return "costs";
    }
}
