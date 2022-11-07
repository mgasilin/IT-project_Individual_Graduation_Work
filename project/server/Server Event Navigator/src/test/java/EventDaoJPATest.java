import org.opentest4j.TestAbortedException;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ServerApp.class)
@DisplayName("Класс EventDao")
@DataJpaTest
public class EventDaoJPATest {

    // Объекты для сохранения/получения сущностей
    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserDao userDao;

    // Необходим для запуска тестов
    @PersistenceContext
    private EntityManager entityManager;

    // Данные для создания мероприятий
    // Первое мероприятие + автор
    final String name = "name";
    final String login = "login";
    final String password = "password";
    final String authorName = "author";
    final String description = "description";
    final String date = "date";
    final String place = "place";
    final double x = 11;
    final double y = 12;
    final int isStreet = 13;
    final int isGroup = 14;
    final int isFamily = 15;
    final int isFree = 16;
    final int hasCovid = 17;
    final int hasRegister = 18;
    final int isSport = 19;
    final int hasAgeRestrictions = 10;

    // Второе мероприятие + автор
    final String second_name = "name2";
    final String second_login = "login2";
    final String second_password = "password2";
    final String second_authorName = "author2";
    final String second_description = "description2";
    final String second_date = "date2";
    final String second_place = "place2";
    final double second_x = 21;
    final double second_y = 22;
    final int second_isStreet = 23;
    final int second_isGroup = 24;
    final int second_isFamily = 25;
    final int second_isFree = 26;
    final int second_hasCovid = 27;
    final int second_hasRegister = 28;
    final int second_isSport = 29;
    final int second_hasAgeRestrictions = 20;

    // Третье мероприятие + автор
    final String third_name = "name3";
    final String third_login = "login3";
    final String third_password = "password3";
    final String third_authorName = "author3";
    final String third_description = "description3";
    final String third_date = "date3";
    final String third_place = "place3";
    final double third_x = 31;
    final double third_y = 32;
    final int third_isStreet = 33;
    final int third_isGroup = 34;
    final int third_isFamily = 35;
    final int third_isFree = 36;
    final int third_hasCovid = 37;
    final int third_hasRegister = 38;
    final int third_isSport = 39;
    final int third_hasAgeRestrictions = 30;

    // Переменная, отвечающая за количество мероприятий в базе данных т.е. за id мероприятий
    static int count = 0;

    @DisplayName("Получает мероприятия")
    @Test
    void getEvent() {
        // Создаем мероприятия
        count++;
        int id = count;
        User author_1 = User.builder().login(login).password(password).name(authorName).build();
        Event event_1 = Event.builder().name(name).users(author_1).description(description).date(date)
                .place(place).coordX(x).coordY(y).isStreet(isStreet).isGroup(isGroup)
                .isFamily(isFamily).isFree(isFree).hasCovid(hasCovid)
                .hasRegister(hasRegister).isSport(isSport).hasAgeRestrictions(hasAgeRestrictions).build();

        count++;
        int second_id = count;
        User author_2 = User.builder().login(second_login).password(second_password).name(second_authorName).build();
        Event event_2 = Event.builder().name(second_name).users(author_2).description(second_description).date(second_date)
                .place(second_place).coordX(second_x).coordY(second_y).isStreet(second_isStreet).isGroup(second_isGroup)
                .isFamily(second_isFamily).isFree(second_isFree).hasCovid(second_hasCovid)
                .hasRegister(second_hasRegister).isSport(second_isSport).hasAgeRestrictions(second_hasAgeRestrictions).build();

        count++;
        int third_id = count;
        User author_3 = User.builder().login(third_login).password(third_password).name(third_authorName).build();
        Event event_3 = Event.builder().name(third_name).users(author_3).description(third_description).date(third_date)
                .place(third_place).coordX(third_x).coordY(third_y).isStreet(third_isStreet).isGroup(third_isGroup)
                .isFamily(third_isFamily).isFree(third_isFree).hasCovid(third_hasCovid)
                .hasRegister(third_hasRegister).isSport(third_isSport).hasAgeRestrictions(third_hasAgeRestrictions).build();

        // Добавляем в базу данных
        userDao.save(author_1);
        eventDao.save(event_1);
        //Проверяем, что мероприятие сохранено верно
        List<Event> expected_list = new ArrayList<>();
        expected_list.add(event_1);
        Event actual_event = eventDao.getById(id);
        assertThat(event_1).isEqualTo(actual_event);
        List<Event> actual_events = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_events);

