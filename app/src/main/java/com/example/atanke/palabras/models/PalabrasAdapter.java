package com.example.atanke.palabras.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;

import java.util.List;

public class PalabrasAdapter extends RecyclerView.Adapter<PalabrasAdapter.ViewHolder> {
    private List<palabrasRelacion> datos;
    private ConfigDataBase db;
    public PalabrasAdapter(List<palabrasRelacion> listaTodo) {
        datos=listaTodo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_diccionario_palabras, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Asignar valores a las vistas del elemento
        String palabra = datos.get(position).palabra;
        String primeraLetraMayuscula = TextUtils.isEmpty(palabra) ? "" : palabra.substring(0, 1).toUpperCase() + palabra.substring(1);
        holder.textView1.setText(primeraLetraMayuscula);
        holder.textView2.setText(datos.get(position).pronunciar);
        String traduccion = datos.get(position).palabra1;
        String primeraLetraMayusculatradu = TextUtils.isEmpty(traduccion) ? "" : traduccion.substring(0, 1).toUpperCase() + traduccion.substring(1);
        holder.textView4.setText(primeraLetraMayusculatradu);
        holder.textView5.setText(datos.get(position).pronunciar1);
        holder.textView3.setText(datos.get(position).count+"");
       // if(datos.get(position).count==0)
       //     holder.boton.setText("Ir a agregar");
        holder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarInformacionSegundoRecyclerView( v.getContext(),datos.get(position));
            }
        });
    }

    private void cargarInformacionSegundoRecyclerView(Context context, palabrasRelacion palabrasRelacion) {
        //##puss aqui pasa a una activity por que otro recicle me da error lol
        Intent intent = new Intent(context, activity_diccionario_palabra_contenido.class);
        intent.putExtra("objeto", palabrasRelacion); // Pasa el ID como extra del intent
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return datos==null?0:datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;
        public Button boton;
        public ImageView copiar,compartir;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.diccf2_palabra);
            textView2 = itemView.findViewById(R.id.diccf2_pronuciar);
            textView4 = itemView.findViewById(R.id.diccf2_palabra2);
            textView5 = itemView.findViewById(R.id.diccf2_pronuciar2);
            textView3 = itemView.findViewById(R.id.diccf2_contenidon);
            boton = itemView.findViewById(R.id.diccf2_button);
            copiar = itemView.findViewById(R.id.copy);
            compartir = itemView.findViewById(R.id.share);
        }
    }
}
