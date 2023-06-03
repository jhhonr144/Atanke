package com.example.atanke.lectura.models;

import static com.example.atanke.config.ConfigClient.Url;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.general.dto.api.sesiones.BDLecturaContenidoDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemLecturaContenidoAdapter extends RecyclerView.Adapter<ItemLecturaContenidoAdapter.ViewHolder> {
    private List<BDLecturaContenidoDTO>  datos;
    public ItemLecturaContenidoAdapter(List<BDLecturaContenidoDTO>  listaTodo) {
        datos=listaTodo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_lectura_contenidos, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //ViewGroup parent = (ViewGroup) holder.img.getParent();
        int  Width = 333;
        int  Height = 111;
        String fotoUrl="";
        LinearLayout.LayoutParams params;
        switch(datos.get(position).getFk_tipo() ){
            case 1://texto
                holder.texto.setText(datos.get(position).getContenido());
                holder.pVideo.setVisibility(View.GONE);
                holder.pImg.setVisibility(View.GONE);
                break;
            case 2://foto total
                holder.pVideo.setVisibility(View.GONE);
                holder.pTexto.setVisibility(View.GONE);
                fotoUrl =Url +  "storage/img/Contenido/"+datos.get(position).getContenido();
                params = new LinearLayout.LayoutParams(Width, Height);
                holder.img.setLayoutParams(params);
                Picasso.get().load(fotoUrl)
                        .placeholder(R.drawable.ic_baseline_auto_stories_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .into(holder.img);

                break;
            case 3://1/3 inicio
                holder.pTexto.setVisibility(View.GONE);
                holder.pVideo.setVisibility(View.GONE);
                fotoUrl =Url +  "storage/img/Contenido/"+datos.get(position).getContenido();
                Picasso.get().load(fotoUrl)
                        .placeholder(R.drawable.ic_baseline_auto_stories_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .into(holder.img);
                params = new LinearLayout.LayoutParams(Width/3, Height);
                params.gravity= Gravity.START;
                holder.img.setLayoutParams(params);
                break;
            case 4://1/3 centro
                holder.pTexto.setVisibility(View.GONE);
                holder.pVideo.setVisibility(View.GONE);
                fotoUrl =Url +  "storage/img/Contenido/"+datos.get(position).getContenido();
                Picasso.get().load(fotoUrl)
                        .placeholder(R.drawable.ic_baseline_auto_stories_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .into(holder.img);
                params = new LinearLayout.LayoutParams(Width/3, Height);
                params.gravity= Gravity.CENTER;
                holder.img.setLayoutParams(params);
                break;
            case 5://1/3 fin
                holder.pTexto.setVisibility(View.GONE);
                holder.pVideo.setVisibility(View.GONE);
                fotoUrl =Url +  "storage/img/Contenido/"+datos.get(position).getContenido();
                Picasso.get().load(fotoUrl)
                        .placeholder(R.drawable.ic_baseline_auto_stories_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .into(holder.img);
                params = new LinearLayout.LayoutParams(Width/3, Height);
                params.gravity= Gravity.END;
                holder.img.setLayoutParams(params);
                break;
            case 6://2/3 inicio
                holder.pTexto.setVisibility(View.GONE);
                holder.pVideo.setVisibility(View.GONE);
                fotoUrl =Url +  "storage/img/Contenido/"+datos.get(position).getContenido();
                Picasso.get().load(fotoUrl)
                        .placeholder(R.drawable.ic_baseline_auto_stories_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .into(holder.img);
                params = new LinearLayout.LayoutParams(2*Width/3, Height);
                params.gravity= Gravity.START;
                holder.img.setLayoutParams(params);
                break;
            case 7://2/3 fin
                holder.pTexto.setVisibility(View.GONE);
                holder.pVideo.setVisibility(View.GONE);
                fotoUrl =Url +  "storage/img/Contenido/"+datos.get(position).getContenido();
                Picasso.get().load(fotoUrl)
                        .placeholder(R.drawable.ic_baseline_auto_stories_24)
                        .error(R.drawable.ic_baseline_close_24)
                        .into(holder.img);
                params = new LinearLayout.LayoutParams(2*Width/3, Height);
                params.gravity= Gravity.END;
                holder.img.setLayoutParams(params);
                break;
            default:
                //$# pss toca mostrar video o fotos o videos
                holder.texto.setText("...");
                holder.pVideo.setVisibility(View.GONE);
                holder.pImg.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datos==null?0:datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView texto;
        public LinearLayout pTexto,pVideo,pImg;
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.esc_txt_texto);
            pTexto = itemView.findViewById(R.id.esc_texto);
            pImg = itemView.findViewById(R.id.esc_foto);
            img=itemView.findViewById(R.id.esc_img);
            pVideo = itemView.findViewById(R.id.esc_video);
        }
    }
}
