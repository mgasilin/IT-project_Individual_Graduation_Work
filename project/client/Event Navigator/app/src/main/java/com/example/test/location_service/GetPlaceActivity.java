package com.example.test.location_service;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import com.example.test.location_service.address_service.AddressService;
import com.example.test.R;
import com.example.test.databinding.ActivityGetPlaceBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetPlaceActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private EditText address;
    private String address_value;
    private LatLng position;
    private boolean done = false;

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.test.databinding.ActivityGetPlaceBinding binding = ActivityGetPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatButton finish = findViewById(R.id.getPlace_done);
        AppCompatButton back = findViewById(R.id.getPlace_back);
        AppCompatButton set = findViewById(R.id.setAddress);
        address = findViewById(R.id.address);
        //ВЫор места по адресу
        set.setOnClickListener(view -> {
            try {
                String tmp = address.getText().toString();
                LatLng tempC = AddressService.getLocationFromAddress(getApplicationContext(), tmp);
                if (tempC != null) {
                    address_value = tmp;
                    position = tempC;
                    done = true;
                } else {
                    throw new Exception();
                }
                if (map != null) {
                    map.clear();
                    map.addMarker(new MarkerOptions().position(position).title("выбранное место"));
                }
            } catch (Exception e) {
                done = false;
                Toast.makeText(getApplicationContext(), "ошибка с адресом", Toast.LENGTH_SHORT).show();
            }
        });
        //Настройка кнопок
        back.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });
        finish.setOnClickListener(view -> {
            if (done) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("adress", address_value);
                returnIntent.putExtra("lat", position.latitude);
                returnIntent.putExtra("lng", position.longitude);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment_map);
        mapFragment.getMapAsync(this);
    }

    //Создание и настройка карты
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(latLng -> {
            String tmp;
            try {
                tmp = AddressService.getAddress(latLng.latitude, latLng.longitude, getApplicationContext());
                if (!tmp.isEmpty()) {
                    map.clear();
                    map.addMarker(new MarkerOptions().position(latLng).title("выбранное место"));
                    done = true;
                    position = latLng;
                    address.setText(tmp);
                    address_value = tmp;
                }
            } catch (Exception e) {
                done = false;
                Toast.makeText(getApplicationContext(), "ошибка при обработке координат", Toast.LENGTH_SHORT).show();
            }
        });
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(55.7, 37.6)));
    }
}