package com.example.test.hub_activity_fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.location_service.address_service.AddressService;
import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;
import com.example.test.event_showcase.ShowCaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment {

    private int state = 0;
    private List<Event> events = new ArrayList<>();
    private List<Event> eventsLen = new ArrayList<>();
    private List<Event> eventsCat = new ArrayList<>();
    private int search_len = 0;
    double start_position_lat;
    double x,y;
    double start_position_lng;
    private SeekBar length;
    private CheckBox apply_categories, searchByLength;
    private TextView search_length_text;
    private ShowCaseFragment showCaseFragment;
    private final Map<Marker, Long> eventHashMap = new HashMap<>();
    private GoogleMap map;
    private long id;

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
        а также могут отвечать за ее отображение */
    public void update() {
        switch (state) {
            case 1:
                events = eventsLen;
                break;
            case 2:
                events = eventsCat;
                break;
            case 3:
                events = intersectLists(eventsCat, eventsLen);
                break;
            default:
                Toast.makeText(getActivity(), "0 state", Toast.LENGTH_LONG).show();
        }
        eventHashMap.clear();
        if (map != null) {
            map.clear();
            for (Event event : events) {
                eventHashMap.put(map.addMarker(new MarkerOptions().position(new LatLng(event.getX(), event.getY()))), event.getId());
            }
        }
    }

    public void updateLen(List<Event> events,  int length) {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        x = Double.parseDouble(preferences.getString("start_lat", "57.333"));
        y = Double.parseDouble(preferences.getString("start_lng", "37.333"));
        ArrayList<Event> eventLen = new ArrayList<>();
        for (Event e : events) {
            if (AddressService.getDistance(new LatLng(x, y), new LatLng(e.getX(), e.getY())) <= length) {
                eventLen.add(e);
            }
        }
        this.eventsLen = eventLen;
        update();
    }

    public void updateCat(List<Event> eventsCat) {
        this.eventsCat = eventsCat;
        update();
    }

    //Пересечение двух списков
    private static List<Event> intersectLists(List<Event> list1, List<Event> list2) {
        List<Event> res = new ArrayList<>();
        for (Event event : list2) {
            for (Event event1 : list1) {
                if (event.getId() == event1.getId()) {
                    res.add(event);
                    break;
                }
            }
        }
        return res;
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong("id");
        }
    }

    //Создание и настройка карты
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            //Заполнение карты маркерами мероприятий
            for (Event event : events) {
                eventHashMap.put(map.addMarker(new MarkerOptions().position(new LatLng(event.getX(), event.getY()))), event.getId());
            }
            //Установка начальной позиции камеры
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(getActivity()
                    .getPreferences(Context.MODE_PRIVATE).getString("start_lat", "57.333")), Double.parseDouble(getActivity().
                    getPreferences(Context.MODE_PRIVATE).getString("start_lng", "37.333")))));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(getActivity()
                    .getPreferences(Context.MODE_PRIVATE).getString("start_lat", "57.333")), Double.parseDouble(getActivity().
                    getPreferences(Context.MODE_PRIVATE).getString("start_lng", "37.333"))), 9.0f));
            //Настройка нажатий на маркер
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    showCaseFragment = ShowCaseFragment.newInstance(eventHashMap.get(marker), id, 4);
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.event_showcase_maps, showCaseFragment)
                            .commit();
                    return false;
                }
            });
            //Настройка местоположения
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            map.setMyLocationEnabled(true);
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        apply_categories = view.findViewById(R.id.search_applyCategories);
        searchByLength = view.findViewById(R.id.searchByLen);
        length = view.findViewById(R.id.search_scrollView);
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        start_position_lat = Double.parseDouble(sharedPreferences.getString("start_lat", "57.333"));
        start_position_lng = Double.parseDouble(sharedPreferences.getString("start_lng", "37.333"));
        length.setMax(50000);
        length.setProgress(sharedPreferences.getInt("length", 0));
        search_len = sharedPreferences.getInt("length", 0);
        apply_categories.setChecked(sharedPreferences.getBoolean("apply_categories", false));
        searchByLength.setChecked(sharedPreferences.getBoolean("length_search", false));
        final boolean[] length_search = {searchByLength.isChecked()};
        final boolean[] apply_cats = {apply_categories.isChecked()};
        //Первоначльное заполнение карты
        if (length_search[0] && apply_cats[0]) {
            Server.getByCategories(getActivity(), getActivity().getPreferences(0), id, MapFragment.this);
            Server.findByLength(search_len, getActivity(), MapFragment.this);
            state = 3;
        } else if (length_search[0]) {
            Server.findByLength(search_len,  getActivity(), MapFragment.this);
            state = 1;
        } else {
            Server.getByCategories(getActivity(), getActivity().getPreferences(0), id, MapFragment.this);
            state = 2;
        }
        //Настройка смены вида поиска
        CompoundButton.OnCheckedChangeListener listener = (compoundButton, b) -> {
            length_search[0] = searchByLength.isChecked();
            apply_cats[0] = apply_categories.isChecked();
            if (length_search[0] && apply_cats[0]) {
                Server.getByCategories(getActivity(), getActivity().getPreferences(0), id, MapFragment.this);
                Server.findByLength(search_len, getActivity(), MapFragment.this);
                state = 3;
            } else if (length_search[0]) {
                Server.findByLength(search_len,  getActivity(), MapFragment.this);
                state = 1;
            } else {
                Server.getByCategories(getActivity(), getActivity().getPreferences(0), id, MapFragment.this);
                state = 2;
            }
            SharedPreferences sharedPreferences1 = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putBoolean("length_search", length_search[0]);
            editor.putBoolean("apply_categories", apply_cats[0]);
            editor.putInt("length", length.getProgress());
            editor.apply();
        };
        //Настройка длины для поиска по удаленности
        searchByLength.setOnCheckedChangeListener(listener);
        apply_categories.setOnCheckedChangeListener(listener);
        length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            //Обновление данных о длине после ее выставления
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                search_len = seekBar.getProgress();
                search_length_text.setText("Расстояние для поиска: " + search_len + " м");
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("length", search_len);
                editor.apply();
                boolean length_search = searchByLength.isChecked();
                boolean apply_cats = apply_categories.isChecked();
                if (length_search && apply_cats) {
                    Server.getByCategories(getActivity(), getActivity().getPreferences(0), id, MapFragment.this);
                    Server.findByLength(search_len,  getActivity(), MapFragment.this);
                    state = 3;
                } else if (length_search) {
                    Server.findByLength(search_len,  getActivity(), MapFragment.this);
                    state = 1;
                } else {
                    Server.getByCategories(getActivity(), getActivity().getPreferences(0), id, MapFragment.this);
                    state = 2;
                }
            }
        });
        search_length_text = view.findViewById(R.id.search_len);
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        search_length_text.setText("Расстояние для поиска: " + sp.getInt("length", 0) + " м");
        return view;
    }

    public static MapFragment newInstance(long id) {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
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
            //Подбор мероприятий в зависимости от типа поиска
            if (state == 3) {
                Server.getByCategories(getActivity(), getActivity().getPreferences(0), id, MapFragment.this);
                Server.findByLength(search_len,  getActivity(), MapFragment.this);
            } else if (state == 1) {
                Server.findByLength(search_len,  getActivity(), MapFragment.this);
            } else {
                Server.getByCategories(getActivity(), getActivity().getPreferences(0), id, MapFragment.this);
            }
            handler.postDelayed(this, 10000);
        }
    };
}