package com.example.veganadvisor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoritesFragment extends Fragment {

    @Nullable

    private Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DatabaseReference myRef = database.getReference().child("restaurant").child("id1").child("adress");

        View v = inflater.inflate(R.layout.fragment_favoriten, container, false);
        final TextView textView = (TextView)v.findViewById(R.id.example);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String x = dataSnapshot.getValue(String.class);
                textView.setText(x);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return v;
    }


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        final DatabaseReference push = database.getReference().child("restaurant").child("id1").child("name");
        button = (Button) getView().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                push.setValue("Hallo") ;
            }
        });
    }


    }

