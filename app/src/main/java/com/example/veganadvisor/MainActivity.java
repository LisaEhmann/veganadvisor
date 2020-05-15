package com.example.veganadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast lisa = Toast.makeText(getApplicationContext(), "Hallo Lisa, ich hab dich lieb.", Toast.LENGTH_LONG);
        lisa.setGravity(Gravity.TOP, 3, 20);
        lisa.show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.startseite){
            Toast.makeText(getApplicationContext(), "You clicked Startseite", Toast.LENGTH_SHORT);
        } else if (id == R.id.favoriten){
            Toast.makeText(getApplicationContext(), "You clicked Favoriten", Toast.LENGTH_SHORT);
        } else if (id == R.id.profil){
            Toast.makeText(getApplicationContext(), "You clicked Profil", Toast.LENGTH_SHORT);
        } else if (id == R.id.einstellungen){
            Toast.makeText(getApplicationContext(), "You clicked Einstellungen", Toast.LENGTH_SHORT);
        } else if (id == R.id.suche){
            Toast.makeText(getApplicationContext(), "You clicked Suche", Toast.LENGTH_SHORT);
        }
        return true;
    }
}
