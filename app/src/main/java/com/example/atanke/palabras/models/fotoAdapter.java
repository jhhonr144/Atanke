package com.example.atanke.palabras.models;

import static com.example.atanke.config.ConfigClient.Url;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.api.palabras.BDPalabraDTO;
import com.example.atanke.general.dto.api.palabras.palabraMultiDTO;
import com.example.atanke.palabras.Dao.GetMultimeiasTask;
import com.example.atanke.palabras.Dao.GetPalabrasTask;
import com.example.atanke.sugerirtraduccion.Dao.GetAllUsuariosTask;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class fotoAdapter extends RecyclerView.Adapter<fotoAdapter.ViewHolder> {
    private int id;
    private ConfigDataBase db;
    private List<palabraMultiDTO> datos;
    public fotoAdapter(int idpalabra,List<palabraMultiDTO>  list) {
        id=idpalabra;
        datos=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_multimedia_foto, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nombre.setText(datos.get(position).getFk_palabra());

        //##metodo para guarda esta foto en base64 lecturarsesion 162
        String fotoUrl =Url +  "storage/img/Contenido/"+datos.get(position).getFk_multimedia();
        Picasso.get().load(fotoUrl)
                .placeholder(R.drawable.atanke)
                .into(holder.img);
    }
     @Override
    public int getItemCount() {
        return datos==null?0:datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.multi_nombre);
            img= itemView.findViewById(R.id.multi_foto);

        }
    }
}
