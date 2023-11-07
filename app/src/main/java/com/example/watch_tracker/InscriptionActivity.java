package com.example.watch_tracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

public class InscriptionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nomField;
    private EditText prenomField;
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mAuth = FirebaseAuth.getInstance();
        nomField = findViewById(R.id.nomFieldInscription);
        prenomField = findViewById(R.id.prenomFieldInscription);
        emailField = findViewById(R.id.emailFieldInscription);
        passwordField = findViewById(R.id.passwordFieldInscription);

        Button inscriptionButton = findViewById(R.id.inscriptionButtonInscription);

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String nom = nomField.getText().toString();
        String prenom = prenomField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inscription réussie, l'utilisateur est maintenant connecté
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // Échec de l'inscription, affichez un message d'erreur
                            Toast.makeText(InscriptionActivity.this, "Échec de l'inscription. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
