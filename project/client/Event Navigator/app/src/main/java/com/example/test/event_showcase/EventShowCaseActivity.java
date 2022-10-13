package com.example.test.event_showcase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.test.HubActivity;
import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Comment;
import com.example.test.model.Event;

import java.util.List;

public class EventShowCaseActivity extends AppCompatActivity {

    private long user;
    //Переменная owner отвечает за то, являетяс ли пользователь автором мероприятия
    private boolean owner = false;
    private RegistrationFragmentOwner registrationFragmentOwner;
    private ShowcaseInfo showcaseInfo;
    private ShowcaseComments showcaseComments;
    private RegistrationFragmentUser registrationFragmentUser;

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
        а также могут отвечать за ее отображение */
    public void update(Event event) {
        //Проверка, не пустое ли мероприятие
        if (event == null || event.getName().isEmpty()) {
            back();
            return;
        }
        owner = event.getOwner_id() == user;
        //Обновление данных в показываемых на фрагментах
        if (!owner) {
            registrationFragmentUser.update(event);
        } else {
            registrationFragmentOwner.update(event);
        }
        showcaseComments.update(event);
        showcaseInfo.update(event);
        Server.findByEvent(event, EventShowCaseActivity.this);
    }

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
        а также могут отвечать за ее отображение */
    public void update(List<Comment> comments) {
        showcaseComments.update(comments);
    }

    //Возврат в основную активность в случае, если мероприятия уже не существует
    public void back() {
        Toast.makeText(getApplicationContext(), "Похоже, данное мероприятие было удалено", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(EventShowCaseActivity.this, HubActivity.class);
        i.putExtra("id", user);
        startActivity(i);
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    @SuppressLint("UseCompatLoadingForDrawables")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_showcase);
        long id = getIntent().getExtras().getLong("id");
        user = getIntent().getExtras().getLong("user");
        registrationFragmentUser = RegistrationFragmentUser.newInstance(user, id);
        showcaseInfo = ShowcaseInfo.newInstance(id, user);
        registrationFragmentOwner = RegistrationFragmentOwner.newInstance(user, id);
        showcaseComments = ShowcaseComments.newInstance(id, user);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_showcase, showcaseInfo).commit();
        AppCompatButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
            Intent i = new Intent(EventShowCaseActivity.this, HubActivity.class);
            i.putExtra("id", user);
            startActivity(i);
        });
        Server.getEventById((int) id, EventShowCaseActivity.this);
        TextView info = findViewById(R.id.info_showcase);
        TextView comments = findViewById(R.id.comments_showcase);
        TextView register = findViewById(R.id.register_for_event);
        //Смена фрагментов в активности
        info.setOnClickListener(view -> {
            info.setBackground(getDrawable(R.drawable.ic_light_rect));
            comments.setBackground(getDrawable(R.drawable.ic_dark_rect));
            register.setBackground(getDrawable(R.drawable.ic_dark_rect));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_showcase, showcaseInfo).commit();
        });
        comments.setOnClickListener(view -> {
            info.setBackground(getDrawable(R.drawable.ic_dark_rect));
            comments.setBackground(getDrawable(R.drawable.ic_light_rect));
            register.setBackground(getDrawable(R.drawable.ic_dark_rect));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_showcase, showcaseComments).commit();
        });

        register.setOnClickListener(view -> {
            info.setBackground(getDrawable(R.drawable.ic_dark_rect));
            comments.setBackground(getDrawable(R.drawable.ic_dark_rect));
            register.setBackground(getDrawable(R.drawable.ic_light_rect));
            if (!owner)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_showcase, registrationFragmentUser).commit();
            else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_showcase,registrationFragmentOwner).commit();
            }
        });
    }
}
