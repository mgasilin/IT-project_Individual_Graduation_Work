package com.example.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.test.hub_activity_fragments.CategoriesFragment;
import com.example.test.hub_activity_fragments.HomeFragment;
import com.example.test.hub_activity_fragments.MapFragment;
import com.example.test.hub_activity_fragments.RegisterFragment;
import com.example.test.hub_activity_fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HubActivity extends AppCompatActivity {
    private long id;

    /* Метод back отвечает за возврат в случае, если мероприятие было удалено в процессе работы с ним */
    public void back(int i) {
        Fragment selectedFragment;
        switch (i) {
            case 1:
                selectedFragment=HomeFragment.newInstance(id);
                break;
            case 2:
                selectedFragment=CategoriesFragment.newInstance(id);
                break;
            case 3:
                selectedFragment=SearchFragment.newInstance(id);
                break;
            case 5:
                selectedFragment=RegisterFragment.newInstance(id);
                break;
            default:
                selectedFragment=MapFragment.newInstance(id);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hub_activity);
        id = getIntent().getExtras().getLong("id");
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setItemIconTintList(null);
        bottomNav.setOnItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(id)).commit();
    }

    // Нижнее меню
    private final NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.main_home:
                    selectedFragment = HomeFragment.newInstance(id);
                    break;
                case R.id.search:
                    selectedFragment = SearchFragment.newInstance(id);
                    break;
                case R.id.categories:
                    selectedFragment = CategoriesFragment.newInstance(id);
                    break;
                case R.id.map_view:
                    selectedFragment = MapFragment.newInstance(id);
                    break;
                default:
                    selectedFragment = RegisterFragment.newInstance(id);
                    break;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };

}