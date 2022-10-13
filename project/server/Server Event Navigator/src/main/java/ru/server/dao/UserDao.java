package ru.server.dao;

import ru.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Интерфейс для работы с пользователями
public interface UserDao extends JpaRepository<User, Integer> {

}
