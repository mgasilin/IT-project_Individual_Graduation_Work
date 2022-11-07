package ru.server.rest.dto;

import ru.server.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Класс, отвечающий за преобразование комментариев перед отправкой
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    // Поля класса
    private int id;

    private String content;

    private UserDto user;

    private int sequence_number;

    private int event_id;

    // Преобразование комментария в класс перед отправкой.
    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getContent(), UserDto.toDto(comment.getAuthor()),comment.getSequence_number(), comment.getEvent().getId());
    }

}

