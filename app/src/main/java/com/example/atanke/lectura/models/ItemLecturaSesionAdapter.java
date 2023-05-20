package com.example.atanke.lectura.models;

import static com.example.atanke.config.ConfigClient.Url;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;
import com.example.atanke.ui.lectura.lectura_sessiones;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemLecturaSesionAdapter extends RecyclerView.Adapter<ItemLecturaSesionAdapter.ViewHolder> {
    private List<BDLecturaSesionDTO>  datos;
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
        // Asignar valores a las vistas del elemento
        holder.textView1.setText(datos.get(position).getNombre());
        holder.textView2.setText(datos.get(position).getContenidos());
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, lectura_sessiones.class);
                intent.putExtra("Titulo", datos.get(position).id+"");
                intent.putExtra("Id", position);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return datos==null?0:datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.txt_ls_titulo);
            textView2 = itemView.findViewById(R.id.txt_ls_contenido);
        }
    }
}
