package com.example.watch_tracker;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;

//Déclaration de la classe InformationsActivity qui étend AppCompatActivity
public class InformationsActivity extends AppCompatActivity {

    //Méthode appellée lors de la création de l'activité
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Définition du layout
        setContentView(R.layout.informations);

        //Récupération de la référence ImageView du bouton
        ImageView mask = findViewById(R.id.Bouton_retour);

        //Définition d'un écouteur de clic sur le bouton retour
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
