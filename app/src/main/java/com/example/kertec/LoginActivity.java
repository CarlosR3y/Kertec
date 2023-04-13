package com.example.kertec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btnSesion;
    Button btnPassword;

    Button btnPyp;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    Button btnCrear;

    TextInputEditText fieldEmail;
    TextInputEditText fieldPassword;

    LinearLayout loginLayout;

    String correo = "";

//    ProgressDialog mDialog;
    BottomSheetDialog bDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSesion = findViewById(R.id.btnSesion);
        btnPassword = findViewById(R.id.btnPassword);
        btnCrear = findViewById(R.id.btnCrear);
        fieldEmail = findViewById(R.id.fieldEmail);
        fieldPassword = findViewById(R.id.fieldPassword);
        loginLayout = findViewById(R.id.loginLayout);
        btnPyp = findViewById(R.id.btnPyp);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

//        mDialog = new ProgressDialog(this);


        if(user != null){
            irAhome();
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.fieldEmail, Patterns.EMAIL_ADDRESS, R.string.error_email);
        awesomeValidation.addValidation(this, R.id.inputPassword, "[a-zA-Z\\s]+", R.string.error_email);
        awesomeValidation.addValidation(this, R.id.editTextPassword, Patterns.EMAIL_ADDRESS, R.string.error_email);

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    String mail = fieldEmail.getText().toString();
                    String password = fieldPassword.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                irAhome();
                            }else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                Error(errorCode);
                            }
                        }
                    });
                }

            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
                startActivity(intent);
            }
        });


        btnPyp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.dialog_webview);

                WebView webView = dialog.findViewById(R.id.webview);
                webView.loadUrl("file:///android_asset/politicas.html");
                Button btnClose = dialog.findViewById(R.id.btn_close);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void irAhome() {
        Intent intent = new Intent(LoginActivity.this, StartActivity.class);
        intent.putExtra("mail",fieldEmail.getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//    private void resetPassword() {
//        firebaseAuth.setLanguageCode("es");
//        firebaseAuth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Snackbar.make(loginLayout, "Se ha enviado el correo de recuperacion", Snackbar.LENGTH_LONG).show();
//                }else {
//                    Snackbar.make(loginLayout, "No se encontro el correo", Snackbar.LENGTH_LONG).show();
//                }
////                mDialog.dismiss();
//            }
//        });
//    }

    private void Error(String error){

        switch (error){

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(LoginActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(LoginActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(LoginActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(LoginActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                fieldEmail.setError("La dirección de correo electrónico está mal formateada.");
                fieldEmail.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(LoginActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                fieldPassword.setError("la contraseña es incorrecta ");
                fieldPassword.requestFocus();
                fieldPassword.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(LoginActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(LoginActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(LoginActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(LoginActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                fieldEmail.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                fieldEmail.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(LoginActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(LoginActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(LoginActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(LoginActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(LoginActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(LoginActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(LoginActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                fieldPassword.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                fieldPassword.requestFocus();
                break;
        }
    }
}