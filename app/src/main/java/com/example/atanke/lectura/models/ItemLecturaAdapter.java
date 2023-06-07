package com.example.atanke.lectura.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.ui.lectura.lectura_sessiones;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemLecturaAdapter extends RecyclerView.Adapter<ItemLecturaAdapter.ViewHolder> {
    private List<titulos> datos;
    private Random random;
    private int[] pastelColors;
    private Context context;
    public ItemLecturaAdapter(Context context,List<titulos> listaTodo) {
        this.context = context;
        datos=listaTodo;
        random = new Random();
        pastelColors = new int[] {
                R.color.pastelColor1,
                R.color.pastelColor2,
                R.color.pastelColor3,
                R.color.pastelColor4,
                R.color.pastelColor5,
                R.color.pastelColor6,
                R.color.pastelColor7,
                R.color.pastelColor12,
                R.color.pastelColor13,
                R.color.pastelColor14,
                R.color.pastelColor15,
                R.color.pastelColor16,
                R.color.pastelColor17
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lectura_item_fragment, parent, false);
        int randomColor = parent.getContext().getResources().getColor(pastelColors[random.nextInt(pastelColors.length)]);

        CardView cardView = itemView.findViewById(R.id.cardView);
        ImageView imageView = itemView.findViewById(R.id.imageView5) ;
        Drawable randomImageDrawable = getRandomImage(randomColor);
        imageView.setImageDrawable(randomImageDrawable);
        cardView.setCardBackgroundColor(randomColor);
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
        holder.textView2.setText(datos.get(position).publico);
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
                intent.putExtra("Portada", datos.get(position).portada);
                context.startActivity(intent);

               Log.i("Titulo", datos.get(position).id+"");
               Log.i("Nombre", datos.get(position).titulo);
               Log.i("Autor", datos.get(position).publico);
               Log.i("Fecha", datos.get(position).fecha);
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

    private Drawable getRandomImage(int backgroundColor) {
        Context context = this.context;
        int[] imageResources = new int[] {
                R.drawable.ic_montana,
                R.drawable.ic_nube,
                R.drawable.ic_arbusto,
                R.drawable.ic_baseline_gesture_24,
                R.drawable.ic_baseline_tsunami_24,
                R.drawable.ic_baseline_wb_sunny_24,
                R.drawable.ic_baseline_forest_24,
                R.drawable.ic_baseline_local_florist_24,
        };
        int darkBackgroundColor = darkenColor(backgroundColor);

        List<Drawable> filteredImages = new ArrayList<>();
        for (int imageResource : imageResources) {
            Drawable drawable = AppCompatResources.getDrawable(context, imageResource);
            if (drawable != null) {
                drawable.setTint(darkBackgroundColor);
                filteredImages.add(drawable);
            }
        }

        if (!filteredImages.isEmpty()) {
            int randomIndex = random.nextInt(filteredImages.size());
            return filteredImages.get(randomIndex);
        }
        return AppCompatResources.getDrawable(context, androidx.transition.R.drawable.abc_btn_check_material);
    }

    private int darkenColor(int color) {
        float factor = 0.95f; 
        int alpha = Color.alpha(color);
        int red = Math.max((int) (Color.red(color) * factor), 0);
        int green = Math.max((int) (Color.green(color) * factor), 0);
        int blue = Math.max((int) (Color.blue(color) * factor), 0);
        return Color.argb(alpha, red, green, blue);
    }
}
