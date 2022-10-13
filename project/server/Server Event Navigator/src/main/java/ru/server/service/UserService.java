package ru.server.service;

import ru.server.domain.User;
import ru.server.rest.dto.LoginDto;

public interface UserService {
    LoginDto login(String login, String password);

    LoginDto register(String login, String password, String username);

    User getById(int id);
}
