package ua.nick.milkcost.service;

import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.TypeCosts;

import java.util.Date;

public interface CostService {

    void createCostStructures();
    CostStructure saveNewCostStructure(CostStructure costStructure);
    CostStructure getCostStructure(TypeCosts type, Date monthYear);
}
