package com.example.kertec;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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

public class OrdenActivity extends AppCompatActivity {

    Button btnFinalizar;
    ScrollView scrollView;
    TextInputEditText fieldCliente;
    TextInputEditText fieldFecha;
    TextInputEditText fieldDepartamento;
    TextInputEditText fieldDireccion;
    TextInputEditText fieldNombre;
    TextInputEditText fieldTelefeno;
    TextInputEditText fieldEquipo;
    TextInputEditText fieldMarca;
    TextInputEditText fieldModelo;
    TextInputEditText fieldSerie;
    TextInputEditText fieldServicio;
    TextInputEditText fieldTarbajo;


    private static final int PERMISSION_REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden);

        btnFinalizar = findViewById(R.id.btnFinalizar);
        scrollView = findViewById(R.id.scrollView);

        fieldCliente = findViewById(R.id.fieldCliente);
        fieldFecha = findViewById(R.id.fieldFecha);
        fieldDepartamento = findViewById(R.id.fieldDepartamento);
        fieldDireccion = findViewById(R.id.fieldDireccion);
        fieldNombre = findViewById(R.id.fieldNombre);
        fieldTelefeno = findViewById(R.id.fieldTelefono);
        fieldEquipo = findViewById(R.id.fieldEquipo);
        fieldMarca = findViewById(R.id.fieldMarca);
        fieldModelo = findViewById(R.id.fieldModelo);
        fieldSerie = findViewById(R.id.fieldSerie);
        fieldServicio = findViewById(R.id.fieldServicio);
        fieldTarbajo = findViewById(R.id.fieldTrabajo);

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            btnFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        generarPDF();
                    }catch (IOException | DocumentException e){

                    }

                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes usar el almacenamiento
                btnFinalizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            generarPDF();
                        }catch (IOException | DocumentException e){

                        }

                    }
                });
            } else {
                // Permiso denegado, no puedes usar el almacenamiento
                Toast.makeText(OrdenActivity.this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generarPDF() throws IOException, DocumentException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        InputStream inputStream = getAssets().open("plantilla.pdf");
        PdfReader reader = new PdfReader(inputStream);
        File file = new File(pdfPath, "Ejemplo.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        PdfStamper stamper = new PdfStamper(reader, outputStream);
        PdfContentByte content = stamper.getUnderContent(1);
        //Variables
        String Cliente = fieldCliente.getText().toString();
        String Fecha = fieldFecha.getText().toString();
        String Departamento = fieldDepartamento.getText().toString();
        String Direccion =fieldDireccion.getText().toString();
        String dNombre = fieldNombre.getText().toString();
        String Telefeno = fieldTelefeno.getText().toString();
        String Equipo = fieldEquipo.getText().toString();
        String Marca = fieldMarca.getText().toString();
        String Modelo = fieldModelo.getText().toString();
        String Serie = fieldSerie.getText().toString();
        String Servicio = fieldServicio.getText().toString();
        String Trabajo = fieldTarbajo.getText().toString();

        Phrase phraseCliente = new Phrase(Cliente);
        Phrase phraseFecha = new Phrase(Fecha);
        Phrase phraseDepartamento = new Phrase(Departamento);
        Phrase phraseDireccion = new Phrase(Direccion);
        Phrase phraseNombre = new Phrase(dNombre);
        Phrase phraseTelefono = new Phrase(Telefeno);
        Phrase phraseEquipo = new Phrase(Equipo);
        Phrase phraseMarca = new Phrase(Marca);
        Phrase phraseModelo = new Phrase(Modelo);
        Phrase phraseSerie = new Phrase(Serie);
        Phrase phraseServicio = new Phrase(Servicio);
        Phrase phraseTrabajo = new Phrase(Trabajo);

        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseCliente, 100, 615, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseFecha, 410, 610, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseDepartamento, 220, 590, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseDireccion, 220, 565, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseNombre, 220, 510, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseTelefono, 220, 545, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseEquipo, 100, 470, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseMarca, 220, 470, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseModelo, 320, 470, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseSerie, 420, 470, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseServicio, 100, 400, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phraseTrabajo, 100, 350, 0);
        stamper.close();
        reader.close();

        Snackbar.make(scrollView, "Orden Generada", Snackbar.LENGTH_LONG).show();


    }
}