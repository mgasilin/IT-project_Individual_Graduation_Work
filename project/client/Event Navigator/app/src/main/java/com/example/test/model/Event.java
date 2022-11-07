package com.example.test.model;


import java.util.ArrayList;
import java.util.List;

// Класс сущности
public class Event {
    private final String date;
    private long id;
    private final long owner_id;
    private final String name;
    private final String description;
    private final String place;
    private final double coordX;
    private final double coordY;
    private final boolean isStreet;
    private final boolean isGroup;
    private final boolean isFamily;
    private final boolean isFree;
    private final boolean hasCovid;
    private final boolean hasRegister;
    private final boolean isSport;
    private final boolean hasAgeRestrictions;
    private boolean register;
    private boolean has_limit;
    private List<User> registrated = new ArrayList<>();
    private int limit;

    // Геттеры
    public boolean isRegister() {
        return register;
    }

    public boolean isHas_limit() {
        return has_limit;
    }

    public List<User> getRegistrated() {
        return registrated;
    }

    public int getLimit() {
        return limit;
    }

    // Конструкторы
    public Event(String date, long id, long owner_id, String name, String description, String place, double coordX, double coordY, boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean hasCovid, boolean hasRegister, boolean isSport, boolean hasAgeRestrictions, boolean register, boolean has_limit,  int limit) {
        this.date = date;
        this.id = id;
        this.owner_id = owner_id;
        this.name = name;
        this.description = description;
        this.place = place;
        this.coordX = coordX;
        this.coordY = coordY;
        this.isStreet = isStreet;
        this.isGroup = isGroup;
        this.isFamily = isFamily;
        this.isFree = isFree;
        this.hasCovid = hasCovid;
        this.hasRegister = hasRegister;
        this.isSport = isSport;
        this.hasAgeRestrictions = hasAgeRestrictions;
        this.register = register;
        this.has_limit = has_limit;
        this.limit = limit;
    }

    public Event(String date, long owner_id, String name, String description, String place, double coordX, double coordY, boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean hasCovid, boolean hasRegister, boolean isSport, boolean hasAgeRestrictions, boolean register, boolean has_limit,  int limit) {
        this.date = date;
        this.owner_id = owner_id;
        this.name = name;
        this.description = description;
        this.place = place;
        this.coordX = coordX;
        this.coordY = coordY;
        this.isStreet = isStreet;
        this.isGroup = isGroup;
        this.isFamily = isFamily;
        this.isFree = isFree;
        this.hasCovid = hasCovid;
        this.hasRegister = hasRegister;
        this.isSport = isSport;
        this.hasAgeRestrictions = hasAgeRestrictions;
        this.register = register;
        this.has_limit = has_limit;
        this.limit = limit;
    }


    public Event(String date, long id, long owner_id, String name, String description, String place, double coordX, double coordY, boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean hasCovid, boolean hasRegister, boolean isSport, boolean hasAgeRestrictions) {
        this.date = date;
        this.id = id;
        this.owner_id = owner_id;
        this.name = name;
        this.description = description;
        this.place = place;
        this.coordX = coordX;
        this.coordY = coordY;
        this.isStreet = isStreet;
        this.isGroup = isGroup;
        this.isFamily = isFamily;
        this.isFree = isFree;
        this.hasCovid = hasCovid;
        this.hasRegister = hasRegister;
        this.isSport = isSport;
        this.hasAgeRestrictions = hasAgeRestrictions;
    }

    public Event(String date, long owner_id, String name, String description, String place, double coordX, double coordY, boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean hasCovid, boolean hasRegister, boolean isSport, boolean hasAgeRestrictions) {
        this.date = date;
        this.owner_id = owner_id;
        this.name = name;
        this.description = description;
        this.place = place;
        this.coordX = coordX;
        this.coordY = coordY;
        this.isStreet = isStreet;
        this.isGroup = isGroup;
        this.isFamily = isFamily;
        this.isFree = isFree;
        this.hasCovid = hasCovid;
        this.hasRegister = hasRegister;
        this.isSport = isSport;
        this.hasAgeRestrictions = hasAgeRestrictions;
    }

    //Геттеры
    public String getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPlace() {
        return place;
    }

    public double getX() {
        return coordX;
    }

    public double getY() {
        return coordY;
    }

    public int isStreet() {
        return (isStreet ? 1 : 0);
    }

    public int isGroup() {
        return (isGroup ? 1 : 0);
    }

    public int isFamily() {
        return (isFamily ? 1 : 0);
    }

    public int isFree() {
        return (isFree ? 1 : 0);
    }

    public int isHasCovid() {
        return (hasCovid ? 1 : 0);
    }

    public int isHasRegister() {
        return (hasRegister ? 1 : 0);
    }

    public int isSport() {
        return (isSport ? 1 : 0);
    }

    public int isHasAgeRestrictions() {
        return (hasAgeRestrictions ? 1 : 0);
    }

    public ArrayList<Boolean> getCategories() {
        ArrayList<Boolean> result = new ArrayList<>();
        result.add(isStreet);
        result.add(isGroup);
        result.add(isFamily);
        result.add(isFree);
        result.add(hasCovid);
        result.add(hasRegister);
        result.add(isSport);
        result.add(hasAgeRestrictions);
        return result;
    }

    // Конструктор
    public Event(String date, long id, long owner_id, String name, String description, String place, double coordX, double coordY, boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean hasCovid, boolean hasRegister, boolean isSport, boolean hasAgeRestrictions, String author) {
        this.date = date;
        this.id = id;
        this.owner_id = owner_id;
        this.name = name;
        this.description = description;
        this.place = place;
        this.coordX = coordX;
        this.coordY = coordY;
        this.isStreet = isStreet;
        this.isGroup = isGroup;
        this.isFamily = isFamily;
        this.isFree = isFree;
        this.hasCovid = hasCovid;
        this.hasRegister = hasRegister;
        this.isSport = isSport;
        this.hasAgeRestrictions = hasAgeRestrictions;
    }

    //Геттер
    public void setRegistrated(List<User> registrated){
        this.registrated = registrated;
    }

    //Обновление параметров записи
    public void update(int limit, boolean has_limit, boolean register) {
        this.limit = limit;
        this.has_limit = has_limit;
        this.register = register;
    }

}