        // Повторяем для второго и третьего мероприятий
        userDao.save(author_2);
        eventDao.save(event_2);
        expected_list.add(event_2);
        actual_event = eventDao.getById(second_id);
        assertThat(event_2).isEqualTo(actual_event);
        actual_events = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_events);

        userDao.save(author_3);
        eventDao.save(event_3);
        expected_list.add(event_3);
        actual_event = eventDao.getById(third_id);
        assertThat(event_3).isEqualTo(actual_event);
        actual_events = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_events);

        /* Проверяем, что при получении несуществующего мероприятия выдастся верная ошибка
         Т.е. неправильные запросы не будут выполняться */
        boolean gotException = false;
        try {
            System.out.println(eventDao.getById(count + 10));
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(EntityNotFoundException.class);
            gotException = true;
        }
        if (!gotException) {
            // Если ошибка не возникнет, то тест не будет пройдет
            throw new TestAbortedException();
        }
    }

    @DisplayName("Добавляет мероприятие")
    @Test
    void addEvent() {

        // Создаем мероприятия
        count++;
        int id = count;
        User author_1 = User.builder().login(login).password(password).name(authorName).build();
        Event event_1 = Event.builder().name(name).users(author_1).description(description).date(date)
                .place(place).coordX(x).coordY(y).isStreet(isStreet).isGroup(isGroup)
                .isFamily(isFamily).isFree(isFree).hasCovid(hasCovid)
                .hasRegister(hasRegister).isSport(isSport).hasAgeRestrictions(hasAgeRestrictions).build();

        count++;
        int second_id = count;
        User author_2 = User.builder().login(second_login).password(second_password).name(second_authorName).build();
        Event event_2 = Event.builder().name(second_name).users(author_2).description(second_description).date(second_date)
                .place(second_place).coordX(second_x).coordY(second_y).isStreet(second_isStreet).isGroup(second_isGroup)
                .isFamily(second_isFamily).isFree(second_isFree).hasCovid(second_hasCovid)
                .hasRegister(second_hasRegister).isSport(second_isSport).hasAgeRestrictions(second_hasAgeRestrictions).build();

        count++;
        int third_id = count;
        User author_3 = User.builder().login(third_login).password(third_password).name(third_authorName).build();
        Event event_3 = Event.builder().name(third_name).users(author_3).description(third_description).date(third_date)
                .place(third_place).coordX(third_x).coordY(third_y).isStreet(third_isStreet).isGroup(third_isGroup)
                .isFamily(third_isFamily).isFree(third_isFree).hasCovid(third_hasCovid)
                .hasRegister(third_hasRegister).isSport(third_isSport).hasAgeRestrictions(third_hasAgeRestrictions).build();

        // Сохраняем мероприятия и проверяем, что они сохранены верно.
        userDao.save(author_1);
        eventDao.save(event_1);
        List<Event> expected_list = new ArrayList<>();
        expected_list.add(event_1);
        Event actual_event = eventDao.getById(id);
        assertThat(event_1).isEqualTo(actual_event);
        List<Event> actual_events = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_events);

        // Повторяем для оставшихся мероприятий
        userDao.save(author_2);
        eventDao.save(event_2);
        expected_list.add(event_2);
        actual_event = eventDao.getById(second_id);
        assertThat(event_2).isEqualTo(actual_event);
        actual_events = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_events);

        userDao.save(author_3);
        eventDao.save(event_3);
        expected_list.add(event_3);
        actual_event = eventDao.getById(third_id);
        assertThat(event_3).isEqualTo(actual_event);
        actual_events = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_events);

        /* Проверяем, что при попытке сохранить мероприятие с неверно заполненными полями будет
        выдана верная ошибка т.е. неправильные запросы не будут выполняться, */
        boolean gotException = false;
        try {
            count++;
            eventDao.save(Event.builder().name("name1").build());
        } catch (Exception e) {
            gotException = true;
            System.out.println(e.getClass());
            assertThat(e.getClass()).isEqualTo(DataIntegrityViolationException.class);
        }
        if (!gotException) {
            // Если ошибка не выдана тест не будет пройден
            throw new TestAbortedException();
        }
    }

    @DisplayName("Удаляет мероприятие")
    @Test
    void deleteEvent() {

        // Создаем мероприятия
        count++;
        int id = count;
        User author_1 = User.builder().login(login).password(password).name(authorName).build();
        Event event_1 = Event.builder().name(name).users(author_1).description(description).date(date)
                .place(place).coordX(x).coordY(y).isStreet(isStreet).isGroup(isGroup)
                .isFamily(isFamily).isFree(isFree).hasCovid(hasCovid)
                .hasRegister(hasRegister).isSport(isSport).hasAgeRestrictions(hasAgeRestrictions).build();

        count++;
        int second_id = count;
        User author_2 = User.builder().login(second_login).password(second_password).name(second_authorName).build();
        Event event_2 = Event.builder().name(second_name).users(author_2).description(second_description).date(second_date)
                .place(second_place).coordX(second_x).coordY(second_y).isStreet(second_isStreet).isGroup(second_isGroup)
                .isFamily(second_isFamily).isFree(second_isFree).hasCovid(second_hasCovid)
                .hasRegister(second_hasRegister).isSport(second_isSport).hasAgeRestrictions(second_hasAgeRestrictions).build();

        count++;
        int third_id = count;
        User author_3 = User.builder().login(third_login).password(third_password).name(third_authorName).build();
        Event event_3 = Event.builder().name(third_name).users(author_3).description(third_description).date(third_date)
                .place(third_place).coordX(third_x).coordY(third_y).isStreet(third_isStreet).isGroup(third_isGroup)
                .isFamily(third_isFamily).isFree(third_isFree).hasCovid(third_hasCovid)
                .hasRegister(third_hasRegister).isSport(third_isSport).hasAgeRestrictions(third_hasAgeRestrictions).build();


        // Сохраняем мероприятия и авторов
        userDao.save(author_1);
        eventDao.save(event_1);
        userDao.save(author_2);
        eventDao.save(event_2);
        userDao.save(author_3);
        eventDao.save(event_3);

        // Создаем переменные для проверки
        List<Event> expected_list = new ArrayList<>();
        List<Event> actual_list;
        expected_list.add(event_1);
        expected_list.add(event_2);
        expected_list.add(event_3);

        // Поочередно удаляем мероприятия и проверяем, что оно удалено корректно
        eventDao.deleteById(second_id);
        expected_list.remove(1);
        actual_list = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_list);

        eventDao.deleteById(third_id);
        expected_list.remove(1);
        actual_list = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_list);

        eventDao.deleteById(id);
        expected_list.remove(0);
        actual_list = eventDao.findAll();
        assertThat(expected_list).isEqualTo(actual_list);

        /* Проверяем, что при удалении несуществующего мероприятия выдастся верная ошибка
         Т.е. неправильные запросы не будут выполняться */
        boolean gotException = false;
        try {
            eventDao.deleteById(count + 12);
        } catch (Exception e) {
            gotException = true;
            assertThat(e.getClass()).isEqualTo(EmptyResultDataAccessException.class);
        }
        if (!gotException) {
            // Если ожидаемая ошибка не будет выдана, тест экстренно завершится
            throw new TestAbortedException();
        }
    }

}
