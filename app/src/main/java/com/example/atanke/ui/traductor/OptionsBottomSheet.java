package com.example.atanke.ui.traductor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.atanke.MainActivity;
import com.example.atanke.R;
import com.example.atanke.ui.sugerirtraduccion.SugerirTraduccion;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OptionsBottomSheet extends BottomSheetDialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_inferior_opciones, container, false);

        // Configuramos el botón de la opción 1
        Button option1Button = view.findViewById(R.id.option1_button);
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el Modal Bottom Sheet y hacer algo con la opción 1
                dismiss();
                pasar();

            }
        });

        // Configuramos el botón de la opción 2
        Button option2Button = view.findViewById(R.id.option2_button);
        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el Modal Bottom Sheet y hacer algo con la opción 2
                dismiss();
                pasar();
            }
        });

        return view;
    }

    private void pasar(){
           MainActivity.irAClase(getContext(), SugerirTraduccion.class);
    }




}