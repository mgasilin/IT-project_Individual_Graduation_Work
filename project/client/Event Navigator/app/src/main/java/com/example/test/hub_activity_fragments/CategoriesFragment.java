package com.example.test.hub_activity_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.test.location_service.address_service.AddressService;
import com.example.test.R;
import com.example.test.location_service.GetPositionFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    private SharedPreferences sharedPreferences;

    //Сохранение настроек категорий пользователя
    public void save(boolean first_criterion, boolean second_criterion, boolean third_criterion, boolean forth_criterion, boolean fifth_criterion, boolean sixth_criterion, boolean seventh_criterion, boolean eighth_criterion) {
        sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(userId + " cr1", first_criterion);
        editor.putBoolean(userId + " cr2", second_criterion);
        editor.putBoolean(userId + " cr3", third_criterion);
        editor.putBoolean(userId + " cr4", forth_criterion);
        editor.putBoolean(userId + " cr5", fifth_criterion);
        editor.putBoolean(userId + " cr6", sixth_criterion);
        editor.putBoolean(userId + " cr7", seventh_criterion);
        editor.putBoolean(userId + " cr8", eighth_criterion);
        editor.apply();
    }

    //Загрузки настроек категорий пользователя
    public ArrayList<Boolean> load() {
        ArrayList<Boolean> res = new ArrayList<Boolean>();
        sharedPreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        res.add(sharedPreferences.getBoolean(userId + " cr1", false));
        res.add(sharedPreferences.getBoolean(userId + " cr2", false));
        res.add(sharedPreferences.getBoolean(userId + " cr3", false));
        res.add(sharedPreferences.getBoolean(userId + " cr4", false));
        res.add(sharedPreferences.getBoolean(userId + " cr5", false));
        res.add(sharedPreferences.getBoolean(userId + " cr6", false));
        res.add(sharedPreferences.getBoolean(userId + " cr7", false));
        res.add(sharedPreferences.getBoolean(userId + " cr8", false));
        return res;
    }

    private long userId;

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    public CategoriesFragment() {
    }

    public static CategoriesFragment newInstance(/*param*/long id) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putLong("ID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getLong("ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ArrayList<Boolean> list = load();
        boolean isStreet = list.get(0);
        boolean isGroup = list.get(1);
        boolean isFamily = list.get(2);
        boolean isFree = list.get(3);
        boolean isCovid = list.get(4);
        boolean isRegister = list.get(5);
        boolean isSport = list.get(6);
        boolean isAgeRestricted = list.get(7);
        CheckBox isStreet_CheckBox = view.findViewById(R.id.isStreet_criterion);
        CheckBox isGroup_CheckBox = view.findViewById(R.id.isGroup_criterion);
        CheckBox isFamily_CheckBox = view.findViewById(R.id.isFamily_criterion);
        CheckBox isFree_CheckBox = view.findViewById(R.id.isFree_criterion);
        CheckBox isCovid_CheckBox = view.findViewById(R.id.isCovid_criterion);
        CheckBox isRegister_CheckBox = view.findViewById(R.id.isRegister_criterion);
        CheckBox isSport_CheckBox = view.findViewById(R.id.isSport_criterion);
        CheckBox isAgeRestricted_CheckBox = view.findViewById(R.id.isRestricted_criterion);

        isStreet_CheckBox.setChecked(isStreet);
        isGroup_CheckBox.setChecked(isGroup);
        isFamily_CheckBox.setChecked(isFamily);
        isFree_CheckBox.setChecked(isFree);
        isCovid_CheckBox.setChecked(isCovid);
        isRegister_CheckBox.setChecked(isRegister);
        isSport_CheckBox.setChecked(isSport);
        isAgeRestricted_CheckBox.setChecked(isAgeRestricted);
        //Сохрание изменений после их появления
        isStreet_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            list.set(0, b);
            save(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
        });
        isGroup_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            list.set(1, b);
            save(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
        });
        isFamily_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            list.set(2, b);
            save(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
        });
        isFree_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            list.set(3, b);
            save(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
        });
        isCovid_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            list.set(4, b);
            save(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
        });
        isRegister_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            list.set(5, b);
            save(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
        });
        isSport_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            list.set(6, b);
            save(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
        });
        isAgeRestricted_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            list.set(7, b);
            save(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
        });
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        LatLng position = new LatLng(Double.parseDouble(sharedPreferences.getString("start_lat", "57.333")), Double.parseDouble(sharedPreferences.getString("start_lng", "37.333")));
        TextView positionInfo = view.findViewById(R.id.reference_point);
        positionInfo.setText("Текущее место отсчета расстояния - " + AddressService.getAddress(position.latitude, position.longitude, getActivity()));
        AppCompatButton setPosition = view.findViewById(R.id.set_reference_point);
        setPosition.setOnClickListener(view1 -> getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, GetPositionFragment.newInstance(position, userId)).commit());
        return view;
    }
}