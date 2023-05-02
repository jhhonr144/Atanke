package com.example.atanke.ui.traductor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.atanke.R;
import com.example.atanke.databinding.TraductorFragmentBinding;
import com.example.atanke.traducirpalabras.client.TraducirPalabraClient;
import com.example.atanke.traducirpalabras.models.TraducirPalabraResponse;
import com.example.atanke.traducirpalabras.services.TraducirPalabraService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TraduccionFragment extends Fragment {

    private TraductorFragmentBinding binding;
    private TextView editTraduccion;
    private EditText editTraducir;
    private TraducirPalabraService traducirPalabraService;
    MotionEvent event;
    private boolean isFirstTap = false;
    private final Handler handler = new Handler();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.traductor_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        editTraduccion = requireView().findViewById(R.id.editTraduccion);
        editTraducir = requireView().findViewById(R.id.editTraducir);

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


        super.onViewCreated(view, savedInstanceState);
    }


    private void getTraduccionPalabra(String data) {
        traducirPalabraService.getTraducir(data).enqueue(new Callback<TraducirPalabraResponse>() {
            @Override
            public void onResponse(@NonNull Call<TraducirPalabraResponse> call, Response<TraducirPalabraResponse> response) {
                editTraduccion.setText(response.body().getTraduccion());
                mostrarTextoConColores(response.body().getTraduccion(), editTraduccion);
            }

            @Override
            public void onFailure(Call<TraducirPalabraResponse> call, Throwable t) {
                Toast.makeText(getContext(), "No se puede traducir, verifica la conexion y vuelve a intentarlo", Toast.LENGTH_SHORT).show();
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
            spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), inicio, fin, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
}