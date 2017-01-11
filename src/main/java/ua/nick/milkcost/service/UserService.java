package ua.nick.milkcost.service;

import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.TypeCost;

import java.text.ParseException;
import java.util.Date;

public interface UserService {

    CostStructure createNewCostStructure() throws ParseException;
    CostStructure saveNewCostStructure(CostStructure costStructure);
    CostStructure getCostStructure(TypeCost typeCost, Date monthYear);
}
