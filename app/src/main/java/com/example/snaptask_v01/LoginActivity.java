package com.example.snaptask_v01;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.snaptask_v01.utils.AppSessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    TextView textSignUp;
    EditText editSignInEmail, editSignInPassword;
    ProgressBar progressBar;
    MaterialButton btnSignIn;
    String textEmail, textPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String selectedRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editSignInEmail = findViewById(R.id.editSignInEmail);
        editSignInPassword = findViewById(R.id.editSignInPassword);
        progressBar = findViewById(R.id.signInProgressBar);
        btnSignIn = findViewById(R.id.btnSignIn);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        textSignUp = findViewById(R.id.textSignUp);

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEmail = editSignInEmail.getText().toString().trim();
                textPassword = editSignInPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(textEmail)) {
                    if (textEmail.matches(emailPattern)) {
                        if (!TextUtils.isEmpty(textPassword)) {
                            SignInUser();

                        } else {
                            editSignInPassword.setError("Password Field can't be empty");
                        }
                    } else {
                        editSignInEmail.setError("Enter a valid Email Address");
                    }
                } else {
                    editSignInEmail.setError("Email Field can't be empty");
                }
            }
        });
    }

//    private void SignInUser() {
//        progressBar.setVisibility(View.VISIBLE);
//        btnSignIn.setVisibility(View.INVISIBLE);
//
//        mAuth.signInWithEmailAndPassword(textEmail, textPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                Toast.makeText(LoginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(LoginActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.INVISIBLE);
//                btnSignIn.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }
private void SignInUser() {
    progressBar.setVisibility(View.VISIBLE);
    btnSignIn.setVisibility(View.INVISIBLE);

    mAuth.signInWithEmailAndPassword(textEmail, textPassword)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    String uid = authResult.getUser().getUid();
                    db = FirebaseFirestore.getInstance(); // initialize Firestore

                    db.collection("users").document(uid).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String email = documentSnapshot.getString("Email");
                                    String activeRole = documentSnapshot.getString("activeRole");

                                    // ✅ Set values in AppSessionManager
                                    AppSessionManager.getInstance().setUid(uid);
                                    AppSessionManager.getInstance().setEmail(email);
                                    AppSessionManager.getInstance().setActiveRole(activeRole);

                                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "User data not found!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    btnSignIn.setVisibility(View.VISIBLE);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(LoginActivity.this, "Error loading user data!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                btnSignIn.setVisibility(View.VISIBLE);
                            });
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                btnSignIn.setVisibility(View.VISIBLE);
            });
}

}