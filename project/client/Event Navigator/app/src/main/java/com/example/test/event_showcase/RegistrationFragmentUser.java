package com.example.test.event_showcase;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;
import com.example.test.model.User;

import java.util.List;


public class RegistrationFragmentUser extends Fragment {
    private int state = 0;
    private long id;
    private long event_id;
    private TextView info;
    private AppCompatButton register;

    //Слушатели отвечат за то, какое именно действие будет выполняться при нажатии на кнопку записи
    //Действия при отсутствии возможности записи
    private final View.OnClickListener unavailable = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "В данный момент регистрация на мероприятие недоступна", Toast.LENGTH_SHORT).show();
        }
    };

    //Запись
    private final View.OnClickListener sign = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Server.sign(RegistrationFragmentUser.this, event_id, id);
            Server.getEventById((int) event_id, getActivity());
        }
    };

    //Отмена записи
    private final View.OnClickListener unSign = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Server.unsign(RegistrationFragmentUser.this, event_id, id);
            Server.getEventById((int) event_id, getActivity());
        }
    };

    public RegistrationFragmentUser() {
    }

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
    а также могут отвечать за ее отображение */
    @SuppressLint("ResourceAsColor")
    public void update(int reg) {
        state = reg;
        if (register == null) return;
        if (state == 1) {
            register.setText("Зарегистрироваться");
            register.setBackgroundColor(R.color.green);
            register.setOnClickListener(sign);
        } else if (state == 2) {
            register.setOnClickListener(unSign);
            register.setText("Вы уже зарегистрированы\n Отменить регистрацию?");
            register.setBackgroundColor(R.color.yellow);
        } else {
            register.setOnClickListener(unavailable);
            register.setText("Невозможно зарегистрироваться");
            register.setBackgroundColor(R.color.red);
        }
    }

    //Приводит дату к виду, удобному для вывода на экран
    String beautify(String date) {
        return date.charAt(0) + date.charAt(1) + "." + date.charAt(2) + date.charAt(3) + "." + date.charAt(4) + date.charAt(5) + date.charAt(6) + date.charAt(7);
    }

    @SuppressLint("ResourceAsColor")
    public void update(Event event) {
        String s = "Мероприятие: " + event.getName() + "\n\n\nДата проведения: " + beautify(event.getDate()) + "\n\n\n Место проведения: " + event.getPlace();
        if (info != null) info.setText(s);
        if (!event.isRegister() || event.getLimit() < event.getRegistrated().size()) {
            state = 0;
        } else {
            List<User> users = event.getRegistrated();
            state = 1;
            for (User u : users) {
                if (u.getId() == id) {
                    state = 2;
                    break;
                }
            }
        }
        if (state == 1 && event.getLimit() <= event.getRegistrated().size()) {
            state = 0;
        }
        update(state);
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    public static RegistrationFragmentUser newInstance(long id, long event_id) {
        RegistrationFragmentUser fragment = new RegistrationFragmentUser();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putLong("event", event_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event_id = getArguments().getLong("event");
            id = getArguments().getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        info = view.findViewById(R.id.event_register_info);
        register = view.findViewById(R.id.event_register_button);
        Server.getEventById((int) event_id, getActivity());
        return view;
    }

    //Пауза автообновления при паузе фрагмента
    @Override
    public void onPause() {
        handler.removeCallbacks(updaterRunnable);
        super.onPause();
    }

    //Возобновление автообновления при возобновлении работы фрагмента
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(updaterRunnable, 100);
    }

    //Создание автообновления
    private final Handler handler = new Handler();
    private final Runnable updaterRunnable = new Runnable() {
        public void run() {
            Server.getEventById((int) event_id, getActivity());
            handler.postDelayed(this, 5000);
        }
    };
}