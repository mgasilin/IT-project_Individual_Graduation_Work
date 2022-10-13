package ru.server.service;

import ru.server.dao.UserDao;
import ru.server.domain.User;
import ru.server.rest.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {


    private final UserDao userDao;

    @Override
    public LoginDto login(String login, String password) {
        System.out.println("ins " + login + " " + password);
        List<User> users = userDao.findAll();
        // 3-нет пользователя, 2-неверный пароль, 1-успешно
        int res = 3;
        int id = -1;
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                if (user.getPassword().equals(password)) {
                    res = 1;
                    id = user.getId();
                } else {
                    res = 2;
                }
                break;
            }
        }
        return new LoginDto(res, id);
    }

    @Transactional
    @Override
    public LoginDto register(String login, String password, String username) {
        List<User> users = userDao.findAll();
        // 1 - успех, 2 - занято имя, 3 - занят логин, 4 - заняты и имя, и логин
        int result = 1;
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getName().equals(username)) {
                result = 4;
            } else if (user.getLogin().equals(login)) {
                result = 3;
            } else if (user.getName().equals(username)) {
                result = 2;
            }
        }
        int id = -1;
        if (result == 1) {
            id = userDao.save(User.builder()
                    .name(username)
                    .login(login)
                    .password(password)
                    .build()).getId();
        }
        return new LoginDto(result, id);
    }

    @Override
    public User getById(int id) {
        return userDao.getById(id);
    }
}
