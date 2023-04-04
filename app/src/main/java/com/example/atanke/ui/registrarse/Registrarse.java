
package com.example.atanke.ui.registrarse;

import static com.example.atanke.general.utils.DialogBuilderDinamico.detenerAlertaCargando;
import static com.example.atanke.general.utils.ValidarEditTextVacios.isValidEmail;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atanke.MainActivity;
import com.example.atanke.R;
import com.example.atanke.general.utils.DialogBuilderDinamico;
import com.example.atanke.general.utils.ValidarEditTextVacios;
import com.example.atanke.registrarusuario.Dao.GuardarSesionAsyncTask;
import com.example.atanke.registrarusuario.client.RegistrarUsuarioClient;
import com.example.atanke.registrarusuario.models.RegistrarUsuarioRequest;
import com.example.atanke.registrarusuario.models.RegistrarUsuarioResponse;
import com.example.atanke.registrarusuario.services.RegistrarUsuarioService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Registrarse extends AppCompatActivity {
    EditText nombreUsuario,correo,contrasena;
    Button registrarse;
    private RegistrarUsuarioService registrarUsuarioService;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse_activity);
        context = this;
        nombreUsuario = findViewById(R.id.editNombreUsu);
        correo = findViewById(R.id.editCorreo);
        contrasena = findViewById(R.id.editContrasena);
        registrarse = findViewById(R.id.btnRegistrarse);


        //validamos correo
        correo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Validar el correo electrónico
                if (!isValidEmail(s.toString())) {
                    correo.setError("Correo electrónico inválido");
                    registrarse.setEnabled(false);
                } else {
                    correo.setError(null);
                    registrarse.setEnabled(true);
                }
            }
        });
        //validamos contrasena
        contrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No es necesario hacer nada aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Validar la contraseña
                if (s.length() < 6) {
                    contrasena.setError("La contraseña debe tener al menos 6 caracteres");
                    registrarse.setEnabled(false);
                } else {
                    contrasena.setError(null);
                    registrarse.setEnabled(true);
                }
            }
        });

        registrarse.setOnClickListener(view -> {


            RegistrarUsuarioRequest request = RegistrarUsuarioRequest.builder()
                    .name(nombreUsuario.getText().toString())
                    .email(correo.getText().toString())
                    .password(contrasena.getText().toString())
                    .build();

            List<EditText> editTexts = new ArrayList<>();
            editTexts.add(nombreUsuario);
            editTexts.add(correo);
            editTexts.add(contrasena);
            if(ValidarEditTextVacios.validarEditTextNoVacio(editTexts)){
                registrarUsuarioService = RegistrarUsuarioClient.getApiService();
                registrarUsuario(request);
                DialogBuilderDinamico.alertaCargando(context,"¡Casi listo! Tu cuenta está en proceso de creación");
            }else{
                DialogBuilderDinamico.camposVacias(context);
            }
        });
    }

    private void registrarUsuario(RegistrarUsuarioRequest request) {
        registrarUsuarioService.registrarUsuario(request).enqueue(new Callback<RegistrarUsuarioResponse>() {
            @Override
            public void onResponse(Call<RegistrarUsuarioResponse> call, Response<RegistrarUsuarioResponse> response) {
                assert response.body() != null;
                if(response.body().getId()==null){
                    detenerAlertaCargando();
                  DialogBuilderDinamico.alertaAdvertencia(context);
                }
                Log.e("", String.valueOf(response.body()));
                if (response.body().getId().equals(BigDecimal.valueOf(-1))){
                    detenerAlertaCargando();
                    if(response.body().getErrores() != null){
                        if(response.body().getErrores().getEmail()!=null){
                            correo.setError("Este correo ya fue registrado anteriormente");}
                        if(response.body().getErrores().getName()!=null){
                            nombreUsuario.setError("Validar nombre de usuario");}
                        if(response.body().getErrores().getPassword()!=null){
                            contrasena.setError("La contraseña debe tener al menos 6 caracteres");}
                    }
                }
                if (response.body().getId().equals(BigDecimal.valueOf(0))){
                    guardarSesion(response);
                    detenerAlertaCargando();
                    String[] botones = {"Continuar"};
                    DialogBuilderDinamico.alertaDinamica(context,"Bienvenido/a al equipo","¡Estamos encantados de tenerte con nosotros! Gracias por unirte a nuestro equipo y contribuir a nuestra misión."
                            ,botones,true,new DialogBuilderDinamico.BotonClickListener(){
                                @Override
                                public void onPositiveButtonClicked() {
                                        MainActivity.irAClase(context, MainActivity.class);
                                }

                                @Override
                                public void onNegativeButtonClicked() {
                                }

                                @Override
                                public void onCancelButtonClicked() {
                                }
                            });

                }

            }

            @Override
            public void onFailure(Call<RegistrarUsuarioResponse> call, Throwable t) {
                detenerAlertaCargando();
                DialogBuilderDinamico.alertaAdvertencia(context);
            }
        });
    }



    private void guardarSesion(Response<RegistrarUsuarioResponse> response) {
        new GuardarSesionAsyncTask(this, response).execute();
    }

}