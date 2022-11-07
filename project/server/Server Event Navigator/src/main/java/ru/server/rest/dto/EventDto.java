package ru.server.rest.dto;

import ru.server.domain.Event;
import ru.server.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

// Класс, отвечающий за обработку мероприятий перед отправкой
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String date;
    private long id;
    private UserDto user;
    private String name;
    private String description;
    private String place;
    private double coordX;
    private double coordY;
    private int isStreet;
    private int isGroup;
    private int isFamily;
    private int isFree;
    private int hasCovid;
    private int hasRegister;
    private int isSport;
    private int hasAgeRestrictions;
    private List<CommentDto> comments;
    private List<UserDto> registers;
    private int register;
    private int limit;
    private int has_limit;

    // Преобразование мероприятия в другой класс перед отправкой.
    public static EventDto toDto(Event event) {
        return new EventDtoBuilder()
                .coordX(event.getCoordX())
                .coordY(event.getCoordY())
                .date(event.getDate())
                .description(event.getDescription())
                .name(event.getName())
                .isFamily(event.getIsFamily())
                .hasCovid(event.getHasCovid())
                .id(event.getId())
                .user(UserDto.toDto(User.builder().id(event.getUsers().getId()).name(event.getUsers().getName()).build()))
                .place(event.getPlace())
                .isStreet(event.getIsStreet())
                .isGroup(event.getIsGroup())
                .isFree(event.getIsFree())
                .hasRegister(event.getHasRegister())
                .isSport(event.getIsSport())
                .hasAgeRestrictions(event.getHasAgeRestrictions())
                .build();
    }

    // Преобразование комментария в класс перед отправкой с учетом параметров записи
    public static EventDto toDto_v2(Event event) {
        return new EventDtoBuilder()
                .coordX(event.getCoordX())
                .coordY(event.getCoordY())
                .date(event.getDate())
                .description(event.getDescription())
                .name(event.getName())
                .isFamily(event.getIsFamily())
                .hasCovid(event.getHasCovid())
                .id(event.getId())
                .user(UserDto.toDto(User.builder().id(event.getUsers().getId()).name(event.getUsers().getName()).build()))
                .place(event.getPlace())
                .isStreet(event.getIsStreet())
                .isGroup(event.getIsGroup())
                .isFree(event.getIsFree())
                .hasRegister(event.getHasRegister())
                .isSport(event.getIsSport())
                .hasAgeRestrictions(event.getHasAgeRestrictions())
                .register(event.getRegistration())
                .limit(event.getLimit())
                .has_limit(event.getHas_limit())
                .registers(event.getRegistered().stream().map(UserDto::toDto_v2).collect(Collectors.toList()))
                .build();
    }
}
