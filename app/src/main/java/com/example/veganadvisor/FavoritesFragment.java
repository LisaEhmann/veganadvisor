package com.example.veganadvisor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment {
    //Objekte instanziieren

    private RecyclerView recycler_favorite_fragment;
    private View favoriten_content;
    private DatabaseReference restaurantRef;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        favoriten_content = inflater.inflate(R.layout.fragment_favoriten, container, false);

        recycler_favorite_fragment = (RecyclerView) favoriten_content.findViewById(R.id.recycler_favorite_fragment);

        restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurant");

        return favoriten_content;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<restaurant>().setQuery(restaurantRef, restaurant.class).build();
        final FirebaseRecyclerAdapter<restaurant, restaurantViewHolder> adapter = new FirebaseRecyclerAdapter<restaurant, restaurantViewHolder>(options) {
            @NonNull
            @Override
            public restaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content, viewGroup, false);
                restaurantViewHolder viewHolder = new restaurantViewHolder(view);

                return viewHolder;
            }


            @Override
            protected void onBindViewHolder(@NonNull final restaurantViewHolder holder, final int position, @NonNull restaurant model) {


                final ArrayList<restaurant> testrestaurants = new ArrayList<>();


                restaurantRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){

                            restaurant r = new restaurant();

                            r.setID(ds.child("id").getValue(String.class));
                            r.setName(ds.child("name").getValue(String.class));
                            r.setOpening(ds.child("opening").getValue(String.class));
                            r.setAdresse(ds.child("adresse").getValue(String.class));
                            r.setBeschreibung(ds.child("beschreibung").getValue(String.class));

                            testrestaurants.add(r);
                        }



                        //Log.i("DataSnapTest",""+dataSnapshot.toString());

                        holder.content_restaurantname.setText(testrestaurants.get(position).getName());
                        holder.content_restaurantdescription.setText(testrestaurants.get(position).getBeschreibung());

                        holder.content_btn_arrow.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                Bundle bundle = new Bundle();
                                bundle.putString("ID", testrestaurants.get(position).getID());
                                bundle.putString("Name", testrestaurants.get(position).getName());
                                bundle.putString("Opening", testrestaurants.get(position).getOpening());
                                bundle.putString("Adresse", testrestaurants.get(position).getAdresse());
                                bundle.putString("Beschreibung", testrestaurants.get(position).getBeschreibung());

                                DetailRestaurantFragment detailRestaurantFragment = new DetailRestaurantFragment();

                                detailRestaurantFragment.setArguments(bundle);
                                FragmentManager manager = getFragmentManager();
                                manager.beginTransaction().replace(R.id.container_fragment, detailRestaurantFragment).commit();

                                Toolbar toolbar = ((MainActivity)getActivity()).toolbar;
                                toolbar.setTitle(testrestaurants.get(position).getName());
                            }
                        });

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };

        recycler_favorite_fragment.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_favorite_fragment.setAdapter(adapter);
        recycler_favorite_fragment.hasFixedSize();
        adapter.startListening();

    }

    public static class restaurantViewHolder extends RecyclerView.ViewHolder{

        TextView content_restaurantname, content_restaurantdescription;
        ImageView content_placeholderimage;

        ImageButton content_btn_arrow;

        public restaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            content_restaurantname = itemView.findViewById(R.id.content_restaurantname);
            content_restaurantdescription = itemView.findViewById(R.id.content_restaurantdescription);

            content_placeholderimage = itemView.findViewById(R.id.content_placeholderimage);

            content_btn_arrow = itemView.findViewById(R.id.content_btn_arrow);


        }
    }

    }






