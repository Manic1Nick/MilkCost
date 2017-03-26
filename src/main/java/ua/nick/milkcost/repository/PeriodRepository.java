package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.Period;

import java.util.List;

public interface PeriodRepository extends JpaRepository<Period, Long> {
    List<Period> findAll();
    Period findById(Long id);
    List<Period> findByYear(String year);
    List<Period> findByMonth(String month);
    Period findByYearAndMonth(String year, String month);
}

