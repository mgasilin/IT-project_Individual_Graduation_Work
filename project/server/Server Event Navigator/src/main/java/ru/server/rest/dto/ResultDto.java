package ru.server.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public

// Класс для отправки результата регистрации на мероприятие, либо для отправки ее статуса
class ResultDto {
    private int result;
}