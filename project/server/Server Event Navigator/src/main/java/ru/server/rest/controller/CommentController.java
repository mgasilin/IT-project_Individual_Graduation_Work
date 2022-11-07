package ru.server.rest.controller;

import ru.server.domain.Comment;
import ru.server.rest.dto.CommentDto;
import ru.server.service.CommentService;
import ru.server.service.EventService;
import ru.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {

    // Сервисы для работы с сущностями
    private final EventService eventService;
    private final CommentService commentService;
    private final UserService userService;

    // Создание комментария
    @PostMapping("/events/comment/new")
    public List<CommentDto> createNewComment(
            @RequestParam String content,
            @RequestParam int event_id,
            @RequestParam int author_id
    ) {
        try {
            List<Comment> comments = commentService.findByEvent(eventService.getById(event_id));
            Comment comment = commentService.insert(Comment.builder().content(content).event(eventService.getById(event_id)).author(userService.getById(author_id)).sequence_number(comments.size()+1).build());
            List<Comment> commentList = commentService.findByEvent(comment.getEvent());
            return commentList.stream().map(CommentDto::toDto).collect(Collectors.toList());
        } catch (Exception e){
            return null;
        }
    }

    // Получение комментариев к мероприятию по айди мероприятия
    @GetMapping("/events/{id}/comments")
    public List<CommentDto> getCommentsByBookId(@PathVariable int id) {
        try {
            return commentService
                    .findByEvent(eventService.getById(id))
                    .stream()
                    .map(CommentDto::toDto)
                    .collect(Collectors.toList());
        }catch (EntityNotFoundException e){
            return null;
        }
    }
}
