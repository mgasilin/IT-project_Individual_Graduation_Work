package com.example.test.server;

import android.content.Context;

import com.example.test.hub_activity_fragments.HomeFragment;
import com.example.test.hub_activity_fragments.MapFragment;
import com.example.test.hub_activity_fragments.RegisterFragment;
import com.example.test.hub_activity_fragments.SearchFragment;
import com.example.test.model.Comment;
import com.example.test.model.Event;
import com.example.test.event_showcase.RegistrationFragmentUser;
import com.example.test.event_showcase.ShowCaseFragment;
import com.example.test.event_showcase.ShowCaseMap;

public interface ServerInterface {
    void insertEvent(Event e, Context context);

    void getById(int id, Context context);

    void getById(int id, Context context, ShowCaseFragment showCaseFragment);

    void getById(int id, Context context, ShowCaseMap showCaseMap);

    void searchByName(String name, Context context, SearchFragment searchFragment);

    void getByUserId(int id, Context context, HomeFragment homeFragment);

    void getByCategories(boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean isCovid, boolean isRegister, boolean isSport, boolean isAgeRestricted, Context context, MapFragment mapFragment);

    void getByCategories(boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean isCovid, boolean isRegister, boolean isSport, boolean isAgeRestricted, Context context, SearchFragment searchFragment);

    void removeEvent(Event event, Context context);

    void login(String login, String password, Context context);

    void findByLength(long length, double x, double y, Context context, MapFragment mapFragment);

    void register(String login, String password, String username, Context context);

    void findByEvent(Event event, Context context);

    void insertComment(Comment comment, Context context);

    void sign(RegistrationFragmentUser fragmentUser, long event_id, long id);

    void unsign(RegistrationFragmentUser registrationFragmentUser, long event_id, long id);

    void update(Context context, boolean has_limit, boolean has_register, int limit, long event_id);

    void findRegister(Context context, RegisterFragment registerFragment, long id);
}
