package ru.server.service;

import ru.server.dao.EventDao;
import ru.server.dao.RegisterDao;
import ru.server.domain.Event;
import ru.server.domain.Register;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class EventServiceImpl implements EventService {

    private final EventDao eventDao;
    private final RegisterDao registerDao;

    @Transactional
    @Override
    public Event insert(Event e) {
        return eventDao.save(e);
    }

    @Override
    public Event getById(int id) {
        Event event = eventDao.getById(id);
        List<Register> registers = event.getRegistered(), registerList = new ArrayList<>();
        int i = 1;
        for (Register r : registers) {
            if (i <= event.getLimit()) {
                registerList.add(r);
            }
            i++;
        }
        event.setRegistered(registerList);
        return event;
    }

    @Override
    public List<Event> searchByName(String name) {
        List<Event> events = eventDao.findAll();
        List<Event> result= new ArrayList<>();
        for (Event e : events) {
            if (e.getName().contains(name)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<Event> getAll() {
        return eventDao.findAll();
    }

    @Override
    public List<Event> getByUserId(int id) {
        List<Event> events = eventDao.findAll();
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            if (e.getUsers().getId() == id) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<Event> getByCategories(boolean isStreet_criterion, boolean isGroup_criterion, boolean isFamily_criterion, boolean isFree_criterion, boolean isCovid_criterion, boolean isRegister_criterion, boolean isAgeRestricted_criterion, boolean isSport_criterion) {
        List<Event> events = eventDao.findAll();
        List<Boolean> res = new ArrayList<>();
        res.add(isStreet_criterion);
        res.add(isGroup_criterion);
        res.add(isFamily_criterion);
        res.add(isFree_criterion);
        res.add(isCovid_criterion);
        res.add(isRegister_criterion);
        res.add(isAgeRestricted_criterion);
        res.add(isSport_criterion);
        boolean isStreet, isGroup, isFamily, isAgeRestricted, isFree, isCovid, isRegister, isSport;
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            isStreet = event.getIsStreet() == 1;
            isGroup = event.getIsGroup() == 1;
            isFamily = event.getIsFamily() == 1;
            isFree = event.getIsFree() == 1;
            isCovid = event.getHasCovid() == 1;
            isRegister = event.getHasRegister() == 1;
            isAgeRestricted = event.getHasAgeRestrictions() == 1;
            isSport = event.getIsSport() == 1;
            List<Boolean> criteria = new ArrayList<>();
            criteria.add(isStreet);
            criteria.add(isGroup);
            criteria.add(isFamily);
            criteria.add(isFree);
            criteria.add(isCovid);
            criteria.add(isRegister);
            criteria.add(isAgeRestricted);
            criteria.add(isSport);
            boolean temp_result = true;
            for (int i = 0; i < 8; i++) {
                if ((!criteria.get(i)) && res.get(i)) {
                    temp_result = false;
                    break;
                }
            }
            if (temp_result) {
                result.add(event);
            }
        }
        return result;
    }

    @Transactional
    @Override
    public void removeEvent(Event event) {
        eventDao.delete(event);
    }

    @Override
    public void updateSettings(int id, boolean has_limit, int limit, boolean register) {
        Event event = eventDao.getById(id);
        event.setLimit(limit);
        event.setHas_limit(has_limit ? 1 : 0);
        event.setRegistration(register ? 1 : 0);
        eventDao.save(event);
    }

    @Override
    public void update_v2(Event event) {
        List<Register> registers = event.getRegistered();
        for (Register r : registers) {
            registerDao.save(r);
        }
        eventDao.save(event);
    }

    @Override
    public void update_v3(Event event, Register register) {
        try {
            registerDao.delete(register);
        } catch (Exception e) {
            // Ничего не происходит т.к. ошибка вызывается в случае уже удаленной регистрации
        }
    }

    @Override
    public List<Event> getRegisters(int id) {
        List<Register> registers = registerDao.findAll();
        List<Event> res = new ArrayList<>();
        int i = 1;
        for (Register r : registers) {
            if (r.getUser().getId() == id && r.getEvent().getRegistration() == 1 && i <= r.getEvent().getLimit()) {
                res.add(r.getEvent());
            }
            i++;
        }
        return res;
    }
}
