package com.example.test.location_service.address_service;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class AddressService {

    //Получение коордиинат по адресу
    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng position = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address.size() == 0) {
                return null;
            }
            Address location = address.get(0);
            position = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return position;
    }

    //Получение адреса по координатам
    public static String getAddress(Double lat, Double lng, Context context) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> adress = null;
        try {
            adress = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (adress == null) {
            return "";
        } else if (adress.size() == 0) {
            return "";
        } else {
            return adress.get(0).getAddressLine(0);
        }
    }

    //Получение расстояния между координатами
    public static double getDistance(LatLng first, LatLng second) {
        double lat_first = first.latitude;
        double lng_first = first.longitude;
        double lat_second = second.latitude;
        double lng_second = first.longitude;
        Location locationFirst = new Location("point A");
        locationFirst.setLatitude(lat_first);
        locationFirst.setLongitude(lng_first);
        Location locationSecond = new Location("point B");
        locationSecond.setLatitude(lat_second);
        locationSecond.setLongitude(lng_second);
        return locationFirst.distanceTo(locationSecond);
    }
}
