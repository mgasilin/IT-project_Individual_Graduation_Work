package com.example.test.model.mapper;

import com.example.test.model.Event;
import com.example.test.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

// Класс, конвертирующий JSON-файл, получаемый от сервера в сущность
public class EventMapper {

    // Преобразует JSON в обьект класса Event
    public Event eventFromJsonObject(JSONObject jsonObject, int user_id) {
        Event event = null;
        try {
            event = new Event(
                    jsonObject.getString("date"),
                    jsonObject.getInt("id"),
                    user_id,
                    jsonObject.getString("name"),
                    jsonObject.getString("description"),
                    jsonObject.getString("place"),
                    jsonObject.getDouble("coordX"),
                    jsonObject.getDouble("coordY"),
                    jsonObject.getInt("isStreet") > 0,
                    jsonObject.getInt("isGroup") > 0,
                    jsonObject.getInt("isFamily") > 0,
                    jsonObject.getInt("isFree") > 0,
                    jsonObject.getInt("hasCovid") > 0,
                    jsonObject.getInt("hasRegister") > 0,
                    jsonObject.getInt("isSport") > 0,
                    jsonObject.getInt("hasAgeRestrictions") > 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return event;
    }

    // Преобразует JSON в обьект класса Event с учетом параметров записи
    public Event eventFromJsonObject_extended(JSONObject jsonObject, int user_id){
        Event event = null;

        try {
            List<User> users= new UserMapper().usersFromJsonArray(jsonObject.getJSONArray("registers"));
            event = new Event(
                    jsonObject.getString("date"),
                    jsonObject.getInt("id"),
                    user_id,
                    jsonObject.getString("name"),
                    jsonObject.getString("description"),
                    jsonObject.getString("place"),
                    jsonObject.getDouble("coordX"),
                    jsonObject.getDouble("coordY"),
                    jsonObject.getInt("isStreet") > 0,
                    jsonObject.getInt("isGroup") > 0,
                    jsonObject.getInt("isFamily") > 0,
                    jsonObject.getInt("isFree") > 0,
                    jsonObject.getInt("hasCovid") > 0,
                    jsonObject.getInt("hasRegister") > 0,
                    jsonObject.getInt("isSport") > 0,
                    jsonObject.getInt("hasAgeRestrictions") > 0,
                    jsonObject.getInt("register")>0,
                    jsonObject.getInt("has_limit")>0,
                    jsonObject.getInt("limit"));
            event.setRegistrated(users);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return event;
    }



}
