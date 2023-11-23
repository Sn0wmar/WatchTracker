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

        private EditText emailField, passwordField;
        private Button loginButton, registerButton, resetpassword;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_auth);

            emailField = findViewById(R.id.emailField);
            passwordField = findViewById(R.id.passwordField);
            loginButton = findViewById(R.id.loginButton);
            registerButton = findViewById(R.id.inscriptionButton);
            resetpassword = findViewById(R.id.forgotmdp);

            mAuth = FirebaseAuth.getInstance();

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginUser();
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AuthActivity.this, InscriptionActivity.class);
                    startActivity(intent);
                }
            });
            resetpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPassword();
                }
            });
        }

        private void loginUser() {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(AuthActivity.this, Liste_personnelle.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AuthActivity.this, "Échec de l'authentification", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        private void resetPassword() {
            String email = emailField.getText().toString();
            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AuthActivity.this, "Un e-mail de réinitialisation a été envoyé à votre adresse e-mail.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AuthActivity.this, "Échec de l'envoi de l'e-mail de réinitialisation.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(AuthActivity.this, "Merci de saisir un email", Toast.LENGTH_SHORT).show();
            }
        }
    }


