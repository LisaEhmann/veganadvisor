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

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newrestaurant = inflater.inflate(R.layout.erstellen_restaurant, container, false);

        restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurant");

        //Elemente finden Erstelllen und Bewertung abgeben
        edit_restaurant_name        = newrestaurant.findViewById(R.id.edit_restaurant_name);
        edit_restaurant_description = newrestaurant.findViewById(R.id.edit_restaurant_description);
        edit_adress                 = newrestaurant.findViewById(R.id.edit_adress);
        edit_restaurant_opening     = newrestaurant.findViewById(R.id.edit_restaurant_opening);
        edit_bewertung              = newrestaurant.findViewById(R.id.edit_bewertung);
        btn_absenden                = newrestaurant.findViewById(R.id.edit_btn_absende);
        ratingBar                   = newrestaurant.findViewById(R.id.edit_ratingBar);
        //Elemente finden Upload Image
        mEdit_text_filename = newrestaurant.findViewById(R.id.edit_text_file_name);
        mBTN_choose_file    = newrestaurant.findViewById(R.id.edit_choose_file_btn);
        mBTN_upload_image   = newrestaurant.findViewById(R.id.edit_btn_upload);
        mImageView          = newrestaurant.findViewById(R.id.edit_image_view);
        mProgressBar        = newrestaurant.findViewById(R.id.edit_progressBar);
        mStorageRef     = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef    = FirebaseDatabase.getInstance().getReference("uploads");


        btn_absenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantErstellenBewertungAbgeben();
            }
        });

        mBTN_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(getActivity(), "Upload in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
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

        rating.setText(edit_bewertung.getText().toString());
        rating.setValue(ratingBar.getRating());


        if(!restaurant.getName().equals("") && !restaurant.getAdresse().equals("") && !restaurant.getBeschreibung().equals("") && !restaurant.getOpening().equals("") && !restaurant.getID().equals("") && !restaurant.getBeschreibung().equals("") && !rating.getText().equals("") && rating.getValue() != 0){

            ratingRef = FirebaseDatabase.getInstance().getReference().child("rating").child(restaurant.getID());

            restaurantRef.push().setValue(restaurant);
            ratingRef.push().setValue(rating);

            Bundle bundle = new Bundle();
            bundle.putString("Name", restaurant.getName());
            bundle.putString("Opening", restaurant.getOpening());
            bundle.putString("Adresse", restaurant.getAdresse());
            bundle.putString("Beschreibung", restaurant.getBeschreibung());
            bundle.putString("ID", restaurant.getID());

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

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                            Upload_Image upload = new Upload_Image(mEdit_text_filename.getText().toString().trim(),
                                    taskSnapshot.getStorage().getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
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
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }
}
