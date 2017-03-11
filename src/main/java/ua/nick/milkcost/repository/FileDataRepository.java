package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.FileDescription;

import java.util.List;

public interface FileDataRepository extends JpaRepository<FileDescription, Long> {
    List<FileDescription> findAll();
}
