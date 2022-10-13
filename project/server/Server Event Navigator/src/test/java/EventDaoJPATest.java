import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import ru.server.ServerApp;
import ru.server.dao.EventDao;
import ru.server.dao.UserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.server.domain.Event;
import ru.server.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ServerApp.class)
@DisplayName("Класс EventDao")
@DataJpaTest
public class EventDaoJPATest {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserDao userDao;

    // Необходим для запуска тестов
    @PersistenceContext
    private EntityManager entityManager;

    // Данные для создания мероприятий
    final String name = "name";
    final String login = "l";
    final String password = "p";
    final String authorName = "author";
    final String description = "description";
    final String date = "date";
    final String place = "place";
    final double x = 2;
    final double y = 3;
    final int isStreet = 1;
    final int isGroup = -1;
    final int isFamily = 0;
    final int isFree = 6;
    final int hasCovid = 4;
    final int hasRegister = 5;
    final int isSport = 11;
    final int hasAgeRestrictions = 12;

    // Переменная count отвечает за параметр id у мероприятий и пользователей ввиду специфики работы базы данных
    static int count = 0;

    @DisplayName("Получает мероприятия")
    @Test
    void getAll() {
        count++;
        int existingId = count;
        int authorId = count;
        User expectedAuthor = User.builder().id(authorId).login(login).password(password).name(authorName).build();
        Event expectedEvent = Event.builder().id(existingId).date(date).coordX(x).coordY(y).description(description).hasAgeRestrictions(hasAgeRestrictions)
                .hasCovid(hasCovid).isFamily(isFamily).isFree(isFree).isGroup(isGroup).isSport(isSport).users(expectedAuthor)
                .isStreet(isStreet).name(name).place(place).hasRegister(hasRegister).build();
        userDao.save(expectedAuthor);
        eventDao.save(expectedEvent);
        List<Event> expectedResult = new ArrayList<>();
        expectedResult.add(expectedEvent);
        List<Event> result = eventDao.findAll();
        assertThat(result).isEqualTo(expectedResult);
    }

    @DisplayName("Добавляет мероприятие")
    @Test
    void addEvent() {
        count++;
        int existingId = count;
        int authorId = count;
        User expectedAuthor = User.builder().id(authorId).login(login).password(password).name(authorName).build();
        Event expectedEvent = Event.builder().id(existingId).date(date).coordX(x).coordY(y).description(description).hasAgeRestrictions(hasAgeRestrictions)
                .hasCovid(hasCovid).isFamily(isFamily).isFree(isFree).isGroup(isGroup).isSport(isSport).users(expectedAuthor)
                .isStreet(isStreet).name(name).place(place).hasRegister(hasRegister).build();
        userDao.save(expectedAuthor);
        eventDao.save(expectedEvent);
        assertThat(eventDao.getById(existingId)).isEqualTo(expectedEvent);
    }

    @DisplayName("Удаляет мероприятие")
    @Test
    void deleteEvent() {
        count++;
        int existingId = count;
        int authorId = count;
        User expectedAuthor = User.builder().id(authorId).login(login).password(password).name(authorName).build();
        Event expectedEvent = Event.builder().id(existingId).date(date).coordX(x).coordY(y).description(description).hasAgeRestrictions(hasAgeRestrictions)
                .hasCovid(hasCovid).isFamily(isFamily).isFree(isFree).isGroup(isGroup).isSport(isSport).users(expectedAuthor)
                .isStreet(isStreet).name(name).place(place).hasRegister(hasRegister).build();
        userDao.save(expectedAuthor);
        eventDao.save(expectedEvent);
        eventDao.deleteById(existingId);
        List<Event> all = eventDao.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

}
