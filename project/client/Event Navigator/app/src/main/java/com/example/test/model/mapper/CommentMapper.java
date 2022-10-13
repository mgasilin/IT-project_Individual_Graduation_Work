package com.example.test.model.mapper;

import com.example.test.model.Comment;

import org.json.JSONException;
import org.json.JSONObject;

// Класс, конвертирующий ответ JSON-файл, получаемый от сервера в сущность
public class CommentMapper {
    public Comment commentFromJsonArray(JSONObject jsonObject, int user_id, String user_name) {

        Comment comment = null;

        try {
            comment = new Comment(
                    jsonObject.getInt("id"),
                    user_id,
                    jsonObject.getInt("event_id"),
                    jsonObject.getString("content"),
                    jsonObject.getInt("sequence_number"),
                    user_name
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return comment;
    }
}
