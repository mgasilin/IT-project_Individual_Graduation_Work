package ru.server.dao;

import ru.server.domain.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Интерфейс для работы с мероприятиями
public interface EventDao extends JpaRepository<Event, Integer> {

    @Override
    @EntityGraph(attributePaths = {"users", "comments"})
    List<Event> findAll();
}
