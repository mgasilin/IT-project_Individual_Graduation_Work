package ru.server.domain;

import lombok.*;
import javax.persistence.*;
import java.util.List;

// Аннотации задают связь с базой данных и определяют конструкторы, сеттеры и геттеры
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {


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
}
