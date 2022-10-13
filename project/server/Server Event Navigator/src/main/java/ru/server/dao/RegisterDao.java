package ru.server.dao;

import ru.server.domain.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Интерфейс для работы с регистрацией
public interface RegisterDao extends JpaRepository<Register, Integer> {
    List<Register> findAll();
}
