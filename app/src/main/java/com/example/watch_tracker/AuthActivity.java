    //Importation des bibliothèques
    package com.example.watch_tracker;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
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
    import com.google.protobuf.Empty;

    public class AuthActivity extends AppCompatActivity {

        //Déclaration des composants d'interface utilisateur
        private EditText emailField, passwordField;
        private Button loginButton, registerButton, resetpassword;

        //Instance d'authentification Firebase
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_auth);

            //Déclarations des composants du layout, interface utilisateur
            emailField = findViewById(R.id.emailField);
            passwordField = findViewById(R.id.passwordField);
            loginButton = findViewById(R.id.loginButton);
            registerButton = findViewById(R.id.inscriptionButton);
            resetpassword = findViewById(R.id.forgotmdp);

            //Initialisation de l'authentification Firebase
            mAuth = FirebaseAuth.getInstance();


            //Lorsqu'on clique sur le bouton "se connecter" on appelle la méthode loginUser()
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginUser();
                }
            });

            //Lorsqu'on clique sur le bouton "s'inscrire", on navigue vers InscriptionActivity
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AuthActivity.this, InscriptionActivity.class);
                    startActivity(intent);
                }
            });

            //Lorsqu'on clique sur le bouton "mot de passe oublié", on lance appelle la méthode resetPassword
            resetpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPassword();
                }
            });
        }

        //Méthode pour gérer la connexion de l'utilisateur
        private void loginUser() {
            // Récupère l'email et le mot de passe saisis
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            //On tente un connexion utilisateur en utilisateur en utilisateur l'authentification Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { //Si la connexion réussit, on ouvre l'activité de la liste personnelle
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(AuthActivity.this, Liste_personnelle.class);
                                startActivity(intent);
                            } else { //Si la connexion échoue, afficher un message d'erreur
                                Toast.makeText(AuthActivity.this, "Échec de l'authentification", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        //Méthode pour gérer la réinitialisation du mot de passe
        private void resetPassword() {
            //On récupère l'email saisi
            String email = emailField.getText().toString();
            if (!email.isEmpty()) { //Vérifier si l'email n'est pas vide
                // On envoie un email de  de réinitialisation du mot de passe et afficher un message en fonction du succès/échec
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) { //ecriture d'un message selon la reussite ou non
                                if (task.isSuccessful()) {
                                    Toast.makeText(AuthActivity.this, "Un e-mail de réinitialisation a été envoyé à votre adresse e-mail.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AuthActivity.this, "Échec de l'envoi de l'e-mail de réinitialisation.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else { // Si aucun email n'est saisi, demander à l'utilisateur d'entrer un email
                Toast.makeText(AuthActivity.this, "Merci de saisir un email", Toast.LENGTH_SHORT).show();
            }
        }
    }


