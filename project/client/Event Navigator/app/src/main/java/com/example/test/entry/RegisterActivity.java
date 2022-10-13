package com.example.test.entry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.server.Server;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginRes;
    private TextView passwordRes;
    private TextView usernameRes;

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
        а также могут отвечать за ее отображение */
    public void update(int registerResult) {
        if (registerResult == 3) {
            loginRes.setVisibility(View.VISIBLE);
            loginRes.setTextColor(Color.RED);
            loginRes.setText("Логин: уже занят");
            passwordRes.setText("Пароль: ");
            usernameRes.setVisibility(View.GONE);
            passwordRes.setVisibility(View.GONE);
            usernameRes.setText("Имя пользователя: ");
            passwordRes.setTextColor(Color.BLACK);
            usernameRes.setTextColor(Color.BLACK);
        } else {
            if (registerResult == 1) {
                loginRes.setText("Логин: ");
                loginRes.setVisibility(View.GONE);
                passwordRes.setVisibility(View.GONE);
                usernameRes.setVisibility(View.GONE);
                passwordRes.setText("Пароль: ");
                usernameRes.setText("Имя пользователя: ");
                loginRes.setTextColor(Color.BLACK);
                passwordRes.setTextColor(Color.BLACK);
                usernameRes.setTextColor(Color.BLACK);
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
            } else if (registerResult == 0) {
                Toast.makeText(getApplicationContext(), "Ошибка при регистрации", Toast.LENGTH_SHORT).show();
            } else if (registerResult == 2) {
                loginRes.setText("Логин: ");
                loginRes.setVisibility(View.GONE);
                passwordRes.setVisibility(View.GONE);
                usernameRes.setVisibility(View.VISIBLE);
                passwordRes.setText("Пароль: ");
                loginRes.setTextColor(Color.BLACK);
                passwordRes.setTextColor(Color.BLACK);
                usernameRes.setTextColor(Color.RED);
                usernameRes.setText("Имя пользователя: уже занято");
            } else {
                loginRes.setVisibility(View.VISIBLE);
                loginRes.setTextColor(Color.RED);
                loginRes.setText("Логин: уже занят");
                passwordRes.setVisibility(View.GONE);
                usernameRes.setVisibility(View.VISIBLE);
                passwordRes.setText("Пароль: ");
                passwordRes.setTextColor(Color.BLACK);
                usernameRes.setTextColor(Color.RED);
                usernameRes.setText("Имя пользователя: уже занято");
            }
        }
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText login = findViewById(R.id.register_login);
        EditText password = findViewById(R.id.register_password);
        EditText userName = findViewById(R.id.register_name);
        loginRes = findViewById(R.id.register_text_1);
        passwordRes = findViewById(R.id.register_text_2);
        usernameRes = findViewById(R.id.register_text_3);
        AppCompatButton btn = findViewById(R.id.register_finish);
        passwordRes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_exlaim_mark, 0);
        loginRes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_exlaim_mark, 0);
        usernameRes.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_exlaim_mark, 0);
        btn.setOnClickListener(view -> {
            //Проверка вводимых данных
            String insertedLogin = login.getText().toString(), insertedPassword = password.getText().toString(), insertedName = userName.getText().toString();
            if (!insertedLogin.isEmpty() && !insertedPassword.isEmpty() && !insertedName.isEmpty()) {
                loginRes.setText("Логин: ");
                loginRes.setVisibility(View.GONE);
                passwordRes.setVisibility(View.GONE);
                usernameRes.setVisibility(View.GONE);
                passwordRes.setText("Пароль: ");
                usernameRes.setText("Имя пользователя: ");
                loginRes.setTextColor(Color.BLACK);
                passwordRes.setTextColor(Color.BLACK);
                usernameRes.setTextColor(Color.BLACK);
                Server.register(insertedLogin, insertedPassword, insertedName, RegisterActivity.this);
            } else {
                usernameRes.setVisibility(View.GONE);
                usernameRes.setVisibility(View.GONE);
                usernameRes.setVisibility(View.GONE);
                loginRes.setText("Логин: ");
                passwordRes.setText("Пароль: ");
                usernameRes.setText("Имя пользователя: ");
                loginRes.setTextColor(Color.BLACK);
                passwordRes.setTextColor(Color.BLACK);
                usernameRes.setTextColor(Color.BLACK);
                if (login.getText().toString().isEmpty()) {
                    loginRes.setTextColor(Color.RED);
                    loginRes.setVisibility(View.VISIBLE);
                    loginRes.setText("Логин: не введен логин");
                }
                if (password.getText().toString().isEmpty()) {
                    passwordRes.setTextColor(Color.RED);
                    passwordRes.setVisibility(View.VISIBLE);
                    passwordRes.setText("Пароль: пустой пароль");
                }
                if (userName.getText().toString().isEmpty()) {
                    usernameRes.setTextColor(Color.RED);
                    usernameRes.setVisibility(View.VISIBLE);
                    usernameRes.setText("Имя пользователя: не введено такое имя");
                }
            }
        });
    }

}