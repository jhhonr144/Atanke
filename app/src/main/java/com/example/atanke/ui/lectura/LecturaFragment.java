package com.example.atanke.ui.lectura;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.databinding.LecturaFragmentBinding;
import com.example.atanke.general.Dao.BDLecturaDao;
import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.dto.ConfigDTO;
import com.example.atanke.general.dto.api.lecturas.BDLecturaDTO;
import com.example.atanke.general.utils.ValidarFechas;
import com.example.atanke.lectura.Dao.ConfigGuardarAsyncTask;
import com.example.atanke.lectura.Dao.GetAllConfigTask;
import com.example.atanke.lectura.Dao.GetAllLecturaTituloTask;
import com.example.atanke.lectura.client.LecturaTituloClient;
import com.example.atanke.lectura.models.ItemLecturaAdapter;
import com.example.atanke.lectura.models.LecturaTitulosResponse;
import com.example.atanke.lectura.models.titulos;
import com.example.atanke.lectura.services.Lectura10Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturaFragment extends Fragment {

    private LecturaFragmentBinding binding;
    private RecyclerView recicle;
    private int  botonSelecionado=1;
    private ItemLecturaAdapter itemsRecicle;
    private ConfigDataBase db;
    private List<ConfigDTO> config;
    private Lectura10Service SLectura;
    private titulos t ;
private  TextView txtcantidadLecturas;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = LecturaFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = ConfigDataBase.getInstance(getContext());
        t= new titulos();
        txtcantidadLecturas= binding.txtCantidadl;

        recicle=binding.recicleP;
        try {
            CargarConfig();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Button button1 = binding.btnVdCuento;
        Button button2 = binding.btnVdLeyenda;
        Button button3 = binding.btnVdMitos;
        Button button4 = binding.btnVdTradiciones;
        Button button = binding.btnVdTodos;
        //$$? forzar actualizacion
        //llamas consultarLecturarTituloApi pero preguntale si te seguro antes
        setupButtonListeners(button1, button2, button3, button4,button);
        return root;
    }
    private void setupButtonListeners(Button... buttons) {
        for (Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_vd_cuento:
                            botonSelecionado = 1;
                            break;
                        case R.id.btn_vd_leyenda:
                            botonSelecionado = 2;
                            break;
                        case R.id.btn_vd_mitos:
                            botonSelecionado = 3;
                            break;
                        case R.id.btn_vd_tradiciones:
                            botonSelecionado = 4;
                            break;
                        default:
                            botonSelecionado=0;
                            break;
                    }
                cargarRecicle();
                }
            });
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CargarConfig() throws ExecutionException, InterruptedException {
        ConfigDao ConfigDao = db.configDao();
        GetAllConfigTask task = new GetAllConfigTask(ConfigDao);
        task.execute();
        config = task.get();
        boolean buscar=false;
        if(config.isEmpty()){
            buscar=true;
        }
        else{
            for(ConfigDTO confi: config){
                if(confi.getNombre().equalsIgnoreCase( "LecturaTitulos")){
                    ValidarFechas validate = new ValidarFechas();
                    //si ya pasaron 7 dia retorna yes
                    buscar=!validate.hanPasado7Dias(confi.getFecha());
                    break;
                }
            }
            buscar=!buscar;
        }
        if(buscar){
            consultarLecturarTituloApi();
        }
        else {
            cargarDatos();
        }
    }

    private void consultarLecturarTituloApi() {

        //$$MOSTRARCARGANDO$$
        //no hay datos descargado hay qe consultar a la web
        SLectura = LecturaTituloClient.getApiService();
        SLectura.getLecturaTitulos (
                        "Bearer 89|LdCRhHUy2wpp5JCHAMpgLen3HNkKJOu1BsLz3iHU",
                        "1000")
                .enqueue(new Callback<LecturaTitulosResponse>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<LecturaTitulosResponse> call,
                            Response<LecturaTitulosResponse> response) {
                        if (response.body() == null) {
                            Toast.makeText(getContext(), "No se puede consultar las lecturas, por favor pruebe despues", Toast.LENGTH_SHORT).show();
                        } else {
                            if(response.body().getDatos_len()==0)
                                Toast.makeText(getContext(), "Sin lecturas, por favor pruebe despues", Toast.LENGTH_SHORT).show();
                            else{
                                guardarLecturas(response.body().getDatos());
                                guardarConfig("LecturaTitulos",response.body().getDatos_len()+"",getContext());
                                try {
                                    cargarDatos();
                                } catch (ExecutionException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LecturaTitulosResponse> call, Throwable t) {
                        Toast.makeText(getContext(),
                                "No se puede conectar con el servidor, validad tu conecion a internet",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarLecturas(List<BDLecturaDTO> datos) {
        new GuardarLecturasTask().execute(datos);
    }

    private class GuardarLecturasTask extends AsyncTask<List<BDLecturaDTO>, Void, Void> {
        @Override
        protected Void doInBackground(List<BDLecturaDTO>... lecturas ) {
            BDLecturaDao BDLecturaTtituloDao = ConfigDataBase.getInstance(getContext()).BDLecturaDao();

            BDLecturaTtituloDao.deleteAll();
            for (BDLecturaDTO lectura : lecturas[0]) {
                BDLecturaTtituloDao.insert(lectura);
            }
            return null;
        }
    }

    private void guardarConfig(String nombre, String valor, Context context) {
        new ConfigGuardarAsyncTask(
                nombre,
                valor,
                context).execute();
    }

    private void cargarDatos() throws ExecutionException, InterruptedException {
        BDLecturaDao lectura = db.BDLecturaDao();
        GetAllLecturaTituloTask task = new GetAllLecturaTituloTask(lectura);
        task.execute();
        List<BDLecturaDTO> lecturas;
        lecturas =  task.get();
        for(BDLecturaDTO l : lecturas){
            titulos tt= new titulos();
            tt.foto=esVacio(l.getPortada().getMultimedia());
            tt.titulo=l.getNombre();
            tt.descripcion=l.getDescripcion();
            tt.categoria =l.getFk_tipo();
            tt.publico =l.getUser().getName();
            tt.id =l.getId();
            try {
                tt.fecha=l.getCreated_at().toString().split("T")[0];
            }
            catch (Exception e){
                try {
                    tt.fecha=l.getCreated_at().toString().split(" ")[0];
                }
                catch (Exception e1){
                    tt.fecha="...";
                }
            }
            switch (l.getFk_tipo()){
                case 1://cuentos
                    t.agregarCuento(tt);
                    break;

                case 2://Leyenda
                    t.agregarLeyenda(tt);
                    break;

                case 3://Mitos
                    tt.agregarMito(tt);
                    break;
                case 4://tradiciones
                    tt.agregarTradiciones(tt);
                    break;
            }
        }
        cargarRecicle();
    }

    private void cargarRecicle() {
        LinearLayoutManager layautManayer = new LinearLayoutManager(getContext());
        recicle.setLayoutManager(layautManayer);
        int cantidad=0;
        switch (botonSelecionado){
            case 1://cuentos
                itemsRecicle= new ItemLecturaAdapter(t.getListaCuento());
                cantidad=t.getListaCuento().size() ;
                break;

            case 2://Leyenda
                itemsRecicle= new ItemLecturaAdapter(t.getListaLeyenda());
                cantidad=t.getListaLeyenda().size() ;
                break;

            case 3://Mitos
                itemsRecicle= new ItemLecturaAdapter(t.getListaMito());
                cantidad=t.getListaMito().size( );
                break;
            case 4://tradiciones
                itemsRecicle= new ItemLecturaAdapter(t.getListaTradiciones());
                cantidad=t.getListaTradiciones().size( );
                break;
            default://Todos
                itemsRecicle= new ItemLecturaAdapter(t.getListaTodo());
                cantidad=t.getListaTodo().size();
                break;
        }
        recicle.setAdapter(itemsRecicle);
        //$$si cantidad es 0 muestra upp no hay nada :V
        txtcantidadLecturas.setText(String.valueOf(cantidad));
    }

    private String esVacio(String value) {
        if(value==null)
            return  null;
        if(value=="")
            return  null;
        if(value.length()<2)
            return  null;
        return  value;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}