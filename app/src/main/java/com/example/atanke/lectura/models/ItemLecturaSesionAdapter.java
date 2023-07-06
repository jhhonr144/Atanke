package com.example.atanke.lectura.models;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.atanke.general.Dao.BDLecturaSesionDao;
import com.example.atanke.general.dto.api.sesiones.BDLecturaContenidoDTO;
import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;
import com.example.atanke.lectura.Dao.GetLecturaContenidoFk_sesionTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemLecturaSesionAdapter extends RecyclerView.Adapter<ItemLecturaSesionAdapter.ViewHolder> {
    private List<BDLecturaSesionDTO>  datos;
    private ConfigDataBase db;
    public ItemLecturaSesionAdapter(List<BDLecturaSesionDTO>  listaTodo) {
        datos=listaTodo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_lectura_sesion, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Context context = holder.itemView.getContext();

       if(datos.get(position).getNombre().equalsIgnoreCase( "default")){
           holder.textView1.setVisibility(View.GONE);
       }else{
           holder.textView1.setVisibility(View.VISIBLE);
           holder.textView1.setText(datos.get(position).getNombre());
       }
       holder.textView2.setText(datos.get(position).getContenidos()+ "");
                    try {
                        cargarInformacionSegundoRecyclerView(holder.reciclec, context,datos.get(position).getId());
                    } catch (ExecutionException e) {
                        Toast.makeText(context, "error ilca.java cath1", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        Toast.makeText(context, "error ilca.java cath2", Toast.LENGTH_SHORT).show();
                    }
                    holder.reciclec.setVisibility(View.VISIBLE);
            }

    @Override
    public int getItemCount() {
        return datos==null?0:datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;
        public RecyclerView reciclec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.txt_ls_titulo);
            textView2 = itemView.findViewById(R.id.txt_ls_contenido);
            reciclec = itemView.findViewById(R.id.recliclecontenido);

        }
    }

    private void cargarInformacionSegundoRecyclerView(RecyclerView recyclerView, Context context,int id) throws ExecutionException, InterruptedException {
        db = ConfigDataBase.getInstance(context);
        List<BDLecturaContenidoDTO> listContenido;
        BDLecturaSesionDao lsesiondao= db.BDLecturaSesionDao();
        GetLecturaContenidoFk_sesionTask task = new GetLecturaContenidoFk_sesionTask(lsesiondao,id+"");
        task.execute();

        SharedPreferences sharedPreferences = context.getSharedPreferences("DATOS_LECTURAS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ArrayList<String> contenidosSet = new ArrayList<>();


        listContenido = task.get();
        if(listContenido.isEmpty()){
            editor.putStringSet("contenidos", new HashSet<>(contenidosSet));
        }
        else {

            for (BDLecturaContenidoDTO contenidoDTO: listContenido){
                if(contenidoDTO.getTipo_contenido().getNombre().equalsIgnoreCase("Texto")){
                    String contenido = contenidoDTO.getContenido();
                    contenidosSet.add(contenido);
                }
            }
            editor.putStringSet("KEY_CONTENIDO", new HashSet<>(contenidosSet));
            editor.apply();

            LinearLayoutManager layautManayer = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layautManayer);
            ItemLecturaContenidoAdapter items  = new ItemLecturaContenidoAdapter(listContenido);
            recyclerView.setAdapter(items);

        }
    }

}
