package com.example.veganadvisor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class erstellenRating extends Fragment {
    private String restaurantName;
    private View rating;
    private TextView erstellen_restaurantname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rating = inflater.inflate(R.layout.erstellen_bewertung, container, false);

        Bundle bundle = this.getArguments();
        restaurantName = bundle.getString("Name");

        //Elemente finden
        erstellen_restaurantname = rating.findViewById(R.id.erstellen_restaurantname);


        //Werte setzen
        erstellen_restaurantname.setText(restaurantName);

        return rating;
    }
}


