package ru.server.service;

import ru.server.domain.Event;
import ru.server.domain.Register;

import java.util.List;

public interface EventService {
    Event insert(Event e);

    Event getById(int id);

    List<Event> searchByName(String name);

    List<Event> getAll();

    List<Event> getByUserId(int id);

    List<Event> getByCategories(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h);

    void removeEvent(Event event);

    void updateSettings(int id, boolean has_limit, int limit, boolean register);

    void update_v2(Event event);

    void update_v3(Event event, Register r);

    List<Event> getRegisters(int id);
}
