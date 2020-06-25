package com.example.veganadvisor;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class erstellenRestaurant extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;


    private DatabaseReference ratingRef;
    private DatabaseReference restaurantRef;
    private View newrestaurant;
    private EditText edit_restaurant_name, edit_adress, edit_bewertung, edit_restaurant_description, edit_restaurant_opening, mEdit_text_filename;
    private RatingBar ratingBar;
    private Button btn_absenden, mBTN_choose_file, mBTN_upload_image;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private restaurant restaurant = new restaurant();
    private Random r = new Random();
    private rating rating = new rating();
    private final int i = r.nextInt(1000000000);
    private String mUri;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    private FirebaseAuth FAuth;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newrestaurant = inflater.inflate(R.layout.erstellen_restaurant, container, false);

        restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurant");
        FAuth = FirebaseAuth.getInstance();

        //Elemente finden Erstelllen und Bewertung abgeben
        edit_restaurant_name        = newrestaurant.findViewById(R.id.edit_restaurant_name);
        edit_restaurant_description = newrestaurant.findViewById(R.id.edit_restaurant_description);
        edit_adress                 = newrestaurant.findViewById(R.id.edit_adress);
        edit_restaurant_opening     = newrestaurant.findViewById(R.id.edit_restaurant_opening);
        edit_bewertung              = newrestaurant.findViewById(R.id.edit_bewertung);
        btn_absenden                = newrestaurant.findViewById(R.id.edit_btn_absende);
        ratingBar                   = newrestaurant.findViewById(R.id.edit_ratingBar);
        //Elemente finden Upload Image
        mBTN_choose_file    = newrestaurant.findViewById(R.id.edit_choose_file_btn);
        mImageView          = newrestaurant.findViewById(R.id.edit_image_view);
        mStorageRef     = FirebaseStorage.getInstance().getReference();
        mDatabaseRef    = FirebaseDatabase.getInstance().getReference("uploads");


        btn_absenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantErstellenBewertungAbgeben();
            }
        });


        mBTN_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        return newrestaurant;
    }


    private void restaurantErstellenBewertungAbgeben() {
        restaurant.setName(edit_restaurant_name.getText().toString());
        restaurant.setAdresse(edit_adress.getText().toString());
        restaurant.setOpening(edit_restaurant_opening.getText().toString());
        restaurant.setID(Integer.toString(i));
        restaurant.setBeschreibung(edit_restaurant_description.getText().toString());
        restaurant.setBild(mUri);

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


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            uploadImage(mImageUri);

//            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private void uploadImage(Uri ImageURI) {

        final StorageReference mIDRef = mStorageRef.child(edit_restaurant_name.getText().toString());

        mIDRef.putFile(ImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Picasso.get().load(mImageUri).into(mImageView);
//                Toast.makeText(getActivity(),"Irgendwas", Toast.LENGTH_SHORT).show();

                mIDRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mUri = uri.toString();
                    }
                });

            }
        });


    }
}
