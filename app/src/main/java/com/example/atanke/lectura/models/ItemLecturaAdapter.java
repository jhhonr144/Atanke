package com.example.atanke.lectura.models;

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
import com.example.atanke.ui.lectura.lectura_sessiones;

import java.util.List;

public class ItemLecturaAdapter extends RecyclerView.Adapter<ItemLecturaAdapter.ViewHolder> {
    private List<titulos> datos;
    public ItemLecturaAdapter(List<titulos> listaTodo) {
        datos=listaTodo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lectura_item_fragment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Asignar valores a las vistas del elemento
         /*  if(datos.get(position).foto==null)
       // holder.imageView.setImageResource(R.drawable.ic_baseline_auto_stories_24);
        else{
         String fotoUrl =Url +  "storage/img/Cuento/"+datos.get(position).foto;
            Picasso.get().load(fotoUrl)
                    .placeholder(R.drawable.ic_baseline_auto_stories_24)
                    .error(R.drawable.ic_baseline_close_24)
                    .into(holder.imageView);
        }
        */
        holder.textView1.setText(datos.get(position).titulo);
        holder.textView2.setText(datos.get(position).descripcion);
        holder.textView3.setText(datos.get(position).publico);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, lectura_sessiones.class);
                intent.putExtra("Titulo", datos.get(position).id+"");
                intent.putExtra("Nombre", datos.get(position).titulo);
                intent.putExtra("Autor", datos.get(position).publico);
                intent.putExtra("Fecha", datos.get(position).fecha);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datos==null?0:datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.txt_dic_itm_titulo);
            textView2 = itemView.findViewById(R.id.txt_dic_itm_descrip);
            textView3 = itemView.findViewById(R.id.txt_dic_itm_user);
        }
    }
}
