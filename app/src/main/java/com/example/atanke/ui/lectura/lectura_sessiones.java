package com.example.atanke.ui.lectura;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.BDLecturaSesionDao;
import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;
import com.example.atanke.lectura.Dao.GetLecturaSesionFk_lecturaTask;
import com.example.atanke.lectura.client.LecturaSesionClient;
import com.example.atanke.lectura.models.ItemLecturaSesionAdapter;
import com.example.atanke.lectura.models.LecturaSesionResponse;
import com.example.atanke.lectura.services.LecturaSesionService;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class lectura_sessiones extends AppCompatActivity {
    private String idTitulo;
    private RecyclerView recicle;
    private ItemLecturaSesionAdapter itemsRecicle;
    private ConfigDataBase db;
    private List<BDLecturaSesionDTO> listSesiones;
    private LecturaSesionService lsServicio;

    public TextView titulo,selecion,cantidad,fecha,autor;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_sessiones);
        Intent intent = getIntent();
        idTitulo= intent.getStringExtra("Titulo");
        if(idTitulo==null || idTitulo.equals("")){
             finish();
             return;
        }
        recicle=findViewById(R.id.recicle_session);
        titulo=findViewById(R.id.txt_s_titulo);
        selecion=findViewById(R.id.txt_s_seleciono);
        cantidad=findViewById(R.id.txt_s_cantida_sessiones);
        fecha=findViewById(R.id.txt_s_fechap);
        autor=findViewById(R.id.txt_s_cantida_sessiones);
        //$$mostrar un cargando mientra bisca la info
        try {
            CargarConfig();
        } catch (ExecutionException e) {
            //$$mostrar error y mandar para main principal
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CargarConfig() throws ExecutionException, InterruptedException {
        db = ConfigDataBase.getInstance(this);
        BDLecturaSesionDao lsesiondao= db.BDLecturaSesionDao();
        GetLecturaSesionFk_lecturaTask task = new GetLecturaSesionFk_lecturaTask(lsesiondao,idTitulo);
        task.execute();
        listSesiones = task.get();
        if(listSesiones.isEmpty()){
            cosultarSesionesApi();
        }
        else{
            cargarRecicle();
        }
    }

    private void cosultarSesionesApi()  {
        //$$MOSTRARCARGANDO$$
        //no hay datos descargado hay qe consultar a la web
        lsServicio = LecturaSesionClient.getApiService();
        lsServicio.getLecturaSesion(
            "Bearer 89|LdCRhHUy2wpp5JCHAMpgLen3HNkKJOu1BsLz3iHU",
            idTitulo)
            .enqueue(new Callback<LecturaSesionResponse>() {
                @Override
                public void onResponse(
                    @NonNull Call<LecturaSesionResponse> call,
                    Response<LecturaSesionResponse> response) {
                        if (response.body() == null)
                            Toast.makeText(getBaseContext(), "No se puede consultar la lectura, por favor pruebe despues", Toast.LENGTH_SHORT).show();
                        else {
                            if(response.body().getDatos_len()==0)
                                Toast.makeText(getBaseContext(), "Lecturas vacia, espere a que el admin publique informacion", Toast.LENGTH_SHORT).show();
                            else{
                                guardarLecturasSesiones(response.body().getDatos());
                                cargarRecicle();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LecturaSesionResponse> call, Throwable t) {
                        Toast.makeText(getBaseContext(),
                                "No se puede conectar con el servidor, validad tu conecion a internet",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void guardarLecturasSesiones(List<BDLecturaSesionDTO> datos) {
        new GuardarLecturasTask().execute(datos);
    }

    private class GuardarLecturasTask extends AsyncTask<List<BDLecturaSesionDTO>, Void, Void> {
        @Override
        protected Void doInBackground(List<BDLecturaSesionDTO>... Sesiones ) {
            BDLecturaSesionDao BDLecturaSesionDao = ConfigDataBase.getInstance(getBaseContext()).BDLecturaSesionDao();
            List<BDLecturaSesionDTO> sesiones = Sesiones[0];
            if (!sesiones.isEmpty()) {
                String fkLectura = sesiones.get(0).getFk_lectura()+"";
                BDLecturaSesionDao.deleteByfk_lectura(fkLectura);
                for (BDLecturaSesionDTO sesion : sesiones) {
                    BDLecturaSesionDao.insert(sesion);
                    /*for(BDLecturaContenidoDTO contenido :sesion.getContenido_lecturas()){
                        BDLecturaSesionDao.insertc(contenido);
                    }*/
                }
            }
            return null;
        }
    }

    private void cargarRecicle() {
        LinearLayoutManager layautManayer = new LinearLayoutManager(getBaseContext());
        recicle.setLayoutManager(layautManayer);
        itemsRecicle= new ItemLecturaSesionAdapter(listSesiones);
        recicle.setAdapter(itemsRecicle);
    }

}