package com.example.veganadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

public class Registrierung extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn; //Link to login
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrierung);

        mFullName       = findViewById(R.id.register_fullName);
        mEmail          = findViewById(R.id.register_email_adress);
        mPassword       = findViewById(R.id.register_password);
        mRegisterBtn    = findViewById(R.id.register_btn);
        mLoginBtn       = findViewById(R.id.register_link_to_login);
        progressBar     = findViewById(R.id.register_progressBar);

        // Initialize Firebase Auth
        mAuth           = FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Es muss eine E-Mail Adresse eingegeben werden.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Es muss ein Passwort eingegeben werden.");
                    return;
                }
                if(password.length() < 8){
                    mPassword.setError("Das Passwort ist zu kurz (min. 8 Zeichen");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //register User in Firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Registrierung.this, "Account angelegt.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(Registrierung.this, "ERROR ! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        }
        );
    }
}