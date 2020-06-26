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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private FirebaseAuth FAuth;
    private String uID;
    private FirebaseFirestore FStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rating = inflater.inflate(R.layout.erstellen_bewertung, container, false);

        Bundle bundle = this.getArguments();
        restaurantName = bundle.getString("name");
        restaurantAdresse = bundle.getString("adresse");
        restaurantOpening = bundle.getString("opening");
        restaurantBeschreibung = bundle.getString("beschreibung");
        restaurantID = bundle.getString("id");

        ratingRef = FirebaseDatabase.getInstance().getReference().child("rating").child(restaurantID);
        FAuth = FirebaseAuth.getInstance();
        uID = FAuth.getCurrentUser().getUid();


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
                newrating.setuID(FAuth.getCurrentUser().getUid());

                if (!newrating.getText().equals("") && newrating.getValue() != 0) {

                    ratingRef.push().setValue(newrating);

                    Bundle bundle = new Bundle();
                    bundle.putString("name", restaurantName);
                    bundle.putString("opening", restaurantOpening);
                    bundle.putString("adresse", restaurantAdresse);
                    bundle.putString("beschreibung", restaurantBeschreibung);
                    bundle.putString("id", restaurantID);

                    DetailRestaurantFragment detailRestaurantFragment = new DetailRestaurantFragment();
                    detailRestaurantFragment.setArguments(bundle);
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.container_fragment, detailRestaurantFragment).commit();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "Alle Felder müssen ausgefüllt sein!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return rating;
    }
}


