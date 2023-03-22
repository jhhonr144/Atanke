package com.example.atanke.ui.sugerirtraduccion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.example.atanke.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SugerirTraduccion extends AppCompatActivity {
    AutoCompleteTextView idiomas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sugerir_palabra_activity);
    }

    public void mensaje(){
        new MaterialAlertDialogBuilder(this)
                .setTitle("¡Ups, parece que necesitas iniciar sesión!")
                .setMessage("Para acceder a esta función, debes iniciar sesión o crear una cuenta. ¡No te preocupes, es muy fácil!")
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                })
                .setNegativeButton("Registrarse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Aquí va el código para abrir la pantalla de registro
                    }
                })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}