package ru.server.rest.dto;

import ru.server.domain.Register;
import ru.server.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Класс, отвечающий за обработку пользователей и записей перед отправкой
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;
    private String name;
    // Преобразует пользователей перед отправкой
    public static UserDto toDto(User user) {
        return UserDto.builder().id(user.getId()).name(user.getName()).build();
    }

    // Преобразует запись перед отправкой
    public static UserDto toDto_v2(Register register) {
        return UserDto.toDto(register.getUser());
    }
}
