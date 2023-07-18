package com.example.atanke.palabras.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.palabras.Dao.GetPalabrasTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LetrasGruopAdapter extends RecyclerView.Adapter<LetrasGruopAdapter.ViewHolder> {
    private List<letraGruop> datos;
    private ConfigDataBase db;
    public LetrasGruopAdapter(List<letraGruop> listaTodo) {
        datos=listaTodo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_diccionario_letras, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Asignar valores a las vistas del elemento
        holder.textView1.setText(datos.get(position).letra);
        holder.textView2.setText(datos.get(position).cantidad+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.recicle.getVisibility()==View.VISIBLE)
                    holder.recicle.setVisibility(View.GONE);
                else{
                    try {
                        cargarInformacionSegundoRecyclerView(
                                holder.recicle, v.getContext(), datos.get(position).letra);
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
    private void cargarInformacionSegundoRecyclerView(RecyclerView recyclerView, Context context,String id) throws ExecutionException, InterruptedException {
        db = ConfigDataBase.getInstance(context);
        List<palabrasRelacion> listapabra;
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
            PalabrasAdapter items  = new PalabrasAdapter(listapabra);
            recyclerView.setAdapter(items);
        }
    }
    @Override
    public int getItemCount() {
        return datos==null?0:datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;
        public RecyclerView recicle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.dicc_letra_letra);
            textView2 = itemView.findViewById(R.id.dicc_letra_cantidad);
            recicle = itemView.findViewById(R.id.dicc_letra_recicle);
        }
    }
}
