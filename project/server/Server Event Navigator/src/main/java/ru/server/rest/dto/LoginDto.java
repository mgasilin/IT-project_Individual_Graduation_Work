package ru.server.rest.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Класс, отвечающий за отправку результата входа/регистрации пользователя
@Data
@Builder
@NoArgsConstructor
public class LoginDto{
    private int result;
    private int id;

    // Конструктор
    public LoginDto(int result, int id){
        this.result=result;
        this.id=id;
    }
}