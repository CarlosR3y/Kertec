package com.example.kertec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class ResetActivity extends AppCompatActivity {

    TextInputEditText inputReset;
    Button btnEnviar;
    AwesomeValidation awesomeValidation;
    LinearLayout resetLayout;
    FirebaseAuth firebaseAuth;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        btnEnviar = findViewById(R.id.btnEnviar);
        inputReset = findViewById(R.id.inputReset);
        resetLayout = findViewById(R.id.resetLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.inputReset, Patterns.EMAIL_ADDRESS, R.string.error_email);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datos = inputReset.getText().toString();

                if (awesomeValidation.validate()){
                    firebaseAuth.setLanguageCode("es");
                    firebaseAuth.sendPasswordResetEmail(datos).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Snackbar.make(resetLayout, "Correo enviado", Snackbar.LENGTH_LONG).show();
                                finish();
                            }else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                Error(errorCode);
                            }
                        }
                    });
                }
            }
        });



    }

    private void Error(String error){

        switch (error){

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(ResetActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(ResetActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(ResetActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(ResetActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                inputReset.setError("La dirección de correo electrónico está mal formateada.");
                inputReset.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(ResetActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                inputReset.setError("la contraseña es incorrecta ");
                inputReset.requestFocus();
                inputReset.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(ResetActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(ResetActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(ResetActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(ResetActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                inputReset.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                inputReset.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(ResetActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(ResetActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(ResetActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(ResetActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(ResetActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(ResetActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(ResetActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                inputReset.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                inputReset.requestFocus();
                break;
        }
    }
}