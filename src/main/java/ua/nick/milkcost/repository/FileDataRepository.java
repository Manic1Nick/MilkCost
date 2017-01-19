package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.FileDescription;

import java.time.YearMonth;

public interface FileDataRepository extends JpaRepository<FileDescription, YearMonth> {

}
