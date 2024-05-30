package com.stemgon.autheply;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button signUpNow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        signUpNow = findViewById(R.id.signUpNow);
    }
}