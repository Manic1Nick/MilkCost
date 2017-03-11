package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.CostStructure;
import ua.nick.milkcost.model.TypeCosts;

import java.util.Date;

public interface CostStructureRepository extends JpaRepository<CostStructure, Long> {
    CostStructure findById(Long id);
    CostStructure findByTypeCostsAndDate(TypeCosts typeCosts, Date date);
}
