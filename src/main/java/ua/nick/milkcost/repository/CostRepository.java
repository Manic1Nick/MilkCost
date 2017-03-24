package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.Cost;
import ua.nick.milkcost.model.NameCosts;
import ua.nick.milkcost.model.TypeCosts;

import java.util.List;

public interface CostRepository extends JpaRepository<Cost, Long> {
    Cost findById(Long id);
    List<Cost> findByPeriodId(Long periodId);
    List<Cost> findByPeriodIdAndNameCosts(Long periodId, NameCosts nameCosts);
    List<Cost> findByPeriodIdAndTypeCosts(Long periodId, TypeCosts typeCosts);
    List<Cost> findByPeriodIdAndNameCostsAndTypeCosts(Long periodId, NameCosts nameCosts, TypeCosts typeCosts);
    List<Cost> findByNameCosts(NameCosts nameCosts);
}
