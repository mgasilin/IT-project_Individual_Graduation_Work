package ru.server.rest.controller;

import ru.server.rest.dto.LoginDto;
import ru.server.rest.dto.UserDto;
import ru.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    // Сервис для работы с пользователями
    private final UserService userService;

    @GetMapping("/user/{id}")
    public UserDto getUser(@PathVariable int id){
        return UserDto.toDto(userService.getById(id));
    }

    @GetMapping("/user/login")
    public LoginDto login(@RequestParam String login, @RequestParam String password){
        return userService.login(login,password);
    }

    @GetMapping("/user/register")
    public LoginDto register(@RequestParam String login, @RequestParam String password, @RequestParam String username){
        return  userService.register(login,password,username);
    }
}
