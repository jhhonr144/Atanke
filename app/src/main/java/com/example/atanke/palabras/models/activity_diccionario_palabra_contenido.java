package com.example.atanke.palabras.models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.dto.api.palabras.palabraMultiDTO;
import com.example.atanke.palabras.Dao.GetMultimeiasTask;
import com.example.atanke.sugerirtraduccion.Dao.GetAllUsuariosTask;
import com.example.atanke.ui.login.Login;
import com.example.atanke.ui.registrarse.Registrarse;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class activity_diccionario_palabra_contenido extends AppCompatActivity {

    private List<UsuarioDTO> usuarios;
    private ConfigDataBase db;
    private TextView txt_palabra,txt_traducion,txt_cantidad,txt_pronunciar,txt_sinContenido;
    private ImageView img_sinContenido;
    private RecyclerView recicle;
    private Button btn_agregar;
    palabrasRelacion objeto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diccionario_palabra_contenido);
        objeto= (palabrasRelacion) getIntent().getSerializableExtra("objeto");
        textos();
        botones();
        fragmet();
        db= ConfigDataBase.getInstance(this);
        buscarinfoBD();
        cargarDatos();
        try {
            cargarFotos();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private List<palabraMultiDTO> datosFoto;
    private void cargarFotos() throws ExecutionException, InterruptedException {
        BDPalabraDao palabra = db.BDPalabraDao();
        GetMultimeiasTask task = new GetMultimeiasTask(palabra, objeto.id);
        task.execute();
        datosFoto = task.get();
        if(datosFoto.isEmpty()){
            datosFoto=null;
        }

        LinearLayoutManager layautManayer = new LinearLayoutManager(getBaseContext());
        recicle.setLayoutManager(layautManayer);
        fotoAdapter items  = new fotoAdapter( objeto.id,datosFoto);
        recicle.setAdapter(items);
    }

    private void cargarDatos() {
        //lo basico que viene en el objecto
        txt_palabra.setText(objeto.palabra);
        txt_traducion.setText(objeto.palabra1);
        txt_cantidad.setText(objeto.count+"");
        txt_pronunciar.setText(objeto.pronunciar1);

        //los recicle consultado en bd

        if(objeto.count == 0){
            txt_sinContenido.setVisibility(View.VISIBLE);
            img_sinContenido.setVisibility(View.VISIBLE);
        }
    }

    private void buscarinfoBD() {
    }

    private void textos() {
        txt_palabra=this.findViewById(R.id.txtpalabra);
        txt_traducion=this.findViewById(R.id.txttraducion);
        txt_cantidad=this.findViewById(R.id.txtcantidad);
        txt_pronunciar=this.findViewById(R.id.pronunciacion);
        txt_sinContenido=this.findViewById(R.id.textView17);
        img_sinContenido=this.findViewById(R.id.imageView6);
    }
    private void botones() {
        btn_agregar=this.findViewById(R.id.btnagregar);
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    agregarContenidoAPalabre();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void agregarContenidoAPalabre() throws ExecutionException, InterruptedException {
        UsuarioDao userDao = db.usuarioDao();
        GetAllUsuariosTask task = new GetAllUsuariosTask(userDao);
        task.execute();
        usuarios = task.get();
        if(usuarios.isEmpty()){
            mensaje(this);
            return;
        }
        Intent intent = new Intent(this,dicicionario_agregar_contenido.class);
        intent.putExtra("objeto", objeto);
        startActivity(intent);
    }

    private void fragmet() {
    recicle = findViewById(R.id.recicle_foto);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Regresar a la actividad anterior
    }
    public void mensaje(Context context){
        new MaterialAlertDialogBuilder(context)
                .setTitle("¡Ups, parece que necesitas iniciar sesión!")
                .setMessage("Para acceder a esta función, debes iniciar sesión o crear una cuenta. ¡No te preocupes, es muy fácil!")
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);

                    }
                })
                .setNegativeButton("Registrarse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Registrarse.class);
                        context.startActivity(intent);
                    }
                })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}