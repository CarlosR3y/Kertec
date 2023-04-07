package com.example.kertec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;

public class LecturaActivity extends AppCompatActivity {

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura);
    }
}