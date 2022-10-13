package com.example.test.hub_activity_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;
import com.example.test.model.adapters.UsualEventRvAdapter;
import com.example.test.new_event.NewEventActivity;
import com.example.test.event_showcase.ShowCaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String ID = "id";
    private ShowCaseFragment showCaseFragment;
    private List<Event> eventList = new ArrayList<>();
    private long userId;
    private UsualEventRvAdapter adapter;

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    public HomeFragment() {
    }

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
    а также могут отвечать за ее отображение */
    public void update(List<Event> events) {
        eventList = events;
        adapter.setEvents(events);
        adapter.notifyDataSetChanged();
    }

    public static HomeFragment newInstance(long id) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putLong(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getLong(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Server.getByUserId((int) userId, getActivity(), HomeFragment.this);
        eventList = new ArrayList<>();
        //Создание обьекта интерфейса для отображения списка мероприятий
        RecyclerView eventRecycler = view.findViewById(R.id.home_recycler);
        adapter = new UsualEventRvAdapter(eventList, (v, position) -> {
            Event event = eventList.get(position);
            showCaseFragment = ShowCaseFragment.newInstance(event.getId(), userId,1);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.event_showcase_home, showCaseFragment)
                    .commit();
        });
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        eventRecycler.setLayoutManager(llm);
        eventRecycler.setAdapter(adapter);
        //Создание обработки свайпа и нажатия по списку
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                adapter.removeEvent(viewHolder.getLayoutPosition(), getActivity());
                adapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                try {
                    getChildFragmentManager().beginTransaction().detach(showCaseFragment).commit();
                } catch (Exception e) {
                    Log.d("Deleting fragment", "Nothing to delete");
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(eventRecycler);
        //Настройка кнопки создания мероприятия
        AppCompatButton new_event = view.findViewById(R.id.new_event);
        new_event.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), NewEventActivity.class);
            i.putExtra("id", userId);
            startActivity(i);
        });
        return view;
    }

    //Пауза автообновления при паузе фрагмента
    @Override
    public void onPause() {
        handler.removeCallbacks(updaterRunnable);
        super.onPause();
    }

    //Возобновление автообновления при работе фрагмента
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(updaterRunnable, 100);
    }

    //Создание автообновления
    private final Handler handler = new Handler();
    private final Runnable updaterRunnable = new Runnable() {
        public void run() {
            Server.getByUserId((int) userId, getActivity(), HomeFragment.this);
            handler.postDelayed(this, 5000);
        }
    };
}