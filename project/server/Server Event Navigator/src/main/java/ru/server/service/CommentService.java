package ru.server.service;

import ru.server.domain.Comment;
import ru.server.domain.Event;

import java.util.List;

public interface CommentService {
    List<Comment> findByEvent(Event event);
    Comment insert(Comment comment);
}
