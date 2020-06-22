package com.example.veganadvisor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ScrollingView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class DetailRestaurantFragment extends Fragment {

    private RecyclerView recycler_restaurant_detail;
    private View restaurant_detail_content;
    private DatabaseReference ratingRef;
    private String restaurantName;
    private String restaurantAdresse;
    private String restaurantOpening;
    private String restaurantBeschreibung;
    private String restaurantID;
    private String restaurandBild;
    private ImageButton btn_subscribe;
    private TextView content_restaurantname, input_adresse, input_oeffnungszeiten, input_Beschreibung;
    private ImageView restaurant_detail_image;
    private FirebaseFirestore FStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        restaurant_detail_content = inflater.inflate(R.layout.fragment_restaurant_detailansicht, container,false);

        Bundle bundle = this.getArguments();
        restaurantName = bundle.getString("name");
        restaurantAdresse = bundle.getString("adresse");
        restaurantOpening = bundle.getString("opening");
        restaurantBeschreibung = bundle.getString("beschreibung");
        restaurantID = bundle.getString("id");
        restaurandBild = bundle.getString("bild");

        //Elemente finden
        content_restaurantname = restaurant_detail_content.findViewById(R.id.content_restaurantname);
        input_adresse = restaurant_detail_content.findViewById(R.id.input_adresse);
        input_oeffnungszeiten = restaurant_detail_content.findViewById(R.id.input_oeffnungszeiten);
        input_Beschreibung = restaurant_detail_content.findViewById(R.id.input_Beschreibung);
        btn_subscribe = restaurant_detail_content.findViewById(R.id.imageButton_edit);
        restaurant_detail_image = restaurant_detail_content.findViewById(R.id.restaurant_detail_image);

        //Werte setzen
        content_restaurantname.setText(restaurantName);
        input_adresse.setText(restaurantAdresse);
        input_oeffnungszeiten.setText(restaurantOpening);
        input_Beschreibung.setText(restaurantBeschreibung);
        Picasso.get().load(restaurandBild).into(restaurant_detail_image);

        btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("name", restaurantName);
                bundle.putString("opening", restaurantOpening);
                bundle.putString("adresse", restaurantAdresse);
                bundle.putString("beschreibung", restaurantBeschreibung);
                bundle.putString("id", restaurantID);
                bundle.putString("bild", restaurandBild);

                erstellenRating erstellenrating = new erstellenRating();
                erstellenrating.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.container_fragment, erstellenrating).commit();

                Toolbar toolbar = ((MainActivity)getActivity()).toolbar;
                toolbar.setTitle("Bewertung abgeben");
            }
        });
        recycler_restaurant_detail = (RecyclerView) restaurant_detail_content.findViewById(R.id.recycler_restaurant_detail);

        ratingRef = FirebaseDatabase.getInstance().getReference().child("rating").child(restaurantID);

        return restaurant_detail_content;
    }

    public void onStart(){
        super.onStart();

        final FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<rating>().setQuery(ratingRef, rating.class).build();
        final FirebaseRecyclerAdapter<rating, ratingViewHolder> adapter = new FirebaseRecyclerAdapter<rating, ratingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ratingViewHolder holder, final int position, @NonNull rating model) {

                final ArrayList<rating> testrating = new ArrayList<>();

                ratingRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){

                                final rating r = new rating();

                                r.setrID(ds.child("rID").getValue(String.class));
                                r.setText(ds.child("text").getValue(String.class));
                                r.setValue(ds.child("value").getValue(Float.class));
                                r.setuID(ds.child("uID").getValue(String.class));
                                testrating.add(r);
                        }

                        FStore = FirebaseFirestore.getInstance();
                        DocumentReference docref = FStore.collection("users").document(testrating.get(position).getuID());
                        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                holder.content_bewertung.setText(documentSnapshot.getString("fullName"));
                                Log.i("Test", documentSnapshot.getString("fullName"));
                            }
                        });
                            holder.content_bewertung_star.setRating(testrating.get(position).getValue());
                            holder.content_freitextBewertung.setText(testrating.get(position).getText());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @NonNull
            @Override
            public ratingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_bewertungen, parent, false);
                ratingViewHolder viewHolder = new ratingViewHolder(view);

                return viewHolder;
            }
        };

        recycler_restaurant_detail.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_restaurant_detail.setAdapter(adapter);
        recycler_restaurant_detail.hasFixedSize();
        adapter.startListening();
    }
    public static class ratingViewHolder extends RecyclerView.ViewHolder{

        TextView content_bewertung, content_freitextBewertung;
        RatingBar content_bewertung_star;

        public ratingViewHolder(@NonNull View itemView){
            super(itemView);
            content_bewertung = itemView.findViewById(R.id.content_bewertung);
            content_freitextBewertung = itemView.findViewById(R.id.content_freitextBewertung);

            content_bewertung_star = itemView.findViewById(R.id.content_bewertung_star);
        }
    }
}
