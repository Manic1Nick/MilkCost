package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.TypeCosts;

import java.time.YearMonth;

public interface CostStructureRepository extends JpaRepository<CostStructure, YearMonth> {
    CostStructure findByTypeCostsAndYearMonth(TypeCosts typeCost, YearMonth monthYear);
}
