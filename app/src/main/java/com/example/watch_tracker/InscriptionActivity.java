package com.example.watch_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Déclaration de la classe InscriptionActivity qui étend AppCompatActivity
public class InscriptionActivity extends AppCompatActivity {

    //Déclaration des champs et boutons nécessaires à l'activité
    private EditText nomField, prenomField, emailField, passwordField;
    private Button inscriptionButton, retourButton; //Ajout du bouton de retour
    private FirebaseAuth mAuth; //Instance de FirebaseAuth pour gérer l'authentification


    //Méthode appelée lors de la création de l'activité
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Définition du layout associé à cette activité
        setContentView(R.layout.activity_inscription);

        //Récupère des références des champs et boutons depuis le layout
        nomField = findViewById(R.id.nomField);
        prenomField = findViewById(R.id.prenomField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        inscriptionButton = findViewById(R.id.inscriptionButton);
        retourButton = findViewById(R.id.retourButton);

        //Initialisation de l'instance FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //Définition de l'écouteur de clic pour le bouton d'inscription
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
            //Appel de la méthode registerUser lors du clic sur le bouton d'inscription
        });


        //Définition de l'écouteur de clic pour le bouton de retour
        retourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirection vers AuthActivity
                Intent intent = new Intent(InscriptionActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });
    }

    //Méthode pour enregistrer un nouvel user
    private void registerUser() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
//Création, inscription d'un nouvel utilisateur
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                        } else {

                            task.getException().printStackTrace();
                            Toast.makeText(InscriptionActivity.this, "Échec de l'inscription : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
