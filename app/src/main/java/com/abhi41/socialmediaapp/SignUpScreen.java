package com.abhi41.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abhi41.socialmediaapp.databinding.ActivitySignUpScreenBinding;
import com.abhi41.socialmediaapp.model.User;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpScreen extends AppCompatActivity {

    private ActivitySignUpScreenBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialization();
        setClickListeners();
    }

    private void initialization() {

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void setClickListeners() {
        binding.txtLogin.setOnClickListener(v -> {
            navigateLoginScreen();
        });

        binding.btnRegister.setOnClickListener(v ->
                registerUser()
        );
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(binding.textInputEmail.getEditText().getText().toString(),
                binding.textInputPassword.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(
                                    binding.txtInputName.getEditText().getText().toString(),
                                    binding.txtInputPreferance.getEditText().getText().toString(),
                                    binding.textInputEmail.getEditText().getText().toString(),
                                    binding.textInputPassword.getEditText().getText().toString()
                            );

                            //get unique id for firebase user
                            String id = task.getResult().getUser().getUid();
                            //store data in firebase
                            database.getReference().child("Users").child(id).setValue(user);
                            PrintMessage.printToastMessage(getApplicationContext(), "User Data Save");
                            navigateLoginScreen();
                        } else {
                            PrintMessage.printToastMessage(getApplicationContext(), "Error");
                        }
                    }
                });
    }
    private void navigateLoginScreen() {
        startActivity(new Intent(SignUpScreen.this, LoginScreen.class));
    }


}