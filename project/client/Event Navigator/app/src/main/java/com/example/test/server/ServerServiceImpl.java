package com.example.test.server;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.test.entry.MainActivity;
import com.example.test.entry.RegisterActivity;
import com.example.test.hub_activity_fragments.HomeFragment;
import com.example.test.hub_activity_fragments.MapFragment;
import com.example.test.hub_activity_fragments.RegisterFragment;
import com.example.test.hub_activity_fragments.SearchFragment;
import com.example.test.model.Comment;
import com.example.test.model.Event;
import com.example.test.model.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.model.mapper.CommentMapper;
import com.example.test.model.mapper.EventMapper;
import com.example.test.model.mapper.UserMapper;
import com.example.test.event_showcase.EventShowCaseActivity;
import com.example.test.event_showcase.RegistrationFragmentUser;
import com.example.test.event_showcase.ShowCaseFragment;
import com.example.test.event_showcase.ShowCaseMap;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServerServiceImpl implements ServerInterface {
    public static final String BASE_URL = "http://192.168.1.120:8081";

    private RequestQueue queue;
    private final Response.ErrorListener errorListener;

    public ServerServiceImpl() {
        errorListener = new ErrorListenerImpl();
    }

    public void findByLength(long length, double x, double y, Context context, MapFragment mapFragment) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/length";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, response -> {
            List<Event> events = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    User user = new UserMapper().userFromJsonArray(jsonObject);
                    Event event = new EventMapper().eventFromJsonArray(jsonObject, user.getId());
                    events.add(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mapFragment.updateLen(events, x, y, (int) length);
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    @Override
    public void insertEvent(Event e, Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/add/v2?x=" + e.getX() +
                " &y=" + e.getY() + "&date=" + e.getDate() + "&place=" + e.getPlace() +
                "&description=" + e.getDescription() +
                "&ageRes=" + (e.isHasAgeRestrictions() > 0 ? "1" : "0") +
                "&covid=" + (e.isHasCovid() > 0 ? "1" : "0") +
                "&fam=" + (e.isFamily() > 0 ? "1" : "0") +
                "&free=" + (e.isFree() > 0 ? "1" : "0") +
                "&group=" + (e.isGroup() > 0 ? "1" : "0") +
                "&sport=" + (e.isSport() > 0 ? "1" : "0") +
                "&owner=" + e.getOwner_id() +
                "&street=" + (e.isStreet() > 0 ? "1" : "0") +
                "&name=" + e.getName() +
                "&register=" + (e.isHasRegister() > 0 ? "1" : "0")
                + "&limit=" + e.getLimit()
                + "&has_limit=" + (e.isHas_limit() ? "1" : "0")
                + "&reg=" + (e.isRegister() ? "1" : "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null, response -> {
            User user = new UserMapper().userFromJsonArray(response);
            Event event = new EventMapper().eventFromJsonArray(response, user.getId());
        }, errorListener);

        queue.add(jsonObjectRequest);
    }

    @Override
    public void getById(int id, Context context, ShowCaseFragment showCaseFragment) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, response -> {
            User user = new UserMapper().userFromJsonArray(response);
            Event event = new EventMapper().eventFromJsonArray(response, user.getId());
            showCaseFragment.update(event);
        }, error -> showCaseFragment.back());

        queue.add(jsonObjectRequest);
    }

    @Override
    public void getById(int id, Context context, ShowCaseMap showCaseMap) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/" + id;

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, response -> {
            User user = new UserMapper().userFromJsonArray(response);
            Event event = new EventMapper().eventFromJsonArray(response, user.getId());
            showCaseMap.update(event);
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    @Override
    public void getById(int id, Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, response -> {
            User user = new UserMapper().userFromJsonArray(response);
            Event event = new EventMapper().eventFromJsonArray_v2(response, user.getId());
            ((EventShowCaseActivity) context).update(event);
        }, error -> {
            ((EventShowCaseActivity) context).back();
        });

        queue.add(jsonObjectRequest);
    }

    @Override
    public void searchByName(String name, Context context, SearchFragment searchFragment) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/name?name=" + name;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, response -> {
            List<Event> events = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject jsonObject = response.getJSONObject(i);

                    User user = new UserMapper().userFromJsonArray(jsonObject);
                    Event event = new EventMapper().eventFromJsonArray(jsonObject, user.getId());
                    events.add(event);
                }
                searchFragment.update(events);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, errorListener);

        queue.add(jsonArrayRequest);

    }


    @Override
    public void getByUserId(int id, Context context, HomeFragment homeFragment) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/user?id=" + id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, response -> {
            List<Event> events = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    User user = new UserMapper().userFromJsonArray(jsonObject);
                    Event event = new EventMapper().eventFromJsonArray(jsonObject, user.getId());
                    events.add(event);
                }
                homeFragment.update(events);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    @Override
    public void getByCategories(boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean isCovid, boolean isRegister, boolean isSport, boolean isAgeRestricted, Context context, MapFragment mapFragment) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/categories?street=" + (isStreet ? 1 : 0) + "&group=" + (isGroup ? 1 : 0) + "&fam=" + (isFamily ? 1 : 0) + "&free=" + (isFree ? 1 : 0) + "&covid=" + (isCovid ? 1 : 0) + "&reg=" + (isRegister ? 1 : 0) + "&sport=" + (isSport ? 1 : 0) + "&age=" + (isAgeRestricted ? 1 : 0);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, response -> {
            List<Event> events = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject jsonObject = response.getJSONObject(i);

                    User user = new UserMapper().userFromJsonArray(jsonObject);
                    Event event = new EventMapper().eventFromJsonArray(jsonObject, user.getId());
                    events.add(event);
                }
                mapFragment.updateCat(events);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }, errorListener);
        queue.add(jsonArrayRequest);
    }

    @Override
    public void getByCategories(boolean isStreet, boolean isGroup, boolean isFamily, boolean isFree, boolean isCovid, boolean isRegister, boolean isSport, boolean isAgeRestricted, Context context, SearchFragment searchFragment) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/categories?street=" + (isStreet ? 1 : 0) + "&group=" + (isGroup ? 1 : 0) + "&fam=" + (isFamily ? 1 : 0) + "&free=" + (isFree ? 1 : 0) + "&covid=" + (isCovid ? 1 : 0) + "&reg=" + (isRegister ? 1 : 0) + "&sport=" + (isSport ? 1 : 0) + "&age=" + (isAgeRestricted ? 1 : 0);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, response -> {
            List<Event> events = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject jsonObject = response.getJSONObject(i);

                    User user = new UserMapper().userFromJsonArray(jsonObject);
                    Event event = new EventMapper().eventFromJsonArray(jsonObject, user.getId());
                    events.add(event);
                }
                searchFragment.update(events);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }, errorListener);
        queue.add(jsonArrayRequest);
    }

    @Override
    public void removeEvent(Event event, Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/delete?id=" + event.getId() + "";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.DELETE,
                url,
                null, response -> {
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    @Override
    public void login(String login, String password, Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/user/login" + "?login=" + login + "&password=" + password;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, response -> {
            try {
                int result = response.getInt("result");
                int id = response.getInt("id");
                ((MainActivity) context).update(result, id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, errorListener);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void register(String login, String password, String username, Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/user/register" + "?login=" + login + "&password=" + password + "&username=" + username;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, response -> {
            try {
                int result = response.getInt("result");
                ((RegisterActivity) context).update(result);
            } catch (JSONException e) {
            }
        }, errorListener);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void findByEvent(Event event, Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/" + event.getId() + "/comments";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, response -> {
            List<Comment> comments = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    User user = null;
                    JSONObject jsonObject = response.getJSONObject(i);
                    user = new UserMapper().userFromJsonArray(jsonObject);
                    Comment comment = new CommentMapper().commentFromJsonArray(jsonObject, user.getId(), user.getName());
                    comments.add(comment);
                }
                ((EventShowCaseActivity) context).update(comments);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, errorListener);

        queue.add(jsonArrayRequest);
    }

    @Override
    public void insertComment(Comment comment, Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/comment/new?content=" + comment.getMessage() + "&event_id=" + comment.getEvent_id() + "&author_id=" + comment.getAuthor_id();//TODO: put parameters into a url
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                url,
                null, response -> {
            List<Comment> comments = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    User user = new UserMapper().userFromJsonArray(jsonObject);
                    Comment comment1 = new CommentMapper().commentFromJsonArray(jsonObject, user.getId(), user.getName());
                    comments.add(comment1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ((EventShowCaseActivity) context).update(comments);
        }, error -> ((EventShowCaseActivity) context).back());
        queue.add(jsonArrayRequest);
    }

    @Override
    public void sign(RegistrationFragmentUser fragmentUser, long event_id, long id) {
        if (queue == null) {
            queue = Volley.newRequestQueue(fragmentUser.getActivity());
        }
        class Result {
            private final int result;

            private int getResult() {
                return result;
            }

            public Result(int result) {
                this.result = result;
            }
        }
        final Result[] result = new Result[1];
        String url = BASE_URL + "/events/" + event_id + "/register?id=" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null, response -> {
            try {
                result[0] = new Result(response.getInt("result"));
                if (result[0].getResult() == 1) {
                    result[0] = new Result(2);
                }
            } catch (JSONException e) {
                result[0] = new Result(0);
                e.printStackTrace();
            }
            fragmentUser.update(result[0].getResult());
        }, errorListener);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void unsign(RegistrationFragmentUser registrationFragmentUser, long event_id, long id) {
        if (queue == null) {
            queue = Volley.newRequestQueue(registrationFragmentUser.getActivity());
        }
        String url = BASE_URL + "/events/" + event_id + "/unregister?id=" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Register", "unregister done");
                registrationFragmentUser.update(1);
            }
        }, errorListener);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void update(Context context, boolean has_limit, boolean has_register, int limit, long event_id) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/" + event_id + "/update?register=" + (has_register ? "1" : "0") +
                "&limit=" + limit +
                "&has_limit=" + (has_limit ? "1" : "0");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User user = new UserMapper().userFromJsonArray(response);
                Event event = new EventMapper().eventFromJsonArray_v2(response, user.getId());
                ((EventShowCaseActivity) context).update(event);
            }
        }, errorListener);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void findRegister(Context context, RegisterFragment registerFragment, long id) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        String url = BASE_URL + "/events/register/find?id=" + id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, response -> {
            List<Event> events = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    User user = new UserMapper().userFromJsonArray(jsonObject);
                    Event event = new EventMapper().eventFromJsonArray(jsonObject, user.getId());
                    events.add(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            registerFragment.update(events);
        }, errorListener);
        queue.add(jsonArrayRequest);
    }

    private static class ErrorListenerImpl implements Response.ErrorListener {


        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("VolleyError", "message:" + error.getMessage());
        }
    }
}
