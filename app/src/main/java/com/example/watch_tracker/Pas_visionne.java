package com.example.watch_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Pas_visionne extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pas_visionne);

        ImageView mask = findViewById(R.id.masquer2);
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2 = new Intent(getApplicationContext(), en_cours.class);
                startActivity(it2);
            }


        });


    }
}