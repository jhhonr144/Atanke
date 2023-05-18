package com.example.atanke.diccionario.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import com.example.atanke.R;

import java.util.List;

public class ItemLecturaAdapter extends RecyclerView.Adapter<ItemLecturaAdapter.ViewHolder> {
    private List<titulos> datos;
    public ItemLecturaAdapter(List<titulos> listaTodo) {
        datos=listaTodo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diccionario_item_fragment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Asignar valores a las vistas del elemento
        if(datos.get(position).foto==null)
        holder.imageView.setImageResource(R.drawable.ic_baseline_auto_stories_24);
        else{
            String fotoUrl = datos.get(position).foto;
            Picasso.get().load(fotoUrl).placeholder(R.drawable.ic_baseline_auto_stories_24).into(holder.imageView);
        }
        holder.textView1.setText(datos.get(position).titulo);
        holder.textView2.setText(datos.get(position).descripcion);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.dic_itm_img);
            textView1 = itemView.findViewById(R.id.txt_dic_itm_titulo);
            textView2 = itemView.findViewById(R.id.txt_dic_itm_descrip);
        }
    }
}
