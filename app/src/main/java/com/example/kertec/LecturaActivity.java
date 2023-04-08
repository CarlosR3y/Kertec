package com.example.kertec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LecturaActivity extends AppCompatActivity {

    ScrollView scrollViewLectura;
    Spinner mSpinner;
    TextInputEditText fieldLecturaCliente;
    TextInputEditText fieldLecturaSerie;
    TextInputEditText fieldLecturaMarca;
    TextInputEditText fieldLecturaModelo;
    TextInputEditText fieldLecturaInicial;
    TextInputEditText fieldLecturaActual;
    TextInputEditText fieldLecturaProcesadas;

    TextView textViewProcesadas;

    MaterialButton btnLectura;

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
        scrollViewLectura = findViewById(R.id.scrollViewLectura);
        mSpinner = findViewById(R.id.mSpinner);
        fieldLecturaCliente = findViewById(R.id.fieldLecturaCliente);
        fieldLecturaSerie = findViewById(R.id.fieldLecturaSerie);
        fieldLecturaMarca = findViewById(R.id.fieldLecturaMarca);
        fieldLecturaModelo = findViewById(R.id.fieldLecturaModelo);
        fieldLecturaInicial = findViewById(R.id.fieldLecturaInicial);
        fieldLecturaActual = findViewById(R.id.fieldLecturaActual);
//        fieldLecturaProcesadas = findViewById(R.id.fieldLecturaProcesadas);
        textViewProcesadas = findViewById(R.id.textViewProcesadas);
        btnLectura = findViewById(R.id.btnLectura);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadData();


        btnLectura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String text1 = fieldLecturaInicial.getText().toString();
                    String text2 = fieldLecturaActual.getText().toString();

                    if (TextUtils.isEmpty(text1)) {
                        // mostrar mensaje de error o resaltar el EditText
                        fieldLecturaInicial.setError("Este campo es obligatorio");
                        return;
                    }

                    if (TextUtils.isEmpty(text2)) {
                        // mostrar mensaje de error o resaltar el EditText
                        fieldLecturaActual.setError("Este campo es obligatorio");
                        return;
                    }
                    else {
                        generarPDF();
                    }
                } catch (IOException | DocumentException | NumberFormatException e){
                    Snackbar.make(scrollViewLectura, "Revise los campos escritos", Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    private void generarPDF() throws IOException, DocumentException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        InputStream inputStream = getAssets().open("LecturaPlantilla.pdf");
        PdfReader reader = new PdfReader(inputStream);
        File file = new File(pdfPath, "Lectura.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        PdfStamper stamper = new PdfStamper(reader, outputStream);
        PdfContentByte content = stamper.getUnderContent(1);

        // Textos a llamar
        String txtCliente = fieldLecturaCliente.getText().toString();
        String txtSerie = fieldLecturaSerie.getText().toString();
        String txtMarca = fieldLecturaMarca.getText().toString();
        String txtModelo = fieldLecturaModelo.getText().toString();
        String txtLecturaInicial = fieldLecturaInicial.getText().toString();
        String txtLecturaActual = fieldLecturaActual.getText().toString();
//        String txtProcesadas = fieldLecturaProcesadas.getText().toString();
            int inicial = Integer.parseInt(txtLecturaInicial);
            int actual = Integer.parseInt(txtLecturaActual);

            if (inicial>actual){
                textViewProcesadas.setText(String.valueOf(inicial - actual));
            } else {
                textViewProcesadas.setText(String.valueOf(actual - inicial));
            }



        String txtProcesadas = textViewProcesadas.getText().toString();

        // Parrafos
        Phrase phraseCliente = new Phrase(txtCliente);
        Phrase phraseSerie = new Phrase(txtSerie);
        Phrase phraseMarca = new Phrase(txtMarca);
        Phrase phraseModelo = new Phrase(txtModelo);
        Phrase phraseLecturaInicial = new Phrase(txtLecturaInicial);
        Phrase phraseLecturaActual = new Phrase(txtLecturaActual);
        Phrase phraseLecturaProcesadas = new Phrase(txtProcesadas);

        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseCliente, 340, 600, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseSerie, 184, 420, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseMarca, 190, 520, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseModelo, 190, 465, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseLecturaInicial, 460, 520, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseLecturaActual, 460, 470, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseLecturaProcesadas, 460, 420, 0);

        stamper.close();
        reader.close();

        Snackbar.make(scrollViewLectura, "Lectura Generada", Snackbar.LENGTH_LONG).show();
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