package com.example.atanke.general.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.atanke.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogBuilderDinamico {
    private static ProgressDialog progressDialog;

    //util para alertar genericas
    public interface BotonClickListener {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
        void onCancelButtonClicked();
    }

    public static void alertaDinamica(Context context, String titulo, String mensaje, String[] botones, Boolean cancelable, BotonClickListener listener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setCancelable(!cancelable);

        if (botones != null) {
            for (String boton : botones) {
                switch (boton) {
                    case "Si":
                        builder.setPositiveButton(boton, (dialog, which) -> {
                            if (listener != null) {
                                listener.onPositiveButtonClicked();
                            }
                        });
                        break;
                    case "Continuar":
                        builder.setPositiveButton("Continuar", (dialog, which) -> {
                            if (listener != null) {
                                listener.onPositiveButtonClicked();
                            }
                        });
                        break;
                    case "No":
                        builder.setNegativeButton(boton, (dialog, which) -> {
                            if (listener != null) {
                                listener.onNegativeButtonClicked();
                            }
                        });
                        break;
                    case "Cancelar":
                        builder.setNeutralButton(boton, (dialog, which) -> {
                            if (listener != null) {
                                listener.onCancelButtonClicked();
                            }
                        });
                        break;
                }
            }
        }

        builder.show();
    }
    public static void camposVacias(Context context) {
                alertaDinamica(context, "Datos faltantes", "Por favor complete todos los campos.", new String[]{"Continuar"}, false,null);
    }

    public static void alertaAdvertencia(Context context) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle("¡Lo sentimos!");
        builder.setMessage("Parece que hubo un problema. Por favor, inténtalo de nuevo o contacta al soporte técnico.");
        builder.setIcon(R.drawable.ic_baseline_warning_amber_24);
        builder.show();
    }

    public static void alertaCargando(Context context,String mensaje){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(mensaje);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public static void detenerAlertaCargando() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
