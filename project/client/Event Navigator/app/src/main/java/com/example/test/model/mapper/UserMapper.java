package com.example.test.model.mapper;

import android.util.Log;

import com.example.test.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Класс, конвертирующий JSON-файл, получаемый от сервера в сущность
public class UserMapper {

    // Преобразует JSON в обьект класса User
    public User userFromJsonObject(JSONObject jsonObject) {
        User user = null;
        try {
            JSONObject object = jsonObject.getJSONObject("user");
            user = new User(
                    object.getInt("id"),
                    object.getString("name")
            );
        } catch (JSONException e) {
            Log.d("Error in UserMapper", e.getMessage());
        }

        return user;
    }

    // Преобразует JSON в массив объектов класса User
    public List<User> usersFromJsonArray(JSONArray array){
        List<User>res= new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
               JSONObject object = array.getJSONObject(i);
               User user= new User(object.getInt("id"),object.getString("name"));
               res.add(user);
            }
        }catch (JSONException e){
            res=new ArrayList<>();
        }
        return res;
    }
}
