package com.example.test.model;

// Класс сущности
public class User {
    private final int id;
    private final String name;

    // Конструктор
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
