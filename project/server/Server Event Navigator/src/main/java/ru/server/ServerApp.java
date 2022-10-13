package ru.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ServerApp {
    // Запуск сервера
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}
