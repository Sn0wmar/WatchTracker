package com.example.watch_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

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
        final String nom = nomField.getText().toString();
        final String prenom = prenomField.getText().toString();
        final String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Inscription Firebase", "Inscription réussie");
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                            // Enregistrement des données dans Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> user = new HashMap<>();
                            user.put("nom", nom);
                            user.put("prenom", prenom);
                            user.put("email", email);

                            db.collection("users")
                                    .document(email)
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Inscription Firestore", "Données enregistrées avec succès");
                                                Toast.makeText(InscriptionActivity.this, "Données enregistrées avec succès", Toast.LENGTH_SHORT).show();

                                                // Rediriger vers AuthActivity après l'inscription
                                                Intent intent = new Intent(InscriptionActivity.this, AuthActivity.class);
                                                startActivity(intent);
                                                finish();  // Fermer l'activité d'inscription
                                            } else {
                                                Log.e("Inscription Firestore", "Échec de l'inscription dans Firestore", task.getException());
                                                Toast.makeText(InscriptionActivity.this, "Échec de l'inscription dans Firestore. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Log.e("Inscription Firebase", "Échec de l'inscription dans Firebase", task.getException());
                            Toast.makeText(InscriptionActivity.this, "Échec de l'inscription dans Firebase. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
