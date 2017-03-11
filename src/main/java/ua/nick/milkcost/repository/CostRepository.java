package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.Cost;

public interface CostRepository extends JpaRepository<Cost, Long> {
    Cost findById(Long id);
}
