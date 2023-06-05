package com.example.atanke.ui.lectura;

import static com.example.atanke.general.utils.DialogBuilderDinamico.detenerAlertaCargando;
import static com.example.atanke.general.utils.ValidarFechas.convertDateTime;
import static com.example.atanke.general.utils.ValidarFechas.obtenerFechaActual;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.databinding.LecturaFragmentBinding;
import com.example.atanke.general.Dao.BDLecturaDao;
import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.dto.ConfigDTO;
import com.example.atanke.general.dto.api.lecturas.BDLecturaDTO;
import com.example.atanke.general.utils.DialogBuilderDinamico;
import com.example.atanke.general.utils.NetworkUtils;
import com.example.atanke.general.utils.ValidarFechas;
import com.example.atanke.lectura.Dao.ConfigGuardarAsyncTask;
import com.example.atanke.lectura.Dao.GetAllConfigTask;
import com.example.atanke.lectura.Dao.GetAllLecturaTituloTask;
import com.example.atanke.lectura.client.LecturaTituloClient;
import com.example.atanke.lectura.models.ItemLecturaAdapter;
import com.example.atanke.lectura.models.LecturaTitulosResponse;
import com.example.atanke.lectura.models.titulos;
import com.example.atanke.lectura.services.Lectura10Service;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturaFragment extends Fragment {

    private LecturaFragmentBinding binding;
    private RecyclerView recicle;
    private ConfigDataBase db;
    private titulos t ;
    private static final String KEY_FECHA = "fechaActual";
    private final String[] tabTitles = {"Todos", "Cuentos", "Leyendas", "Mitos", "Tradiciones"};
    private static final String PREF_NAME = "PHONE_DATA";
    private  TextView txtcantidadLecturas,fecha;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LecturaFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = ConfigDataBase.getInstance(getContext());
        t= new titulos();
        txtcantidadLecturas= binding.txtCantidadl;
        fecha = binding.txtFechaAct;
        TabLayout tabLayout = binding.tabLayout;

        for (String title : tabTitles) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(title);
            tabLayout.addTab(tab);
        }
        TabLayout.Tab defaultTab = tabLayout.getTabAt(0);
        if (defaultTab != null) {
            defaultTab.select();
        }
        recicle=binding.recicleP;
        LoadData();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                cargarRecicle(position);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return root;
    }

    private void LoadData(){
        boolean isInternetAvailable = NetworkUtils.isNetworkAvailable(requireContext());
        //$$ sin dtos pero con el icino on, se mete en el si
        if (isInternetAvailable) {
            //aqui no deberias validar que no halla pasado 7 dias
            //por qe que si no hay net, abajo valida para consultar, ....
            //$$
            consultarLecturarTituloApi();
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_FECHA, obtenerFechaActual());
            editor.apply();
            fecha.setText(obtenerFechaActual());
        } else {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CargarConfig();
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                    fecha.setText(sharedPreferences.getString(KEY_FECHA, ""));
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CargarConfig() throws ExecutionException, InterruptedException {
        ConfigDao ConfigDao = db.configDao();
        GetAllConfigTask task = new GetAllConfigTask(ConfigDao);
        task.execute();
        List<ConfigDTO> config = task.get();
        boolean buscar=false;
        if(config.isEmpty()){
            buscar=true;
        }
        else{
            for(ConfigDTO confi: config){
                if(confi.getNombre().equalsIgnoreCase( "LecturaTitulos")){
                    ValidarFechas validate = new ValidarFechas();
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
        DialogBuilderDinamico.alertaCargando(getContext(),"¡Casi listo! Cargando información");
        Lectura10Service SLectura = LecturaTituloClient.getApiService();
        SLectura.getLecturaTitulos ("1000")
                .enqueue(new Callback<LecturaTitulosResponse>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<LecturaTitulosResponse> call,
                            Response<LecturaTitulosResponse> response) {
                        detenerAlertaCargando();
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
                                } catch (ExecutionException | InterruptedException e) {
                                    Toast.makeText(getContext(), "Error al procesar la lista nueva",Toast.LENGTH_SHORT);
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LecturaTitulosResponse> call, Throwable t) {
                        detenerAlertaCargando();
                        Toast.makeText(getContext(),
                                "No se puede conectar con el servidor, buscando en datos locales",
                                Toast.LENGTH_SHORT).show();
                        try {
                            cargarDatos();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
            tt.publico =l.getAuthor()==null?"":l.getAuthor();
            tt.id =l.getId();
            //$$esta es la fecha que sufre cambio la lectura, en teoria nunca es vacias
            //asi que solo pon a validar el update
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (l.getCreated_at() != null) {
                    tt.fecha = convertDateTime(l.getCreated_at());
                } else if (l.getUpdated_at() != null) {
                    tt.fecha = convertDateTime(l.getUpdated_at());
                }else{
                    tt.fecha = obtenerFechaActual();
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
                    t.agregarMito(tt);
                    break;
                case 4://tradiciones
                    t.agregarTradiciones(tt);
                    break;
            }
        }
        cargarRecicle(0);
    }

    private void cargarRecicle(int botonSelecionado) {
        LinearLayoutManager layautManayer = new LinearLayoutManager(getContext());
        recicle.setLayoutManager(layautManayer);
        int cantidad=0;
        ItemLecturaAdapter itemsRecicle;
        switch (botonSelecionado){
            case 1://cuentos
                itemsRecicle = new ItemLecturaAdapter(getContext(),t.getListaCuento());
                cantidad=t.getListaCuento().size() ;
                break;
            case 2://Leyenda
                itemsRecicle = new ItemLecturaAdapter(getContext(),t.getListaLeyenda());
                cantidad=t.getListaLeyenda().size() ;
                break;
            case 3://Mitos
                itemsRecicle = new ItemLecturaAdapter(getContext(),t.getListaMito());
                cantidad=t.getListaMito().size( );
                break;
            case 4://tradiciones
                itemsRecicle = new ItemLecturaAdapter(getContext(),t.getListaTradiciones());
                cantidad=t.getListaTradiciones().size( );
                break;
            default://Todos
                itemsRecicle = new ItemLecturaAdapter(getContext(),t.getListaTodo());
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