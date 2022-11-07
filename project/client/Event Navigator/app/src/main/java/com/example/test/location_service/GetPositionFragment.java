package com.example.test.location_service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.test.location_service.address_service.AddressService;
import com.example.test.R;
import com.example.test.hub_activity_fragments.CategoriesFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetPositionFragment extends Fragment {

    private long id;
    private AppCompatButton back, done;
    private LatLng start_position;
    private LatLng position;
    private GoogleMap map;

    //Создание и настройка карты. Вызывается как только карта будет готова к использованию.
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            //Настройка кнопок
            back.setOnClickListener(view -> {
                position = start_position;
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("start_lat", "" + position.latitude);
                editor.putString("start_lng", "" + position.longitude);
                editor.apply();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, CategoriesFragment.newInstance(id)).commit();
            });
            done.setOnClickListener(view -> {
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("start_lat", "" + position.latitude);
                editor.putString("start_lng", "" + position.longitude);
                editor.apply();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, CategoriesFragment.newInstance(id)).commit();
            });
            //Настройка начального положения карты и нажатий на нее
            map = googleMap;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 9.0f));
            map.addMarker(new MarkerOptions().position(position).title(AddressService.getAddress(position.latitude, position.longitude, getActivity())));
            map.moveCamera(CameraUpdateFactory.newLatLng(position));
            map.setOnMapClickListener(latLng -> {
                map.clear();
                position = latLng;
                map.addMarker(new MarkerOptions().position(position).title(AddressService.getAddress(position.latitude, position.longitude, getActivity())));
                map.moveCamera(CameraUpdateFactory.newLatLng(position));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 9.0f));
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("start_lat", "" + position.latitude);
                editor.putString("start_lng", "" + position.longitude);
                editor.apply();
            });
        }
    };

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_position, container, false);
        back = view.findViewById(R.id.getPos_back);
        done = view.findViewById(R.id.setPos_set);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong("id");
            position = new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
            start_position = new LatLng(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
        } else {
            id = 0;
            position = new LatLng(57.7, 37.7);
            start_position = new LatLng(57.7, 37.7);
        }

    }

    public static GetPositionFragment newInstance(LatLng pos, long id) {
        Bundle args = new Bundle();
        args.putDouble("lat", pos.latitude);
        args.putDouble("lng", pos.longitude);
        args.putLong("id", id);
        GetPositionFragment fragment = new GetPositionFragment();
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
}