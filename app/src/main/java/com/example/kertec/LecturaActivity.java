package com.example.kertec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LecturaActivity extends AppCompatActivity {

    Spinner mSpinner;
    TextInputEditText fieldLecturaCliente;
    TextInputEditText fieldLecturaSerie;
    TextInputEditText fieldLecturaMarca;
    TextInputEditText fieldLecturaModelo;
    TextInputEditText fieldLecturaInicial;
    TextInputEditText fieldLecturaActual;
    TextInputEditText fieldLecturaProcesadas;

    String dataCliente;
    String dataSerie;
    String dataMarca;
    String dataModelo;

    DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura);
        mSpinner = findViewById(R.id.mSpinner);
        fieldLecturaCliente = findViewById(R.id.fieldLecturaCliente);
        fieldLecturaSerie = findViewById(R.id.fieldLecturaSerie);
        fieldLecturaMarca = findViewById(R.id.fieldLecturaMarca);
        fieldLecturaModelo = findViewById(R.id.fieldLecturaModelo);
        fieldLecturaInicial = findViewById(R.id.fieldLecturaInicial);
        fieldLecturaActual = findViewById(R.id.fieldLecturaActual);
        fieldLecturaProcesadas = findViewById(R.id.fieldLecturaProcesadas);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadData();






    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    private void loadData() {
        List<Clientes> clientesList = new ArrayList<>();
        mDatabase.child("clientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()) {
                            String cliente = ds.child("cliente").getValue().toString();
                            String marca = ds.child("marca").getValue().toString();
                            String modelo = ds.child("modelo").getValue().toString();
                            String serie = ds.child("serie").getValue().toString();
                            clientesList.add(new Clientes(cliente, marca, modelo, serie));
                    }
                        ArrayAdapter<Clientes> arrayAdapter = new ArrayAdapter<>(LecturaActivity.this, android.R.layout.simple_dropdown_item_1line, clientesList);
                    mSpinner.setAdapter(arrayAdapter);
                }

                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        dataCliente = parent.getItemAtPosition(position).toString();
                        fieldLecturaCliente.setText(dataCliente);

                        dataSerie = clientesList.get(position).getSerie();
                        fieldLecturaSerie.setText(dataSerie);

                        dataMarca = clientesList.get(position).getMarca();
                        fieldLecturaMarca.setText(dataMarca);

                        dataModelo = clientesList.get(position).getModelo();
                        fieldLecturaModelo.setText(dataModelo);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}