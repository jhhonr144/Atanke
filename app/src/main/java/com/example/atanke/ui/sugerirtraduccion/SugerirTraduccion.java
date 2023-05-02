package com.example.atanke.ui.sugerirtraduccion;

import static com.example.atanke.general.utils.DialogBuilderDinamico.detenerAlertaCargando;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.utils.DialogBuilderDinamico;
import com.example.atanke.general.utils.ValidarEditTextVacios;
import com.example.atanke.sugerirtraduccion.Dao.GetAllUsuariosTask;
import com.example.atanke.sugerirtraduccion.client.SugerirTraduccionClient;
import com.example.atanke.sugerirtraduccion.models.SugerirTraduccionRequest;
import com.example.atanke.sugerirtraduccion.models.SugerirTraduccionResponse;
import com.example.atanke.sugerirtraduccion.services.SugerirTraduccionService;
import com.example.atanke.ui.login.Login;
import com.example.atanke.ui.registrarse.Registrarse;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SugerirTraduccion extends AppCompatActivity {
    AutoCompleteTextView idiomas;
    Spinner  codIdioma;
    EditText palabra,traduccion,pronunciacion;
    Button sugerir;
    private SugerirTraduccionService service;
    List<UsuarioDTO> usuarios;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sugerir_palabra_activity);
        context = this;
        sugerir = findViewById(R.id.btnSugerir);
        codIdioma = findViewById(R.id.codIdioma);
        palabra = findViewById(R.id.palabra);
        traduccion = findViewById(R.id.traduccion);
        pronunciacion = findViewById(R.id.pronunciacion);


        String[] idiomas = {"Español","Kankuamo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, idiomas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codIdioma.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences("seleccionPalabra", Context.MODE_PRIVATE);
        String miPalabra = preferences.getString("miPalabra", "Palabra");
        palabra.setText(miPalabra);

        try {
            verificarSesion();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sugerir.setOnClickListener(view -> {
            List<EditText> editTexts = new ArrayList<>();
            editTexts.add(palabra);
            editTexts.add(traduccion);
            editTexts.add(pronunciacion);
            if(ValidarEditTextVacios.validarEditTextNoVacio(editTexts)){
                if (!usuarios.isEmpty()){
                    service = SugerirTraduccionClient.getApiService();
                    DialogBuilderDinamico.alertaCargando(this,"¡Casi listo! Estamos validando datos");
                    sugerirTraduccion(SugerirTraduccionRequest.builder()
                            .palabra(palabra.getText().toString())
                            .traduccion(traduccion.getText().toString())
                            .pronunciacion(pronunciacion.getText().toString())
                            .fkUser(usuarios.get(0).getId())
                            .codIdioma(codIdioma.getSelectedItem().toString().equals("Español") ? 1 : 2)
                            .codIdioma2(codIdioma.getSelectedItem().toString().equals("Español") ? 2 : 1)
                            .build());
                }else{
                    mensaje(this);
                }
            }else{
                DialogBuilderDinamico.camposVacias(this);
            }
                }
        );
    }

    private void verificarSesion() throws ExecutionException, InterruptedException {
        ConfigDataBase db = ConfigDataBase.getInstance(this);
        UsuarioDao userDao = db.usuarioDao();
        GetAllUsuariosTask task = new GetAllUsuariosTask(userDao);
        task.execute();
        usuarios = task.get();
        if(usuarios.isEmpty()){
            mensaje(this);
        }
    }

    private void sugerirTraduccion(SugerirTraduccionRequest build) {
        service.registrarUsuario("Bearer " + usuarios.get(0).getToken(),build).enqueue(new Callback<SugerirTraduccionResponse>() {
            @Override
            public void onResponse(Call<SugerirTraduccionResponse> call, Response<SugerirTraduccionResponse> response) {
                Log.e("", String.valueOf(response));
                detenerAlertaCargando();
                String[] botones = {"Continuar"};
                if(response.body()==null){
                    mensaje(context);
                }else {
                    if (response.body().getDataHeader().getCodRespuesta() == -1) {

                        DialogBuilderDinamico.alertaAdvertencia(context);
                    }
                    if (response.body().getDataHeader().getCodRespuesta() == 2) {
                        DialogBuilderDinamico.alertaDinamica(context, "¡Buen intento! ", "Ya se encuentra registrada esa traduccion"
                                , botones, true, new DialogBuilderDinamico.BotonClickListener() {
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
                    if (response.body().getDataHeader().getCodRespuesta() == 0) {
                        DialogBuilderDinamico.alertaDinamica(context, "¡Traduccion registrada! ", "Gracias por aportar y apoyarnos para seguir creciendo, su sugerencia pasara a revision."
                                , botones, true, new DialogBuilderDinamico.BotonClickListener() {
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
            }
            @Override
            public void onFailure(Call<SugerirTraduccionResponse> call, Throwable t) {
                detenerAlertaCargando();
                DialogBuilderDinamico.alertaAdvertencia(context);
            }
        });
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