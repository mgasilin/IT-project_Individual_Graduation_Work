package com.example.test.event_showcase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;

import java.util.ArrayList;

public class ShowcaseInfo extends Fragment {
    private long id;
    private TextView name, place, date, description, categories;

    //Вызывается в случае, когда мероприятие было удалено
    public void back() {
        getParentFragmentManager().beginTransaction().detach(ShowcaseInfo.this).commit();
    }

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
        а также могут отвечать за ее отображение */
    public void update(Event e) {
        //Проверка, не пустое ли мероприятие
        if (e == null || e.getName().isEmpty()) {
            back();
        }
        if (name != null) {
            name.setText("Мероприятие: " + e.getName());
            description.setText("Описание: " + e.getDescription());
            place.setText("Место проведения: " + e.getPlace());
            date.setText("Время проведения: " + ShowCaseFragment.beautifyEventDate(e.getDate()));
            ArrayList<Boolean> booleanArrayList = e.getCategories();
            String categoriesForShow = "";
            if (booleanArrayList.get(0)) {
                categoriesForShow += "Уличное мероприятие. ";
            }
            if (booleanArrayList.get(1)) {
                categoriesForShow += "Групповое мероприятие. ";
            }
            if (booleanArrayList.get(2)) {
                categoriesForShow += "Мероприятие для всей семьи. ";
            }
            if (booleanArrayList.get(3)) {
                categoriesForShow += "Бесплатное мероприятие.";
            }
            if (booleanArrayList.get(4)) {
                categoriesForShow += "Мероприятие с ковидными ограничениями. ";
            }
            if (booleanArrayList.get(5)) {
                categoriesForShow += "Мероприятие с предварительной регистрацией. ";
            }
            if (booleanArrayList.get(6)) {
                categoriesForShow += "Спротивное мероприятие. ";
            }
            if (booleanArrayList.get(7)) {
                categoriesForShow += "Мероприятие с возрастными ограничениями. ";
            }
            if (categoriesForShow.isEmpty()) {
                categoriesForShow = "Данное мероприятие не принадлежит ни одной из категорий.";
            }
            categories.setText("Категории: " + categoriesForShow);
        }
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    public ShowcaseInfo() {
    }

    public static ShowcaseInfo newInstance(long id, long user) {
        ShowcaseInfo fragment = new ShowcaseInfo();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putLong("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showcase_info, container, false);
        name = view.findViewById(R.id.info_name);
        date = view.findViewById(R.id.info_date);
        place = view.findViewById(R.id.info_place);
        description = view.findViewById(R.id.info_description);
        categories = view.findViewById(R.id.info_categories);
        ShowCaseMap map = ShowCaseMap.newInstance(id);
        getChildFragmentManager().beginTransaction().replace(R.id.map_placeholder, map).commit();
        Server.getEventById((int) id, getActivity());
        return view;
    }

    //Пауза автообновления при паузе фрагмента
    @Override
    public void onPause() {
        handler.removeCallbacks(updaterRunnable);
        super.onPause();
    }

    //Продолжение автообновления при работе фрагмента
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(updaterRunnable, 100);
    }

    //Создание автообновления
    private final Handler handler = new Handler();
    private final Runnable updaterRunnable = new Runnable() {
        public void run() {
            Server.getEventById((int) id, getActivity());
            handler.postDelayed(this, 5000);
        }
    };
}