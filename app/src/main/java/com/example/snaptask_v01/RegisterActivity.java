package com.example.snaptask_v01;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.snaptask_v01.utils.AppSessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView textSignIn;
    EditText editFullName, editEmail, editPhone, editPassword, editConfirmPassword;
    ProgressBar progressBar;
    MaterialButton btnSignUp, btnFreelancer, btnClient;
    String textFullName, textEmail, textMobile, textPassword, textConfirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String selectedRole;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textSignIn = findViewById(R.id.textSignIn);
        editFullName = findViewById(R.id.editFullName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        progressBar = findViewById(R.id.signUpProgressBar);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnClient = findViewById(R.id.btnClient);
        btnFreelancer = findViewById(R.id.btnFreelancer);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();


// Hold the selected role
        selectedRole = "";


// Define selected/unselected colors
        int selectedColor = ContextCompat.getColor(this, R.color.sapphireBlue);
//        int unselectedColor = ContextCompat.getColor(this, R.color.whisperWhite);

        //Toggle button
        btnClient.setOnClickListener(v -> {
            selectedRole = "client";

            // Highlight Client button
            btnClient.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            btnClient.setTextColor(Color.WHITE);

            // Reset Freelancer button
            btnFreelancer.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            btnFreelancer.setTextColor(selectedColor);
        });

        btnFreelancer.setOnClickListener(v -> {
            selectedRole = "freelancer";

            // Highlight Freelancer button
            btnFreelancer.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            btnFreelancer.setTextColor(Color.WHITE);

            // Reset Client button
            btnClient.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            btnClient.setTextColor(selectedColor);
        });



        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //SignUP
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textFullName = editFullName.getText().toString();
                textEmail = editEmail.getText().toString().trim();
                textMobile = editPhone.getText().toString().trim();
                textPassword = editPassword.getText().toString().trim();
                textConfirmPassword = editConfirmPassword.getText().toString().trim();

                if(!TextUtils.isEmpty(textFullName)){
                    if(!TextUtils.isEmpty(textEmail)){
                        if(textEmail.matches(emailPattern)){
                            if(!TextUtils.isEmpty(textMobile)){
                                if(textMobile.length() == 10){
                                    if(!TextUtils.isEmpty(textPassword)){
                                        if(!TextUtils.isEmpty(textConfirmPassword)){
                                            if(textConfirmPassword.equals(textPassword)){
                                                if (selectedRole.isEmpty()) {
                                                    Toast.makeText(RegisterActivity.this, "Please select a role (Client or Freelancer)", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                signUpUser();
                                            }else{
                                                editConfirmPassword.setError("Password & Confirm Password Not Matched");
                                            }
                                        }else{
                                            editConfirmPassword.setError("Confirm Password Field Can't Be Empty");
                                        }
                                    }else{
                                        editPassword.setError("Password Field Can't Be Empty");
                                    }
                                }else{
                                    editPhone.setError("Enter a valid Phone Number");
                                }
                            }else{
                                editPhone.setError("Mobile Number Field Can't Be Empty");
                            }
                        }else{
                            editEmail.setError("Enter a valid email address");
                        }
                    }else{
                        editEmail.setError("Email Field Can't Be Empty");
                    }
                }else{
                    editFullName.setError("Full Name Field Can't Be Empty");
                }

            }
        });

    }
//    private void signUpUser(){
//        progressBar.setVisibility(View.VISIBLE);
//        btnSignUp.setVisibility(View.INVISIBLE);
//        mAuth.createUserWithEmailAndPassword(textEmail,textPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
////                Toast.makeText(SignUpActivity.this, "Sign Up Successful !", Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
////                startActivity(intent);
////                finish();
//            Map<String, Object> user = new HashMap<>();
//                user.put("FullName", textFullName);
//                user.put("Email", textEmail);
//                user.put("MobileNumber", textMobile);
//                user.put("activeRole", selectedRole);
//                db.collection("users")
//                        .add(user)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                @Override
//                public void onSuccess(DocumentReference documentReference) {
//                    Toast.makeText(RegisterActivity.this, "Account Created Successfully !", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            })
//                    .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(RegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(RegisterActivity.this,"Error" + e.getMessage(),Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.INVISIBLE);
//                btnSignUp.setVisibility(View.VISIBLE);
//            }
//        });
//    }
    private void signUpUser() {
        progressBar.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(textEmail, textPassword)
                .addOnSuccessListener(authResult -> {
                    String uid = mAuth.getCurrentUser().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("FullName", textFullName);
                    user.put("Email", textEmail);
                    user.put("MobileNumber", textMobile);
                    user.put("activeRole", selectedRole);

                    db.collection("users").document(uid).set(user)
                            .addOnSuccessListener(unused -> {
                                AppSessionManager.getInstance().setUid(uid);
                                AppSessionManager.getInstance().setEmail(textEmail);
                                AppSessionManager.getInstance().setActiveRole(selectedRole);
                                Toast.makeText(RegisterActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                btnSignUp.setVisibility(View.VISIBLE);
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnSignUp.setVisibility(View.VISIBLE);
                });
    }

}