package com.example.veganadvisor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class erstellenRating extends Fragment {
    private String restaurantName;
    private String restaurantID;
    private View rating;
    private TextView erstellen_restaurantname;
    private EditText freitext_rating;
    private Button btn_absenden;
    private DatabaseReference ratingRef;
    private rating newrating = new rating();
    private RatingBar ratingBar;
    private String restaurantAdresse;
    private String restaurantOpening;
    private String restaurantBeschreibung;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rating = inflater.inflate(R.layout.erstellen_bewertung, container, false);

        Bundle bundle = this.getArguments();
        restaurantName = bundle.getString("Name");
        restaurantAdresse = bundle.getString("Adresse");
        restaurantOpening = bundle.getString("Opening");
        restaurantBeschreibung = bundle.getString("Beschreibung");
        restaurantID = bundle.getString("ID");

        ratingRef = FirebaseDatabase.getInstance().getReference().child("rating").child(restaurantID);

        //Elemente finden
        erstellen_restaurantname = rating.findViewById(R.id.erstellen_restaurantname);
        freitext_rating = rating.findViewById(R.id.freitext_rating);
        btn_absenden = rating.findViewById(R.id.btn_absenden);
        ratingBar = rating.findViewById(R.id.ratingBar);

        //Werte setzen
        erstellen_restaurantname.setText(restaurantName);

        btn_absenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newrating.setText(freitext_rating.getText().toString());
                newrating.setValue(ratingBar.getRating());

               if(!newrating.getText().equals("") && newrating.getValue() != 0){

                   ratingRef.push().setValue(newrating);

                    Bundle bundle = new Bundle();
                    bundle.putString("Name", restaurantName);
                    bundle.putString("Opening", restaurantOpening);
                    bundle.putString("Adresse", restaurantAdresse);
                    bundle.putString("Beschreibung", restaurantBeschreibung);
                    bundle.putString("ID", restaurantID);

                    DetailRestaurantFragment detailRestaurantFragment = new DetailRestaurantFragment();
                    detailRestaurantFragment.setArguments(bundle);
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.container_fragment, detailRestaurantFragment).commit();
               }
                else{
                    Toast toast = Toast.makeText(getActivity(), "Alle Felder müssen ausgefüllt sein!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return rating;
    }
}


