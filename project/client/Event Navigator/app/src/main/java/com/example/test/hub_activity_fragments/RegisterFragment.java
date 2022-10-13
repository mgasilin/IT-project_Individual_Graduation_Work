package com.example.test.hub_activity_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;
import com.example.test.model.adapters.UsualEventRvAdapter;
import com.example.test.event_showcase.ShowCaseFragment;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {

    private ShowCaseFragment showCaseFragment;
    private List<Event> eventList = new ArrayList<>();
    private long userId;
    private TextView noEventsText;
    private UsualEventRvAdapter adapter;

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
    а также могут отвечать за ее отображение */
    public void update(List<Event> events) {
        eventList = events;
        if (events.size()==0){
            if (noEventsText !=null){
                noEventsText.setVisibility(View.VISIBLE);
            }
        }else if (noEventsText !=null){
            noEventsText.setVisibility(View.GONE);
        }
        adapter.setEvents(events);
        adapter.notifyDataSetChanged();
    }

    public RegisterFragment() {}
    public static RegisterFragment newInstance(long id) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId=getArguments().getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        noEventsText =view.findViewById(R.id.t);
        Server.findRegister(getActivity(),RegisterFragment.this,userId);
        List<Event> myEvents = new ArrayList<>();
        RecyclerView recycler = view.findViewById(R.id.reg_recycler);
        //Настройка списка мероприятий
        adapter = new UsualEventRvAdapter(myEvents, (v, position) -> {
            Event event = eventList.get(position);
            showCaseFragment = ShowCaseFragment.newInstance(event.getId(), userId, 3);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.event_showcase_reg, showCaseFragment)
                    .commit();
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);
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
            Server.findRegister(getActivity(),RegisterFragment.this,userId);
            handler.postDelayed(this, 5000);
        }
    };
}