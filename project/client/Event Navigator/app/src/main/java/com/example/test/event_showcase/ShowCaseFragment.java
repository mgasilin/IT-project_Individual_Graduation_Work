package com.example.test.event_showcase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.test.HubActivity;
import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;

public class ShowCaseFragment extends Fragment {

    private long id;
    private long userId;
    private int code;
    private TextView date;
    private TextView name;
    private TextView address;

    //Преобразование даты к более удобному для вывода формату
    public static String beautifyEventDate(String date) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            result.append(date.charAt(i));
            if (i == 1 || i == 3) {
                result.append(".");
            }
        }
        return result.toString();
    }

    public ShowCaseFragment() {
    }

    //Возвращение в главную активность в случае, если мероприятия уже нет
    public void back() {
        Context activity = getActivity();
        ((HubActivity)activity).back(code);
        Toast.makeText(getActivity(), "Похоже, данное мероприятие было удалено", Toast.LENGTH_SHORT).show();
        destroy();
    }

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
        а также могут отвечать за ее отображение */
    public void update(Event event) {
        //Проверка, не пустое ли мероприятие
        if (event == null || event.getName().isEmpty()) {
            back();
        } else {
            date.setText("Дата проведения: " + beautifyEventDate(event.getDate()));
            name.setText("Мероприятие: " + event.getName());
            address.setText("Место проведения: " + event.getPlace());
        }
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    public static ShowCaseFragment newInstance(long id, long userId, int code) {
        ShowCaseFragment fragment = new ShowCaseFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putInt("code",code);
        args.putLong("user", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong("id");
            userId = getArguments().getLong("user");
            code=getArguments().getInt("code");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_case, container, false);
        name = view.findViewById(R.id.showcase_name);
        address = view.findViewById(R.id.showcase_address);
        date = view.findViewById(R.id.showcase_date);
        //Переход в активность просмотра мероприятия
        AppCompatButton btn = view.findViewById(R.id.showcase_showEvent);
        btn.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), EventShowCaseActivity.class);
            i.putExtra("id", id);
            i.putExtra("user", userId);
            startActivity(i);
        });

        AppCompatButton close = view.findViewById(R.id.showcase_close);
        close.setOnClickListener(view12 -> destroy());
        Server.getEventById((int) id, getActivity(), ShowCaseFragment.this);
        return view;
    }

    //Удаление фрагмента, если мероприятия уже не существует
    private void destroy() {
        getParentFragmentManager().beginTransaction().detach(this).commit();
    }

}