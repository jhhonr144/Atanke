package com.example.atanke.ui.sugerirtraduccion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atanke.R;
import com.example.atanke.general.utils.DialogBuilderDinamico;
import com.example.atanke.general.utils.ValidarEditTextVacios;
import com.example.atanke.ui.login.Login;
import com.example.atanke.ui.registrarse.Registrarse;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class SugerirTraduccion extends AppCompatActivity {
    AutoCompleteTextView idiomas;
    Spinner  codIdioma;
    EditText palabra,traduccion,pronunciacion;
    Button sugerir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sugerir_palabra_activity);

        sugerir = findViewById(R.id.btnSugerir);
        codIdioma = findViewById(R.id.codIdioma);
        palabra = findViewById(R.id.palabra);
        traduccion = findViewById(R.id.traduccion);
        pronunciacion = findViewById(R.id.pronunciacion);

        String[] idiomas = {"Kankuamo","Español",};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, idiomas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codIdioma.setAdapter(adapter);

        sugerir.setOnClickListener(view -> {
            List<EditText> editTexts = new ArrayList<>();
            editTexts.add(palabra);
            editTexts.add(traduccion);
            editTexts.add(pronunciacion);
            if(ValidarEditTextVacios.validarEditTextNoVacio(editTexts)){

            }else{
                DialogBuilderDinamico.camposVacias(this);
            }



            mensaje(this);
                }
        );
    }

    public void mensaje(Context context){
        new MaterialAlertDialogBuilder(context)
                .setTitle("¡Ups, parece que necesitas iniciar sesión!")
                .setMessage("Para acceder a esta función, debes iniciar sesión o crear una cuenta. ¡No te preocupes, es muy fácil!")
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);

                    }
                })
                .setNegativeButton("Registrarse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Registrarse.class);
                        context.startActivity(intent);
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