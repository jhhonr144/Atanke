package com.example.atanke.ui.login;

import static com.example.atanke.general.utils.DialogBuilderDinamico.detenerAlertaCargando;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atanke.MainActivity;
import com.example.atanke.R;
import com.example.atanke.general.utils.DialogBuilderDinamico;
import com.example.atanke.general.utils.ValidarEditTextVacios;
import com.example.atanke.iniciarsesion.client.IniciarSesionClient;
import com.example.atanke.iniciarsesion.dao.IniciarSesionAsyncTask;
import com.example.atanke.iniciarsesion.models.IniciarSesionRequest;
import com.example.atanke.iniciarsesion.models.IniciarSesionResponse;
import com.example.atanke.iniciarsesion.services.IniciarSesionService;
import com.example.atanke.ui.registrarse.Registrarse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText correo, contrasena;
    Button iniciar;
    TextView registrarse;
    private IniciarSesionService service;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        context = this;
        correo = findViewById(R.id.edtCorreo);
        contrasena = findViewById(R.id.edtContrasena);
        iniciar = findViewById(R.id.btnIniciar);
        registrarse = findViewById(R.id.txtRegistrarse);

        registrarse.setOnClickListener(view -> {
            MainActivity.irAClase(this, Registrarse.class);
        });

        iniciar.setOnClickListener(view -> {
            IniciarSesionRequest request = IniciarSesionRequest.builder()
                    .email(correo.getText().toString())
                    .password(contrasena.getText().toString())
                    .build();

            List<EditText> editTexts = new ArrayList<>();
            editTexts.add(contrasena);
            editTexts.add(correo);


            if(ValidarEditTextVacios.validarEditTextNoVacio(editTexts)){
                service = IniciarSesionClient.getApiService();
                iniciarSesion(request);
                DialogBuilderDinamico.alertaCargando(context,"¡Casi listo! Estamos validando datos.");

        }else{
                DialogBuilderDinamico.camposVacias(this);
            }
        });

    }

    private void iniciarSesion(IniciarSesionRequest request) {
        service.registrarUsuario(request).enqueue(new Callback<IniciarSesionResponse>() {
            @Override
            public void onResponse(Call<IniciarSesionResponse> call, Response<IniciarSesionResponse> response) {
                Log.e("", String.valueOf(response.body()));
                assert response.body() != null;
                detenerAlertaCargando();
                String[] botones = {"Continuar"};
                if(response.body().getId()==null){
                    DialogBuilderDinamico.alertaAdvertencia(context);
                }
                if (response.body().getId().equals(BigDecimal.valueOf(1))){

                    DialogBuilderDinamico.alertaDinamica(context,"¡Oops!","Usuario o contraseña incorrectos",botones,false,null);
                }
                if (response.body().getId().equals(BigDecimal.valueOf(0))){
                    guardarSesion(response);
                    DialogBuilderDinamico.alertaDinamica(context,"¡Bienvenido de nuevo! ","A aprender se ha dicho"
                            ,botones,true,new DialogBuilderDinamico.BotonClickListener(){
                                @Override
                                public void onPositiveButtonClicked() {
                                    finish();
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
            public void onFailure(Call<IniciarSesionResponse> call, Throwable t) {
                Log.e("", String.valueOf(t));
                detenerAlertaCargando();
                DialogBuilderDinamico.alertaAdvertencia(context);
            }
        });
    }

    private void guardarSesion(Response<IniciarSesionResponse> response) {
        new IniciarSesionAsyncTask(this, response).execute();
    }
}