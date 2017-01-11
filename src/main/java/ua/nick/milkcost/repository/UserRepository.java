package ua.nick.milkcost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.milkcost.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
