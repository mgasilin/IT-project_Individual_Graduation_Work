package com.example.test.server;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.test.hub_activity_fragments.HomeFragment;
import com.example.test.hub_activity_fragments.MapFragment;
import com.example.test.hub_activity_fragments.RegisterFragment;
import com.example.test.hub_activity_fragments.SearchFragment;
import com.example.test.model.Comment;
import com.example.test.model.Event;
import com.example.test.event_showcase.RegistrationFragmentUser;
import com.example.test.event_showcase.ShowCaseFragment;
import com.example.test.event_showcase.ShowCaseMap;

import java.util.ArrayList;

/* Методы сервера передают запрос и в специальном слушателе обрабатывают ответ,
   вызывая один из методов update у контекста (активности и/или фрагмента), который передается как параметр */
public class Server {

    // Не создаем обьекты класса
    private Server() {}

    private static final ServerServiceImpl server = new ServerServiceImpl();

    /* Методы для работы с серверной часть приложения: передаются параметры, вызывается еще один метод.
    Один из обязательных параметров - контекст, он либо передается напрямую, либо через что-то, от чего его можно получить
    Методы могут быть похожи, но отличаться лишь передаваемой активностью. Это происходит по причине того,
    что в различных фрагментах/активностях могут потребоваться одинаковые методы рабоыт с сервером,
    например, получение мероприятия по айди */

    public static void insertEvent(Event e, Context context) {
        server.insertEvent(e, context);
    }

    public static void findByLength(long length, Context context, MapFragment mapFragment) {
        server.findByLength(length, context, mapFragment);
    }
      
    public static void getEventById(int id, Context context) {
        server.getById(id, context);
    }

    public static void getEventById(int id, Context context, ShowCaseFragment showCaseFragment) {
        server.getById(id, context, showCaseFragment);
    }

    public static void getEventById(int id, Context context, ShowCaseMap showCaseMap) {
        server.getById(id, context, showCaseMap);
    }

    public static void searchByName(String name, Context context, SearchFragment searchFragment) {
        server.searchByName(name, context, searchFragment);
    }

    public static void getByUserId(int id, Context context, HomeFragment homeFragment) {
        server.getByUserId(id, context, homeFragment);
    }

    public static void getByCategories(Context context, SharedPreferences sharedPreferences, long id, MapFragment mapFragment) {
        ArrayList<Boolean> res = new ArrayList<>();
        res.add(sharedPreferences.getBoolean(id + " cr1", false));
        res.add(sharedPreferences.getBoolean(id + " cr2", false));
        res.add(sharedPreferences.getBoolean(id + " cr3", false));
        res.add(sharedPreferences.getBoolean(id + " cr4", false));
        res.add(sharedPreferences.getBoolean(id + " cr5", false));
        res.add(sharedPreferences.getBoolean(id + " cr6", false));
        res.add(sharedPreferences.getBoolean(id + " cr7", false));
        res.add(sharedPreferences.getBoolean(id + " cr8", false));
        server.getByCategories(res.get(0), res.get(1), res.get(2), res.get(3), res.get(4), res.get(5), res.get(6), res.get(7), context, mapFragment);
    }

    public static void getByCategories(Context context, SharedPreferences sharedPreferences, long id, SearchFragment searchFragment) {
        ArrayList<Boolean> res = new ArrayList<>();
        res.add(sharedPreferences.getBoolean(id + " cr1", false));
        res.add(sharedPreferences.getBoolean(id + " cr2", false));
        res.add(sharedPreferences.getBoolean(id + " cr3", false));
        res.add(sharedPreferences.getBoolean(id + " cr4", false));
        res.add(sharedPreferences.getBoolean(id + " cr5", false));
        res.add(sharedPreferences.getBoolean(id + " cr6", false));
        res.add(sharedPreferences.getBoolean(id + " cr7", false));
        res.add(sharedPreferences.getBoolean(id + " cr8", false));
        server.getByCategories(res.get(0), res.get(1), res.get(2), res.get(3), res.get(4), res.get(5), res.get(7), res.get(6), context, searchFragment);
    }

    public static void removeEvent(Event event, Context context) {
        server.removeEvent(event, context);
    }

    public static void login(String login, String password, Context context) {
        server.login(login, password, context);
    }

    public static void register(String login, String password, String username, Context context) {
        server.register(login, password, username, context);
    }

    public static void findByEvent(Event event, Context context) {
        server.findByEvent(event, context);
    }

    public static void insertComment(Comment comment, Context context) {
        server.insertComment(comment, context);
    }
    public  static  void sign(RegistrationFragmentUser fragmentUser, long event_id, long id){
        server.sign(fragmentUser,event_id,id);
    }

    public static  void unsign(RegistrationFragmentUser context, long event_id, long id){
        server.unsign(context,event_id,id);
    }

    public static void update(Context context, boolean has_limit, boolean reg, int limit, long event_id){
        server.update(context,has_limit,reg,limit,event_id);
    }

    public static void findRegister(Context context, RegisterFragment registerFragment, long id){
        server.findRegister(context,registerFragment,id);
    }
}
