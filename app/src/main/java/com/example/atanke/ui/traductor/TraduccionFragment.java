package com.example.atanke.ui.traductor;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.atanke.R;
import com.example.atanke.databinding.TraductorFragmentBinding;
import com.example.atanke.traducirpalabras.client.TraducirPalabraClient;
import com.example.atanke.traducirpalabras.models.TraducirPalabraResponse;
import com.example.atanke.traducirpalabras.services.TraducirPalabraService;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TraduccionFragment extends Fragment implements  TextToSpeech.OnInitListener{

    private TraductorFragmentBinding binding;
    private TextView editTraduccion;
    private EditText editTraducir;
    private TraducirPalabraService traducirPalabraService;
    MotionEvent event;
    private boolean isFirstTap = false;
    private final Handler handler = new Handler();
    private TextView btnIdioma1, btnIdioma2, textView1,textView2;
    TextToSpeech textToSpeech;

    private final int REQUEST_RECORD_PERMISSION = 100;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.traductor_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        editTraduccion = requireView().findViewById(R.id.editTraduccion);
        editTraducir = requireView().findViewById(R.id.editTraducir);
        ImageButton imageButton = requireView().findViewById(R.id.imageButton);
        btnIdioma1 = requireView().findViewById(R.id.btnIdioma1);
        btnIdioma2 = requireView().findViewById(R.id.btnIdioma2);
        ImageView btnCopy = requireView().findViewById(R.id.copy);
        ImageView btnVoz = requireView().findViewById(R.id.btnVoz);
        ImageView imageView1 = requireView().findViewById(R.id.imageView2);
        ImageView imageView2 = requireView().findViewById(R.id.imageView3);
        ImageView btnclear = requireView().findViewById(R.id.btnclear);
        ImageView share = requireView().findViewById(R.id.share);
        LottieAnimationView recordingbubble = requireView().findViewById(R.id.recordingbubble);

        textView1 = requireView().findViewById(R.id.textView1);
        textView2 = requireView().findViewById(R.id.textView2);

        btnclear.setOnClickListener(view1 -> {
            editTraducir.setText("");
        }  );

        share.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, editTraduccion.getText().toString());

            startActivity(Intent.createChooser(intent, "Compartir traducción"));
        }  );

        btnVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertirTextoAVoz();
            }
        });

        textToSpeech = new TextToSpeech(getActivity(), this);

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", editTraduccion.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(requireActivity().getApplicationContext(), "Traduccion copiada en portapapeles", Toast.LENGTH_SHORT).show();
            }
        });

        textView1.setText(btnIdioma1.getText().toString());
        textView2.setText(btnIdioma2.getText().toString());

        imageButton.setOnClickListener(view1 -> {
            btnIdioma1.setText(btnIdioma1.getText().toString().equals("Español")?"Kankuamo":"Español");
            btnIdioma2.setText(btnIdioma2.getText().toString().equals("Español")?"Kankuamo":"Español");
            textView1.setText(btnIdioma1.getText().toString());
            textView2.setText(btnIdioma2.getText().toString());

            if(btnIdioma1.getText().toString().equals("Español")){
                Drawable drawable = getResources().getDrawable(R.drawable.banderasp);
                Drawable drawable2 = getResources().getDrawable(R.drawable.atanbande);

                imageView1.setImageDrawable(drawable);
                imageView2.setImageDrawable(drawable2);
            }else{
                Drawable drawable = getResources().getDrawable(R.drawable.banderasp);
                Drawable drawable2 = getResources().getDrawable(R.drawable.atanbande);

                imageView2.setImageDrawable(drawable);
                imageView1.setImageDrawable(drawable2);
            }


        });

        //traduccion de palabras a medida que va escribiendo el usuario
        editTraducir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                traducirPalabraService = TraducirPalabraClient.getApiService();
                if (!editTraducir.getText().toString().isEmpty())
                    getTraduccionPalabra(editTraducir.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editTraduccion.setOnTouchListener((v, event) -> {
            // Guardar el evento en la variable event
            TraduccionFragment.this.event = event;
            return false;
        });


        final Runnable tapTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                isFirstTap = false;
            }
        };

        // Agrega un listener de clics DOS VECES al TextView para saber que palabra selecciono
        editTraduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] pos = {0, 0};
                editTraduccion.getLocationOnScreen(pos);
                float x = event.getRawX() - pos[0];
                float y = event.getRawY() - pos[1];
                int offset = editTraduccion.getOffsetForPosition(x, y);
                String text = editTraduccion.getText().toString();
                int startIndex = text.lastIndexOf(" ", offset) + 1;
                int endIndex = text.indexOf(" ", offset);
                if (endIndex == -1) {
                    endIndex = text.length();
                }
                if (startIndex <= endIndex) {
                    String selectedWord = text.substring(startIndex, endIndex);
                    if (!selectedWord.trim().isEmpty()) {
                        if (!isFirstTap) {
                            isFirstTap = true;
                            handler.postDelayed(tapTimeoutRunnable, 500);
                        } else {
                            SharedPreferences preferences =  requireActivity().getSharedPreferences("seleccionPalabra", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("miPalabra", selectedWord);
                            editor.apply();

                            Log.e("",selectedWord);
                            isFirstTap = false;
                            OptionsBottomSheet bottomSheet = new OptionsBottomSheet();
                            bottomSheet.show(requireActivity().getSupportFragmentManager(), "OptionsBottomSheet");
                        }
                    }
                }
            }
        });

        ActivityResultLauncher<Intent> speechRecognitionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // Procesa los resultados del reconocimiento de voz
                    if (matches != null && !matches.isEmpty()) {
                        String recognizedText = matches.get(0);
                        // Haz algo con el texto reconocido, como mostrarlo en un EditText
                        editTraducir.setText(recognizedText);
                    }
                }
            }
        });
        recordingbubble.setOnClickListener(view1 -> {
            // Verifica si el reconocimiento de voz está disponible
            if (SpeechRecognizer.isRecognitionAvailable(requireActivity())) {

                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                speechRecognitionLauncher.launch(intent);
            } else {
                Toast.makeText(getActivity(), "Reconocimiento de voz no disponible", Toast.LENGTH_SHORT).show();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }


    private void getTraduccionPalabra(String data) {
        String fk_idioma = btnIdioma1.getText().toString().equals("Español")?"1":"2";
        traducirPalabraService.getTraducir(data,fk_idioma).enqueue(new Callback<TraducirPalabraResponse>() {
            @Override
            public void onResponse(@NonNull Call<TraducirPalabraResponse> call, Response<TraducirPalabraResponse> response) {
                if(response.body() != null){
                editTraduccion.setText(response.body().getTraduccion());
                mostrarTextoConColores(response.body().getTraduccion(), editTraduccion);
                }
                Log.e("TTS", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<TraducirPalabraResponse> call, Throwable t) {
                editTraduccion.setText(R.string.no_traduccion);
            }
        });
    }

    //Buscamos la parabas que comienzan por "<>" y le cambiamos el color a rojo
    public void mostrarTextoConColores(String texto, TextView editText) {
        SpannableString spannableString = new SpannableString(texto);
        Pattern pattern = Pattern.compile("<(.*?)>");
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String palabra = matcher.group();
            String palabraSinCaracteres = palabra.replaceAll("<|>", "");
            int inicio = matcher.start();
            int fin = matcher.end();
            spannableString.setSpan(new UnderlineSpan(), inicio, fin, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Editable editable = Editable.Factory.getInstance().newEditable(spannableString);
            editable.replace(inicio, fin, palabraSinCaracteres);
            spannableString = new SpannableString(editable);
            matcher = pattern.matcher(spannableString);
        }
        editText.setText(spannableString);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    public void convertirTextoAVoz() {
        Toast.makeText(requireActivity().getApplicationContext(), "Reproduciendo...", Toast.LENGTH_SHORT).show();
        String texto = editTraducir.getText().toString();

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