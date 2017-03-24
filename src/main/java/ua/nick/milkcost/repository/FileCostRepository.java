package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.FileCost;
import ua.nick.milkcost.model.TypeCosts;

import java.util.List;

public interface FileCostRepository extends JpaRepository<FileCost, Long> {
    List<FileCost> findAll();
    List<FileCost> findByTypeCostsIsNotNull();
    List<FileCost> findByPeriodId(Long periodId);
    List<FileCost> findByPeriodIdAndTypeCosts(Long periodId, TypeCosts typeCosts);
    FileCost findByFileName(String fileName);
}
