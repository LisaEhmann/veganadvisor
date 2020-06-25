package com.example.veganadvisor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class erstellenRestaurant extends Fragment {

    private DatabaseReference ratingRef;
    private DatabaseReference restaurantRef;
    private View newrestaurant;
    private EditText edit_restaurant_name, edit_adress, edit_bewertung, edit_restaurant_description, edit_restaurant_opening;
    private RatingBar ratingBar;
    private Button btn_absenden;
    private restaurant restaurant = new restaurant();
    private Random r = new Random();
    private rating rating = new rating();
    private FirebaseAuth FAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int i = r.nextInt(1000000000);

        newrestaurant = inflater.inflate(R.layout.erstellen_restaurant, container, false);

        restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurant");
        FAuth = FirebaseAuth.getInstance();

        //Elemente finden
        edit_restaurant_name = newrestaurant.findViewById(R.id.edit_restaurant_name);
        edit_restaurant_description = newrestaurant.findViewById(R.id.edit_restaurant_description);
        edit_adress = newrestaurant.findViewById(R.id.edit_adress);
        edit_restaurant_opening = newrestaurant.findViewById(R.id.edit_restaurant_opening);
        edit_bewertung = newrestaurant.findViewById(R.id.edit_bewertung);
        btn_absenden = newrestaurant.findViewById(R.id.edit_btn_absende);
        ratingBar = newrestaurant.findViewById(R.id.edit_ratingBar);

        btn_absenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restaurant.setName(edit_restaurant_name.getText().toString());
                restaurant.setAdresse(edit_adress.getText().toString());
                restaurant.setOpening(edit_restaurant_opening.getText().toString());
                restaurant.setID(Integer.toString(i));
                restaurant.setBeschreibung(edit_restaurant_description.getText().toString());
                restaurant.setBild("https://firebasestorage.googleapis.com/v0/b/veganadvisor-levn.appspot.com/o/LeerBild.jpg?alt=media&token=6cb98a17-b0f0-4ab0-b163-f0f946845906");

                rating.setText(edit_bewertung.getText().toString());
                rating.setValue(ratingBar.getRating());
                rating.setuID(FAuth.getCurrentUser().getUid());




                if(!restaurant.getName().equals("") && !restaurant.getAdresse().equals("") && !restaurant.getBeschreibung().equals("") && !restaurant.getOpening().equals("") && !restaurant.getID().equals("") && !restaurant.getBeschreibung().equals("") && !rating.getText().equals("") && rating.getValue() != 0){

                    ratingRef = FirebaseDatabase.getInstance().getReference().child("rating").child(restaurant.getID());

                    restaurantRef.push().setValue(restaurant);
                    ratingRef.push().setValue(rating);

                    Bundle bundle = new Bundle();
                    bundle.putString("name", restaurant.getName());
                    bundle.putString("opening", restaurant.getOpening());
                    bundle.putString("adresse", restaurant.getAdresse());
                    bundle.putString("beschreibung", restaurant.getBeschreibung());
                    bundle.putString("id", restaurant.getID());
                    bundle.putString("bild", restaurant.getBild());

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

        return newrestaurant;
    }
}
