package com.example.test.hub_activity_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;
import com.example.test.model.adapters.UsualEventRvAdapter;
import com.example.test.event_showcase.ShowCaseFragment;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private long id;
    private EditText search_name;
    private List<Event> events = new ArrayList<>();
    private int search_state = 1;

    public SearchFragment() {
    }

    private UsualEventRvAdapter adapter;
    private SharedPreferences sharedPreferences;
    private ShowCaseFragment showCaseFragment;

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
    а также могут отвечать за ее отображение */
    public void update(List<Event> events) {
        adapter.setEvents(events);
        this.events = events;
        adapter.notifyDataSetChanged();
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    public static SearchFragment newInstance(long id) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
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
        sharedPreferences = this.getActivity().getPreferences(0);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        AppCompatButton searchByNameButton = view.findViewById(R.id.search_by_name);
        search_name = view.findViewById(R.id.search_name);
        List<Event> myEvents = new ArrayList<>();
        //Создание и настройка списка мероприятий на экране
        RecyclerView result = view.findViewById(R.id.search_recycler);
        adapter = new UsualEventRvAdapter(myEvents, (v, position) -> {
            Event event = events.get(position);
            showCaseFragment = ShowCaseFragment.newInstance(event.getId(), id, 5);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.event_showcase_search, showCaseFragment)
                    .commit();
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        result.setLayoutManager(llm);
        result.setAdapter(adapter);
        Server.getByCategories(getActivity(), sharedPreferences, id, SearchFragment.this);
        //Настройка смены видов поиска
        searchByNameButton.setOnClickListener(view1 -> {
            Server.searchByName(search_name.getText().toString(), getActivity(), SearchFragment.this);
            search_state = 2;
        });
        AppCompatButton backButton = view.findViewById(R.id.search_back);
        backButton.setOnClickListener(view12 -> {
            sharedPreferences = getActivity().getPreferences(0);
            Server.getByCategories(getActivity(), sharedPreferences, id, SearchFragment.this);
            search_name.setText("");
            search_state = 1;
        });
        return view;
    }

    //Пауза автообновления
    @Override
    public void onPause() {
        handler.removeCallbacks(updaterRunnable);
        super.onPause();
    }

    //Возобновление автообновления
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(updaterRunnable, 100);
    }

    //Создание автообновления
    private final Handler handler = new Handler();
    private final Runnable updaterRunnable = new Runnable() {
        public void run() {
            if (search_state ==1){
                Server.getByCategories(getActivity(), sharedPreferences, id, SearchFragment.this);
            }else{
                Server.searchByName(search_name.getText().toString(), getActivity(), SearchFragment.this);
            }
            handler.postDelayed(this, 5000);
        }
    };

}