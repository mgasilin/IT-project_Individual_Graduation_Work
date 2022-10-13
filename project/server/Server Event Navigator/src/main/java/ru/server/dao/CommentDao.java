package ru.server.dao;

import ru.server.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Интерфейс для работы с комментариями
public interface CommentDao extends JpaRepository<Comment, Integer> {
    List<Comment> findByEventId(int id);
}

