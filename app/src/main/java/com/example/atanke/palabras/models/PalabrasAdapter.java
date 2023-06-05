package com.example.atanke.palabras.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
        holder.textView1.setText(datos.get(position).palabra);
        holder.textView2.setText(datos.get(position).pronunciar);
        holder.textView4.setText(datos.get(position).palabra1);
        holder.textView5.setText(datos.get(position).pronunciar1);
        holder.textView3.setText(datos.get(position).count+"");
        if(datos.get(position).count==0)
            holder.boton.setText("Agregar");
        holder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.recicle.getVisibility()==View.VISIBLE)
                    holder.recicle.setVisibility(View.GONE);
                else{
                    try {
                        cargarInformacionSegundoRecyclerView(holder.recicle, v.getContext(),datos.get(position).count);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    holder.recicle.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void cargarInformacionSegundoRecyclerView(RecyclerView recyclerView, Context context,int cantidad) throws ExecutionException, InterruptedException {
       /*
        db = ConfigDataBase.getInstance(context);
        List<BDPalabraDTO> listapabra;
        BDPalabraDao palabraDao= db.BDPalabraDao();
        GetPalabrasTask task = new GetPalabrasTask(palabraDao,id+"");
        task.execute();
        listapabra = task.get();
        if(listapabra.isEmpty()){
            Toast.makeText(context, "No hay Informacion visible", Toast.LENGTH_SHORT).show();
        }
        else {
            LinearLayoutManager layautManayer = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layautManayer);
            ItemLecturaContenidoAdapter items  = new ItemLecturaContenidoAdapter(listapabra);
            recyclerView.setAdapter(items);
        }

        */
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
        public RecyclerView recicle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.diccf2_palabra);
            textView2 = itemView.findViewById(R.id.diccf2_pronuciar);
            textView4 = itemView.findViewById(R.id.diccf2_palabra2);
            textView5 = itemView.findViewById(R.id.diccf2_pronuciar2);
            textView3 = itemView.findViewById(R.id.diccf2_contenidon);
            recicle = itemView.findViewById(R.id.diccf2__recicle);
            boton = itemView.findViewById(R.id.diccf2_button);
        }
    }
}
