package com.abhi41.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abhi41.socialmediaapp.databinding.ActivityLoginScreenBinding;
import com.abhi41.socialmediaapp.untils.PrintMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {
    private ActivityLoginScreenBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialization();
        setClickListener();
    }

    private void initialization() {
          auth = FirebaseAuth.getInstance();
          currentUser = auth.getCurrentUser();
    }

    private void setClickListener() {
        binding.txtRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginScreen.this, SignUpScreen.class))
        );

        binding.btnLogin.setOnClickListener(v -> {
                authenticateUser();
        });
    }

    private void authenticateUser() {
            auth.signInWithEmailAndPassword(
                    binding.textInputEmail.getEditText().getText().toString(),
                    binding.textInputPassword.getEditText().getText().toString()
            ).addOnCompleteListener(task -> {

                if (task.isSuccessful()){
                    startActivity(new Intent(LoginScreen.this,MainActivity.class));
                }else {
                    PrintMessage.printToastMessage(getApplicationContext(),"Invalid User Name or Password");
                }

            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null){
            startActivity(new Intent(LoginScreen.this,MainActivity.class));
        }
    }
}