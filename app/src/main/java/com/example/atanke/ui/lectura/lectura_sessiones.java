package com.example.atanke.ui.lectura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.BDLecturaSesionDao;
import com.example.atanke.general.dto.api.sesiones.BDLecturaContenidoDTO;
import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;
import com.example.atanke.lectura.Dao.GetLecturaSesionFk_lecturaTask;
import com.example.atanke.lectura.client.LecturaSesionClient;
import com.example.atanke.lectura.models.ItemLecturaSesionAdapter;
import com.example.atanke.lectura.models.LecturaSesionResponse;
import com.example.atanke.lectura.services.LecturaSesionService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class lectura_sessiones extends AppCompatActivity implements  TextToSpeech.OnInitListener{
    private String idTitulo,resultadoFinal;
    private RecyclerView recicle;
    private ItemLecturaSesionAdapter itemsRecicle;
    private ConfigDataBase db;
    private List<BDLecturaSesionDTO> listSesiones;
    private LecturaSesionService lsServicio;
    TextToSpeech textToSpeech;
    public TextView titulo,selecion,cantidad,fecha,autor;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_sessiones);
        Intent intent = getIntent();
        db = ConfigDataBase.getInstance(this);
        idTitulo= intent.getStringExtra("Titulo");
        if(idTitulo==null || idTitulo.equals("")){
             finish();
             return;
        }
        recicle=findViewById(R.id.recicle_session);
        titulo=findViewById(R.id.txt_s_titulo);
        titulo.setText(intent.getStringExtra("Nombre"));
        fecha=findViewById(R.id.txt_s_fechap);
        fecha.setText(intent.getStringExtra("Fecha"));
        autor=findViewById(R.id.txt_s_autor);
        autor.setText(intent.getStringExtra("Autor"));
        //$$mostrar un cargando mientra bisca la info
        try {
            CargarConfig();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("DATOS_LECTURAS", Context.MODE_PRIVATE);
        Set<String> contenidosSet = sharedPreferences.getStringSet("KEY_CONTENIDO", new HashSet<>());
        StringBuilder contenidoFinal = new StringBuilder();

        Iterator<String> iterator = contenidosSet.iterator();
        while (iterator.hasNext()) {
            String contenido = iterator.next();
            contenidoFinal.insert(0, contenido);
            contenidoFinal.insert(0, " ");
        }

        resultadoFinal ="Titulo: "+ titulo.getText().toString()+". " +"Autor: "+autor.getText().toString() +". " + contenidoFinal;

        findViewById(R.id.accion_narrar).setOnClickListener(v -> convertirTextoAVoz());
        findViewById(R.id.accion_compartir).setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.putExtra(Intent.EXTRA_TEXT, resultadoFinal);
            startActivity(Intent.createChooser(intent1, "Compartir informacion"));
        });
        findViewById(R.id.accion_buscar).setOnClickListener(v -> {

        });
        textToSpeech = new TextToSpeech(getApplicationContext(), this);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CargarConfig() throws ExecutionException, InterruptedException {
        BDLecturaSesionDao lsesiondao= db.BDLecturaSesionDao();
        GetLecturaSesionFk_lecturaTask task = new GetLecturaSesionFk_lecturaTask(lsesiondao,idTitulo);
        task.execute();
        listSesiones = task.get();
        if(listSesiones.isEmpty()){
            cosultarSesionesApi();
        }
        else{
            cargarRecicle();
        }
    }

    private void cosultarSesionesApi()  {
        //$$MOSTRARCARGANDO$$
        //no hay datos descargado hay qe consultar a la web
        lsServicio = LecturaSesionClient.getApiService();
        lsServicio.getLecturaSesion(idTitulo)
            .enqueue(new Callback<LecturaSesionResponse>() {
                @Override
                public void onResponse(
                    @NonNull Call<LecturaSesionResponse> call,
                    Response<LecturaSesionResponse> response) {
                        if (response.body() == null)
                            Toast.makeText(getBaseContext(), "No se puede consultar la lectura, por favor pruebe despues", Toast.LENGTH_SHORT).show();
                        else {
                            if(response.body().getDatos_len()==0)
                                Toast.makeText(getBaseContext(), "Lecturas vacia, espere a que el admin publique informacion", Toast.LENGTH_SHORT).show();
                            else{
                                guardarLecturasSesiones(response.body().getDatos());
                                listSesiones=response.body().getDatos();
                                cargarRecicle();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LecturaSesionResponse> call, Throwable t) {
                        Toast.makeText(getBaseContext(),
                                "No se puede conectar con el servidor, validad tu conecion a internet",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void guardarLecturasSesiones(List<BDLecturaSesionDTO> datos) {
        new GuardarLecturasTask().execute(datos);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            Locale language = Locale.getDefault();
            int result = textToSpeech.setLanguage(language);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Lenguaje no soportado");
            }
        } else {
            Log.e("TTS", "Voz no disponible");
        }
    }

    private class GuardarLecturasTask extends AsyncTask<List<BDLecturaSesionDTO>, Void, Void> {
        @Override
        protected Void doInBackground(List<BDLecturaSesionDTO>... Sesiones ) {
            BDLecturaSesionDao BDLecturaSesionDao = ConfigDataBase.getInstance(getBaseContext()).BDLecturaSesionDao();
            List<BDLecturaSesionDTO> sesiones = Sesiones[0];
            if (!sesiones.isEmpty()) {
                String fkLectura = sesiones.get(0).getFk_lectura()+"";
                BDLecturaSesionDao.deleteByfk_lectura(fkLectura);
                for (BDLecturaSesionDTO sesion : sesiones) {
                    BDLecturaSesionDao.insert(sesion);
                    for(BDLecturaContenidoDTO contenido :sesion.getContenido_lecturas()){
                        BDLecturaSesionDao.insertc(contenido);
                    }
                }
            }
            return null;
        }
    }

    private void cargarRecicle() {
        LinearLayoutManager layautManayer = new LinearLayoutManager(getBaseContext());
        recicle.setLayoutManager(layautManayer);
        itemsRecicle= new ItemLecturaSesionAdapter(listSesiones);
        recicle.setAdapter(itemsRecicle);
    }

    public void convertirTextoAVoz() {
        Toast.makeText(getApplicationContext(), "Reproduciendo...", Toast.LENGTH_SHORT).show();
        String texto = resultadoFinal;

        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            // Parar el TextToSpeech previo
            textToSpeech.stop();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Utilizar una forma alternativa para dispositivos con API nivel >= 21
                Bundle params = new Bundle();
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "1.0f");
                textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, params, "1.0f");
            } else {
                // Utilizar la forma original para dispositivos con API nivel < 21
                textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @Override
    public void onDestroy() {
        // Liberar recursos de TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}