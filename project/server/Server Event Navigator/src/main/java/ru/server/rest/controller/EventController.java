package ru.server.rest.controller;

import ru.server.domain.Event;
import ru.server.domain.Register;
import ru.server.rest.dto.EventDto;
import ru.server.rest.dto.ResultDto;
import ru.server.service.EventService;
import ru.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class EventController {
    // Сервисы для работы с сущностями
    private final UserService userService;
    private final EventService eventService;

    // Получение всех мероприятий
    @GetMapping("/events")
    public List<EventDto> getAll() {
        return eventService.getAll().stream().map(EventDto::toDto).collect(Collectors.toList());
    }

    // Получение мероприятия по айди
    @GetMapping("/events/{id}")
    public EventDto getById(@PathVariable int id) {
        try {
            Event event = eventService.getById(id);
            return EventDto.toDto_v2(event);
        } catch (Exception e) {
            return null;
        }
    }

    // Получение мероприятий по категориям.
    @GetMapping("/events/categories")
    public List<EventDto> getByCategories(@RequestParam boolean street, @RequestParam boolean group, @RequestParam boolean sport,
                                          @RequestParam boolean fam, @RequestParam boolean free, @RequestParam boolean covid,
                                          @RequestParam boolean reg, @RequestParam boolean age) {
        List<Event> events = eventService.getByCategories(street, group, fam, free, covid, reg, age, sport);
        return events.stream().map(EventDto::toDto).collect(Collectors.toList());
    }

    // Поиск по имени. Возвращает мероприятия, содержащие переданную строку в названии
    @GetMapping("/events/name")
    public List<EventDto> getByName(@RequestParam String name) {
        return eventService.searchByName(name).stream().map(EventDto::toDto).collect(Collectors.toList());
    }

    // Получение всех мероприятий пользователя по айди
    @GetMapping("/events/user")
    public List<EventDto> getByUserId(@RequestParam int id) {
        try {
            return eventService.getByUserId(id).stream().map(EventDto::toDto).collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    //Получение всех мероприятий для последующего поиска по удаленности
    @GetMapping("/events/length")
    public List<EventDto> getByLength() {
        try {
            return eventService.getAll().stream().map(EventDto::toDto).collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    // Удаление мероприятия по айди
    @DeleteMapping("/events/delete")
    public void deleteById(@RequestParam int id) {
        eventService.removeEvent(eventService.getById(id));
    }

    // Создание мероприятия с учетом параметров записи
    @PostMapping("/events/add_v2")
    public EventDto addEvent_extended(@RequestParam String name, @RequestParam String description, @RequestParam String date, @RequestParam int owner, @RequestParam String place, @RequestParam double x, @RequestParam double y, @RequestParam int street, @RequestParam int group, @RequestParam int fam, @RequestParam int free, @RequestParam int covid, @RequestParam int register, @RequestParam int ageRes, @RequestParam int sport, @RequestParam int limit, @RequestParam boolean has_limit, @RequestParam boolean has_register) {
        return EventDto.toDto(eventService.insert(Event.builder()
                .date(date).coordX(x).coordY(y).description(description).hasAgeRestrictions(ageRes)
                .hasCovid(covid).isFamily(fam).isFree(free).isGroup(group).isSport(sport).users(userService.getById(owner))
                .isStreet(street).name(name).place(place).hasRegister(register).registered(new ArrayList<>()).registration(has_register ? 1 : 0).has_limit(has_limit ? 1 : 0).limit(limit).build()));
    }
    // Регистрация пользователя на мероприятие по двум айди
    @PostMapping("/events/{event_id}/register")
    public ResultDto registerById(@PathVariable int event_id, @RequestParam int id) {
        try {
            Event event = eventService.getById(event_id);
            if (event.getRegistration() == 1 && !(event.getHas_limit() == 1 && event.getRegistered().size() > event.getLimit())) {
                List<Register> registers = event.getRegistered();
                boolean isAlreadyRegistered = false;
                for (Register r : registers) {
                    if (r.getUser().getId() == id && r.getEvent().getId() == event_id) {
                        isAlreadyRegistered = true;
                        break;
                    }
                }
                if (isAlreadyRegistered) {
                    return new ResultDto(2);
                } else {
                    registers.add(Register.builder().user(userService.getById(id)).event(eventService.getById(event_id)).build());
                    event.setRegistered(registers);
                    eventService.update_v2(event);
                    return new ResultDto(1);
                }
            } else {
                return new ResultDto(0);
            }
        } catch (EntityNotFoundException e) {
            return new ResultDto(0);
        }
    }

    // Обновление данных о регистрации указанного мероприятия.
    @PostMapping("/events/{event_id}/update")
    public EventDto updateEvent(@PathVariable int event_id, @RequestParam int has_limit, @RequestParam int register, @RequestParam int limit) {
        eventService.updateSettings(event_id, has_limit == 1, limit, register == 1);
        return getById(event_id);
    }

    //Отмена регистрации на мероприятие
    @PostMapping("/events/{event_id}/unregister")
    public ResultDto unregisterById(@PathVariable int event_id, @RequestParam int id) {
        try {
            Event event = eventService.getById(event_id);
            List<Register> registers = event.getRegistered();
            int res = 2;
            for (Register r : registers) {
                if (r.getUser().getId() == id && r.getEvent().getId() == event_id) {
                    res = 0;
                    registers.remove(r);
                    event.setRegistered(registers);
                    eventService.update_v3(event, r);
                    break;
                }
            }
            return new ResultDto(res);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    // Создание мероприятия
    @PostMapping("/events/add/v2")
    public EventDto addEvent_extended(@RequestParam String name, @RequestParam String description, @RequestParam String date, @RequestParam int owner, @RequestParam String place, @RequestParam double x, @RequestParam double y, @RequestParam int street, @RequestParam int group, @RequestParam int fam, @RequestParam int free, @RequestParam int covid, @RequestParam int register, @RequestParam int ageRes, @RequestParam int sport, @RequestParam int limit, @RequestParam int has_limit, @RequestParam int reg) {
        return EventDto.toDto(eventService.insert(Event.builder()
                .date(date).coordX(x).coordY(y).description(description).hasAgeRestrictions(ageRes)
                .hasCovid(covid).isFamily(fam).isFree(free).isGroup(group).isSport(sport).users(userService.getById(owner))
                .isStreet(street).name(name).place(place).hasRegister(register).registration(reg).has_limit(has_limit).limit(limit).build()));
    }


    // Поиск мероприятий, на которые записан пользователь.
    @GetMapping("/events/register/find")
    public List<EventDto> getRegisters(@RequestParam int id) {
        return eventService.getRegisters(id).stream().map(EventDto::toDto).collect(Collectors.toList());
    }

}
