package com.example.test.event_showcase;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;

public class RegistrationFragmentOwner extends Fragment {

    private long id;
    private long event_id;
    //Массивы хранят данные которые были изначально и данные после изменений пользователя
    private final int[] limit = {100,100};
    private final boolean[] has_register = {false,false};
    private final boolean[] has_limit_list = {false,false};
    private CheckBox has_limit, register;
    private EditText limit_EditText;
    private AppCompatButton back,save,refresh;
    private String eventInfo;
    private TextView info;
    public RegistrationFragmentOwner() {}

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
        а также могут отвечать за ее отображение */
    void update(Event event){
        eventInfo = "Информация о записи: \n";
        if (!event.isRegister()){
            eventInfo +="Запись отключена.";
        }else{
            eventInfo +="Количество записавшихся - "+event.getRegistrated().size();
            if (event.isHas_limit()){
                eventInfo +=" из "+event.getLimit();
            }
        }
        if (info!=null){
            info.setText(eventInfo);
        }
        has_register[0]=event.isRegister();
        has_register[1]=event.isRegister();
        has_limit_list[0]=event.isHas_limit();
        has_limit_list[1]=event.isHas_limit();
        limit[0]=event.getLimit();
        limit[1]=event.getLimit();
        if (register !=null){
            register.setChecked(event.isRegister());
            if (has_register[0]) {
                has_limit.setVisibility(View.VISIBLE);
                if (has_limit_list[0]) {
                    limit_EditText.setVisibility(View.VISIBLE);
                }
            } else {
                has_limit.setVisibility(View.GONE);
                limit_EditText.setVisibility(View.GONE);
            }
        }
        if (has_limit!=null){
            if (has_limit_list[0]) {
                limit_EditText.setVisibility(View.VISIBLE);
            } else {
                limit_EditText.setVisibility(View.GONE);
            }
        }
    }


    public static RegistrationFragmentOwner newInstance(long id, long event_id) {
        RegistrationFragmentOwner fragment = new RegistrationFragmentOwner();
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
        View view =inflater.inflate(R.layout.fragment_registration_owner, container, false);
        back=view.findViewById(R.id.registration_owner_back);
        info=view.findViewById(R.id.registration_owner_info_1);
        register =view.findViewById(R.id.registration_owner_register);
        save=view.findViewById(R.id.registration_owner_save);
        refresh=view.findViewById(R.id.registration_owner_refresh);
        has_limit=view.findViewById(R.id.registration_owner_has_limit);
        limit_EditText =view.findViewById(R.id.registration_owner_limit);
        info.setText(eventInfo);
        limit_EditText.setText(limit[0]+"");
        has_limit.setChecked(has_limit_list[0]);
        register.setChecked(has_register[0]);
        if (register !=null){
            if (has_register[0]) {
                has_limit.setVisibility(View.VISIBLE);
                if (has_limit_list[0]) {
                    limit_EditText.setVisibility(View.VISIBLE);
                }
            } else {
                has_limit.setVisibility(View.GONE);
                limit_EditText.setVisibility(View.GONE);
            }
        }
        if (has_limit!=null){
            if (has_limit_list[0]) {
                limit_EditText.setVisibility(View.VISIBLE);
            } else {
                limit_EditText.setVisibility(View.GONE);
            }
        }
        //Изменение вида фрагмента в зависимости от того, какие параметры записи выставил пользователь
        register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                has_register[0] = b;
                if (b) {
                    has_limit.setVisibility(View.VISIBLE);
                    if (has_limit.isChecked()) {
                        limit_EditText.setVisibility(View.VISIBLE);
                    }
                } else {
                    has_limit.setVisibility(View.GONE);
                    limit_EditText.setVisibility(View.GONE);
                }
            }
        });
        has_limit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                has_limit_list[0] = b;
                if (b) {
                    limit_EditText.setVisibility(View.VISIBLE);
                } else {
                    limit_EditText.setVisibility(View.GONE);
                }
            }
        });
        //Действия основных кнопок
        //Возврат
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                has_limit_list[0]= has_limit_list[1];
                has_register[0]= has_register[1];
                limit[0]=limit[1];
                limit_EditText.setText(limit[0]+"");
                has_limit.setChecked(has_limit_list[0]);
                register.setChecked(has_register[0]);
            }
        });
        //Сохранение изменений
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    limit[0]=Integer.parseInt(limit_EditText.getText().toString());
                }catch (Exception e){
                    limit[0]=limit[1];
                }
                Server.update(getActivity(), has_limit_list[0], has_register[0],limit[0],event_id);

            }
        });
        //Обновление данных о записи
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server.getEventById((int) event_id, getActivity());
            }
        });
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