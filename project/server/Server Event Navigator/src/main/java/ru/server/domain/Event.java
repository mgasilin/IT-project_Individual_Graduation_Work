package ru.server.domain;

import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

// Аннотации задают связь с базой данных и определяют конструкторы, сеттеры и геттеры
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {

    // Поля класса
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne(targetEntity = User.class,  fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private User users;


    @Column(name = "name")
    private String name;


    @Column(name="description")
    private String description;

    @Column(name = "date")
    private String date;


    @Column(name="place")
    private String place;


    @Column(name="x_coord")
    private double coordX;


    @Column(name="y_coord")
    private double coordY;


    @Column(name="street_event")
    private int isStreet;

    @Column(name="group_event")
    private int isGroup;


    @Column(name="family_event")
    private int isFamily;


    @Column(name="free_event")
    private int isFree;


    @Column(name="event_with_covid_restrictions")
    private int hasCovid;


    @Column(name="event_with_registration")
    private int hasRegister;


    @Column(name = "sport_event")
    private int isSport;


    @Column(name = "event_with_age_restrictions")
    private int hasAgeRestrictions;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Comment> comments;

    @Column(name = "registration")
    @Setter
    private int registration;

    @Setter
    @Column(name = "lim")
    private int limit;

    @Setter
    @Column(name = "has_limit")
    private int has_limit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Register> registered;


    // Переопределение сравнения
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && Double.compare(event.coordX, coordX) == 0 && Double.compare(event.coordY, coordY) == 0
                && isStreet == event.isStreet && isGroup == event.isGroup && isFamily == event.isFamily && isFree == event.isFree
                && hasCovid == event.hasCovid && hasRegister == event.hasRegister && isSport == event.isSport
                && hasAgeRestrictions == event.hasAgeRestrictions && Objects.equals(users, event.users)
                && Objects.equals(name, event.name) && Objects.equals(description, event.description)
                && Objects.equals(date, event.date) && Objects.equals(place, event.place);
    }
}
