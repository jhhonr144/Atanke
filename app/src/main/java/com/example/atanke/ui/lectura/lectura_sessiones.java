package com.example.atanke.ui.lectura;

import static com.example.atanke.general.utils.DialogBuilderDinamico.detenerAlertaCargando;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.atanke.general.utils.DialogBuilderDinamico;
import com.example.atanke.general.utils.NetworkUtils;
import com.example.atanke.lectura.Dao.GetLecturaSesionFk_lecturaTask;
import com.example.atanke.lectura.client.LecturaSesionClient;
import com.example.atanke.lectura.models.ItemLecturaSesionAdapter;
import com.example.atanke.lectura.models.LecturaSesionResponse;
import com.example.atanke.lectura.services.LecturaSesionService;
import com.example.atanke.traducirpalabras.client.TraducirPalabraClient;
import com.example.atanke.traducirpalabras.models.TraducirPalabraResponse;
import com.example.atanke.traducirpalabras.services.TraducirPalabraService;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class lectura_sessiones extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private String idTitulo, resultadoFinal, palabraSeleccionada;
    private RecyclerView recicle;
    private ConfigDataBase db;
    private List<BDLecturaSesionDTO> listSesiones;
    TextToSpeech textToSpeech;
    public TextView titulo, selecion, cantidad, fecha, autor, contenidoTraducido;
    private TraducirPalabraService traducirPalabraService;
    StringBuilder contenidoFinal;
    MotionEvent event;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_sessiones);
        Intent intent = getIntent();
        context = this;
        db = ConfigDataBase.getInstance(this);
        idTitulo = intent.getStringExtra("Titulo");
        if (idTitulo == null || idTitulo.equals("")) {
            finish();
            return;
        }
        recicle = findViewById(R.id.recicle_session);
        titulo = findViewById(R.id.txt_s_titulo);
        titulo.setText(intent.getStringExtra("Nombre"));
        fecha = findViewById(R.id.txt_s_fechap);
        fecha.setText(intent.getStringExtra("Fecha"));
        autor = findViewById(R.id.txt_s_autor);
        autor.setText(intent.getStringExtra("Autor"));
        contenidoTraducido = findViewById(R.id.txt_busqueda);

        boolean isInternetAvailable = NetworkUtils.isNetworkAvailable(this);
        if (isInternetAvailable) {
            cosultarSesionesApi();
        }else{
            try {
                CargarConfig();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("DATOS_LECTURAS", Context.MODE_PRIVATE);
        Set<String> contenidosSet = sharedPreferences.getStringSet("KEY_CONTENIDO", new HashSet<>());
        contenidoFinal = new StringBuilder();

        for (String contenido : contenidosSet) {
            contenidoFinal.insert(0, contenido);
            contenidoFinal.insert(0, " ");
        }

        resultadoFinal = "Título: " + titulo.getText().toString() + ". " + "Autor: " + autor.getText().toString() + ". " + contenidoFinal;

        findViewById(R.id.accion_narrar).setOnClickListener(v -> convertirTextoAVoz());
        findViewById(R.id.accion_compartir).setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.putExtra(Intent.EXTRA_TEXT, resultadoFinal);
            startActivity(Intent.createChooser(intent1, "Compartir informacion"));
        });
        findViewById(R.id.accion_buscar).setOnClickListener(v -> {
            traducirPalabraService = TraducirPalabraClient.getApiService();
            getTraduccionPalabra(String.valueOf(contenidoFinal), "1");
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), this);

        contenidoTraducido.setOnTouchListener((v, event) -> {
            // Guardar el evento en la variable event
            this.event = event;
            return false;
        });


        contenidoTraducido.setOnClickListener(v -> {
            int[] pos = {0, 0};
            contenidoTraducido.getLocationOnScreen(pos);
            float x = event.getRawX() - pos[0];
            float y = event.getRawY() - pos[1];
            int offset = contenidoTraducido.getOffsetForPosition(x, y);
            String text = contenidoTraducido.getText().toString();
            int startIndex = text.lastIndexOf(" ", offset) + 1;
            int endIndex = text.indexOf(" ", offset);
            if (endIndex == -1) {
                endIndex = text.length();
            }
            if (startIndex <= endIndex) {
                String selectedWord = text.substring(startIndex, endIndex);
                if (!selectedWord.trim().isEmpty()) {
                    palabraSeleccionada = selectedWord;
                    getTraduccionPalabra(selectedWord, "2");
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CargarConfig() throws ExecutionException, InterruptedException {
        BDLecturaSesionDao lsesiondao = db.BDLecturaSesionDao();
        GetLecturaSesionFk_lecturaTask task = new GetLecturaSesionFk_lecturaTask(lsesiondao, idTitulo);
        task.execute();
        listSesiones = task.get();
        if (listSesiones.isEmpty()) {
            cosultarSesionesApi();
        } else {
            cargarRecicle();
        }
    }

    private void cosultarSesionesApi() {
        DialogBuilderDinamico.alertaCargando(this,"¡Casi listo! Cargando información");
        LecturaSesionService lsServicio = LecturaSesionClient.getApiService();
        lsServicio.getLecturaSesion(idTitulo)
                .enqueue(new Callback<LecturaSesionResponse>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<LecturaSesionResponse> call,
                            @NonNull Response<LecturaSesionResponse> response) {
                        detenerAlertaCargando();
                        if (response.body() == null)
                            Toast.makeText(getBaseContext(), "No se puedo consultar lectura, por favor intentar mas tarde.", Toast.LENGTH_SHORT).show();
                        else {
                            if (response.body().getDatos_len() == 0)
                                Toast.makeText(getBaseContext(), "Lecturas vacia.", Toast.LENGTH_SHORT).show();
                            else {
                                guardarLecturasSesiones(response.body().getDatos());
                                listSesiones = response.body().getDatos();
                                cargarRecicle();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LecturaSesionResponse> call, @NonNull Throwable t) {
                        detenerAlertaCargando();
                        DialogBuilderDinamico.alertaAdvertencia(context);
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                CargarConfig();
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
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
        protected Void doInBackground(List<BDLecturaSesionDTO>... Sesiones) {
            BDLecturaSesionDao BDLecturaSesionDao = ConfigDataBase.getInstance(getBaseContext()).BDLecturaSesionDao();
            List<BDLecturaSesionDTO> sesiones = Sesiones[0];
            if (!sesiones.isEmpty()) {
                String fkLectura = sesiones.get(0).getFk_lectura() + "";
                BDLecturaSesionDao.deleteByfk_lectura(fkLectura);
                for (BDLecturaSesionDTO sesion : sesiones) {
                    BDLecturaSesionDao.insert(sesion);
                    for (BDLecturaContenidoDTO contenido : sesion.getContenido_lecturas()) {
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
        ItemLecturaSesionAdapter itemsRecicle = new ItemLecturaSesionAdapter(listSesiones);
        recicle.setAdapter(itemsRecicle);
    }

    public void convertirTextoAVoz() {
        Toast.makeText(getApplicationContext(), "Reproduciendo...", Toast.LENGTH_SHORT).show();
        String texto = resultadoFinal;

        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            textToSpeech.stop();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle params = new Bundle();
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "1.0f");
                textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, params, "1.0f");
            } else {
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

    private void getTraduccionPalabra(String data, String fk_idioma) {
        DialogBuilderDinamico.alertaCargando(this,"¡Casi listo! Buscando...");
        traducirPalabraService.getTraducir(data, fk_idioma).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TraducirPalabraResponse> call, @NonNull Response<TraducirPalabraResponse> response) {
                detenerAlertaCargando();
                if (fk_idioma.equals("1")) {
                    contenidoTraducido.setText(contenidoFinal);
                    assert response.body() != null;
                    mostrarTextoConColores(response.body().getTraduccion(), contenidoTraducido);
                    contenidoTraducido.setVisibility(View.VISIBLE);
                    recicle.setVisibility(View.GONE);
                } else {
                    assert response.body() != null;
                    getsnackbar(response.body().getTraduccion());
                }

            }

            @Override
            public void onFailure(@NonNull Call<TraducirPalabraResponse> call, @NonNull Throwable t) {
                detenerAlertaCargando();
                Toast.makeText(getBaseContext(), R.string.no_traduccion, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mostrarTextoConColores(String texto, TextView editText) {
        SpannableString spannableString = new SpannableString(texto);
        Pattern pattern = Pattern.compile("\\b(?![^<>]*>)\\w+\\b");
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int inicio = matcher.start();
            int fin = matcher.end();
            int color = Color.parseColor("#145737");
            spannableString.setSpan(new ForegroundColorSpan(color), inicio, fin, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        pattern = Pattern.compile("<(.*?)>");
        matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String palabra = matcher.group();
            String palabraSinCaracteres = palabra.replaceAll("<|>", "");
            int inicio = matcher.start();
            int fin = matcher.end();
            Editable editable = Editable.Factory.getInstance().newEditable(spannableString);
            editable.replace(inicio, fin, palabraSinCaracteres);
            spannableString = new SpannableString(editable);
            matcher = pattern.matcher(spannableString);
        }
        editText.setText(spannableString);
    }

    private void getsnackbar(String traduccion) {
        if (!traduccion.contains("<")) {
            Snackbar snackbar = Snackbar.make(contenidoTraducido, palabraSeleccionada + ": " + traduccion, Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.BLACK);
            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }
}