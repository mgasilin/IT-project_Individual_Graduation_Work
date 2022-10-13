package com.example.test.event_showcase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowCaseMap extends Fragment {
    public static ShowCaseMap newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong("id", id);
        ShowCaseMap fragment = new ShowCaseMap();
        fragment.setArguments(args);
        return fragment;
    }

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
        а также могут отвечать за ее отображение */
    public void update(Event event) {
        showcasedEvent = event;
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.show_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    //Настройка карты при создании
    private Event showcasedEvent;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(showcasedEvent.getX(), showcasedEvent.getY())));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(showcasedEvent.getX(), showcasedEvent.getY()), 9.0f));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(showcasedEvent.getX(), showcasedEvent.getY())).title(showcasedEvent.getName()).snippet(showcasedEvent.getDescription()));
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    };

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showcase_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long id = 0;
        if (getArguments() != null) {
            id = getArguments().getLong("id");
        }
        showcasedEvent = null;
        Server.getEventById((int) id, getActivity(), ShowCaseMap.this);
    }
}