package com.example.test.new_event;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.test.HubActivity;
import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;
import com.example.test.location_service.GetPlaceActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewEventActivity extends AppCompatActivity {
    private long id;
    final String[] place = {""};
    final double[] x = {0};
    final double[] y = {0};
    final boolean[] couldBeCreated = {false};
    private TextView addressText;

    // Проверка корректности даты
    private boolean checkDate(String date) {
        if (date.length() != 8) {
            return false;
        } else {
            int day = (date.charAt(0) - '0') * 10 + (date.charAt(1) - '0');
            int month = (date.charAt(2) - '0') * 10 + (date.charAt(3) - '0');
            int year = (date.charAt(4) - '0') * 1000 + (date.charAt(5) - '0') * 100 + (date.charAt(6) - '0') * 10 + (date.charAt(7) - '0');
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    if (day > 31) {
                        Toast.makeText(getApplicationContext(), "День больше, чем количество дней в месяце", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (!(day <= 28 || (day <= 29 && (((year & 400) == 0) || (year % 4 == 0 && year % 100 != 0))))) {
                        Toast.makeText(getApplicationContext(), "День больше, чем количество дней в месяце", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day > 30) {
                        Toast.makeText(getApplicationContext(), "День больше, чем количество дней в месяце", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Неверно указан месяц", Toast.LENGTH_SHORT).show();
                    return false;
            }
            // Проверка, что указанная дата не раньше и не сильно позже
            long time = System.currentTimeMillis();
            String myDate = "" + year + "/" + month + "/" + day + " 23:59:59";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date actual_date = null;
            try {
                actual_date = sdf.parse(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            long millis = actual_date.getTime();
            if (millis - time > 0 && year<=Calendar.getInstance().get(Calendar.YEAR)+1) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Похоже, вы указали уже прошедшую дату, либо дату слишком наперед", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        final int[] limit = {100};
        final boolean[] has_reg = {false};
        final boolean[] has_lim = {false};
        id = getIntent().getExtras().getLong("id");
        addressText = findViewById(R.id.getAdress);

        EditText lim = findViewById(R.id.new_event_limit);
        EditText date = findViewById(R.id.getDate);
        EditText name = findViewById(R.id.getName);
        EditText description = findViewById(R.id.getDescription);

        CheckBox isStreet_checkbox = findViewById(R.id.isStreet);
        CheckBox isGroup_checkbox = findViewById(R.id.isGroup);
        CheckBox isFamily_checkbox = findViewById(R.id.isFamily);
        CheckBox isFree_checkbox = findViewById(R.id.isFree);
        CheckBox isCovid_checkbox = findViewById(R.id.isCovid);
        CheckBox isRegister_checkbox = findViewById(R.id.isRegister);
        CheckBox isSport_checkbox = findViewById(R.id.isSport);
        CheckBox isAgeRestricted_checkbox = findViewById(R.id.isAgeRestricted);
        CheckBox reg = findViewById(R.id.new_event_register);
        CheckBox has_limit = findViewById(R.id.new_event_hasLimit);

        reg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                has_reg[0] = b;
                if (b) {
                    has_limit.setVisibility(View.VISIBLE);
                    if (has_limit.isChecked()) {
                        lim.setVisibility(View.VISIBLE);
                    }
                } else {
                    has_limit.setVisibility(View.GONE);
                    lim.setVisibility(View.GONE);
                }
            }
        });
        has_limit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                has_lim[0] = b;
                if (b) {
                    lim.setVisibility(View.VISIBLE);
                } else {
                    lim.setVisibility(View.GONE);
                }
            }
        });
        AppCompatButton setPos = findViewById(R.id.startAddressActivity);
        setPos.setOnClickListener(view -> {
            Intent i = new Intent(NewEventActivity.this, GetPlaceActivity.class);
            startActivityForResult(i, 1);
        });
        AppCompatButton back = findViewById(R.id.new_event_back);
        back.setOnClickListener(view -> {
            Intent i = new Intent(NewEventActivity.this, HubActivity.class);
            i.putExtra("id", id);
            startActivity(i);
        });

        AppCompatButton btn = findViewById(R.id.new_event_check);
        btn.setOnClickListener(view -> {
            if (checkDate(date.getText().toString())) {
                if (!(name.getText().toString().isEmpty() || description.getText().toString().isEmpty() ||
                        place[0].isEmpty() || date.getText().toString().isEmpty() || !couldBeCreated[0])) {
                    boolean isStreet = isStreet_checkbox.isChecked();
                    boolean isCovid = isCovid_checkbox.isChecked();
                    boolean isGroup = isGroup_checkbox.isChecked();
                    boolean isFamily = isFamily_checkbox.isChecked();
                    boolean isFree = isFree_checkbox.isChecked();
                    boolean isRegister = isRegister_checkbox.isChecked();
                    boolean isSport = isSport_checkbox.isChecked();
                    boolean isAgeRestricted = isAgeRestricted_checkbox.isChecked();
                    try{
                        limit[0] =Integer.parseInt(lim.getText().toString());
                    }catch (Exception e){
                        limit[0]=100;
                    }
                    name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    Event e = new Event(date.getText().toString(), id, name.getText().toString(), description.getText().toString(),
                            place[0], x[0], y[0], isStreet, isGroup, isFamily, isFree, isCovid, isRegister, isSport, isAgeRestricted, has_reg[0],has_lim[0], limit[0]);
                    Server.insertEvent(e, getApplicationContext());
                    Intent i = new Intent(NewEventActivity.this, HubActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);
                } else {
                    date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    if (name.getText().toString().isEmpty()) {
                        name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_exlaim_mark, 0);
                    }
                    if (!couldBeCreated[0]) {
                        Toast.makeText(getApplicationContext(), "Не задан адрес", Toast.LENGTH_SHORT).show();
                        setPos.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_exlaim_mark, 0);
                    } else {
                        setPos.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    if (description.getText().toString().isEmpty()) {
                        description.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_exlaim_mark, 0);
                    }
                }
            } else {
                date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_exlaim_mark, 0);
            }
        });

    }


    // Обработка результата активности задания адреса
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                double lat1 = data.getDoubleExtra("lat", 1000);
                double lng1 = data.getDoubleExtra("lng", 1000);
                String adress = data.getStringExtra("adress");
                if (lng1 != 1000 && lat1 != 1000 && adress != null) {
                    x[0] = lat1;
                    y[0] = lng1;
                    place[0] = adress;
                    couldBeCreated[0] = true;
                    addressText.setText(place[0]);
                } else {
                    Toast.makeText(getApplicationContext(), "Ошибка при передаче, попробуйте снова", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}