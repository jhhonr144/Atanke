package com.example.atanke.ui.diccionario;

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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
 
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.databinding.DiccionarioFragmentBinding;
import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.dto.ConfigDTO;
import com.example.atanke.general.dto.api.palabras.BDPalabraDTO;
import com.example.atanke.general.dto.api.palabras.BDPalabraRelacionDTO;
import com.example.atanke.general.dto.api.palabras.MultimediaDTO;
import com.example.atanke.general.utils.NetworkUtils;
import com.example.atanke.general.utils.ValidarFechas;
import com.example.atanke.lectura.Dao.ConfigGuardarAsyncTask;
import com.example.atanke.lectura.Dao.GetAllConfigTask;
import com.example.atanke.palabras.Dao.GetPalabrasGroupTask;
import com.example.atanke.palabras.client.PalabrasClient;
import com.example.atanke.palabras.client.PalabrasRelacionClient;
import com.example.atanke.palabras.models.LetrasGruopAdapter;
import com.example.atanke.palabras.models.PalabrasRelacionResponse;
import com.example.atanke.palabras.models.PalabrasResponse;
import com.example.atanke.palabras.models.letraGruop;
import com.example.atanke.palabras.services.PalabrasRelacionService;
import com.example.atanke.palabras.services.PalabrasService;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiccionarioFragment extends Fragment {
    private DiccionarioFragmentBinding binding;
    private RecyclerView recicle;
    private LetrasGruopAdapter itemsRecicle;
    private TextView txt_fechaActualizacion;
    private ConfigDataBase db;
    private List<ConfigDTO> config;
    private PalabrasService Servi;
    private PalabrasRelacionService Servi2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.diccionario_fragment, container, false);        
        binding = DiccionarioFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = ConfigDataBase.getInstance(getContext());
        recicle= binding.recicleGruop;
        ConfigText();
        ConfigBoton();
        cargarConfig();
        return root;
    }
private void cargarConfig(){
    boolean isInternetAvailable = NetworkUtils.isNetworkAvailable(requireContext());
    if (isInternetAvailable) {
        consultarPalabrasApi(0);
    } else {
        try {
            cargarDatos();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void cargarConfig__VIEJO() throws ExecutionException, InterruptedException {
        ConfigDao ConfigDao = db.configDao();
        GetAllConfigTask task = new GetAllConfigTask(ConfigDao);
        task.execute();
        config = task.get();
        boolean buscar=false;
        int cantidadActualPalabras=-1;
        if(config.isEmpty()){
            buscar=true;
        }
        else{
            for(ConfigDTO confi: config){
                if(confi.getNombre().equalsIgnoreCase( "Palabras")){
                    ValidarFechas validate = new ValidarFechas();
                    //si ya pasaron 7 dia retorna yes
                    buscar=!validate.hanPasado7Dias(confi.getFecha());
                    txt_fechaActualizacion.setText(confi.getFecha());
                    cantidadActualPalabras=Integer.parseInt(confi.getInfo());
                    break;
                }
            }
            buscar=!buscar;
        }
        if(buscar){
            consultarPalabrasApi(cantidadActualPalabras);
        }
        else {
            cargarDatos();
        }
    }

    private void cargarDatos()  throws ExecutionException, InterruptedException {
        BDPalabraDao palabraGroup = db.BDPalabraDao();
        GetPalabrasGroupTask task=new GetPalabrasGroupTask(palabraGroup);
        task.execute();
        List<letraGruop> grupoLetra;
        grupoLetra =  task.get();
        cargarRecicle(grupoLetra);
    }
    private void cargarRecicle(List<letraGruop> grupoLetra) {
        LinearLayoutManager layautManayer = new LinearLayoutManager(getContext());
        recicle.setLayoutManager(layautManayer);
        itemsRecicle= new LetrasGruopAdapter(grupoLetra);
        recicle.setAdapter(itemsRecicle);
    } 
    private void ConfigBoton() { } 
    private void ConfigText() {
        txt_fechaActualizacion=binding.diccf1TxtFecha;
    } 
    private void consultarPalabrasApi(int cantidadActualPalabras) {
        //$$MOSTRARCARGANDO$$
        //no hay datos descargado hay qe consultar a la web
        Servi = PalabrasClient.getApiService();
        Servi.getPalabras ( 1,0,1000)
                .enqueue(new Callback<PalabrasResponse>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<PalabrasResponse> call,
                            Response<PalabrasResponse> response) {
                        if (response.body() == null) {
                            Toast.makeText(getContext(), "No se puede consultar las palabras, por favor pruebe despues", Toast.LENGTH_SHORT).show();
                        } else {
                            if(response.body().getDatos_len()==0)
                                Toast.makeText(getContext(), "Sin palabras, por favor pruebe despues", Toast.LENGTH_SHORT).show();
                            else{
                                if(response.body().getDatos_len()!=cantidadActualPalabras )
                                    guardarPalabras(response.body().getDatos());
                                guardarConfig("Palabras", response.body().getDatos_len() + "", getContext());
                                cargarRelaciones();
                                try {
                                    cargarDatos();
                                } catch (ExecutionException e) {
                                    Toast.makeText(getContext(), "Error al Listar la informacion", Toast.LENGTH_SHORT).show();
                                } catch (InterruptedException e) {
                                    Toast.makeText(getContext(), "Error al Listar la informacion", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<PalabrasResponse> call, Throwable t) {
                        Toast.makeText(getContext(),
                                "No se puede conectar con el servidor, validad tu conecion a internet",
                                Toast.LENGTH_SHORT).show();
                        try {
                            cargarDatos();
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private void cargarRelaciones() {
        Servi2 = PalabrasRelacionClient.getApiService();
        Servi2.getPalabrasRelacion()
                .enqueue(new Callback<PalabrasRelacionResponse>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<PalabrasRelacionResponse> call,
                            Response<PalabrasRelacionResponse> response) {
                        if (response.body() == null) {
                            Toast.makeText(getContext(), "No se puede consultar las traduciones de las palabras, por favor pruebe despues", Toast.LENGTH_SHORT).show();
                        } else {
                            if(response.body().getDatos_len()==0) {
                                Toast.makeText(getContext(), "Sin  traduciones de palabras, por favor pruebe despues", Toast.LENGTH_SHORT).show();
                            }else {
                                guardarPalabrasRelacion(response.body().getDatos());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<PalabrasRelacionResponse> call, Throwable t) {
                        Toast.makeText(getContext(),
                            "No se puede conectar con el servidor, validad tu conecion a internet",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarPalabrasRelacion(List<BDPalabraRelacionDTO> datos) {
        new DiccionarioFragment.GuardarPalabrasRelacionTask().execute(datos);
    }
    private class GuardarPalabrasRelacionTask extends AsyncTask<List<BDPalabraRelacionDTO>, Void, Void> {
        @Override
        protected Void doInBackground(List<BDPalabraRelacionDTO>... palabras ) {
            BDPalabraDao BDPalabraDao = ConfigDataBase.getInstance(getContext()).BDPalabraDao();
            BDPalabraDao.dellAllRelacion();
            for (BDPalabraRelacionDTO relacion : palabras[0]) {
                BDPalabraDao.insertr(relacion);
            }
            return null;
        }
    }

    private void guardarPalabras(List<BDPalabraDTO> datos) {
        new DiccionarioFragment.GuardarPalabrasTask().execute(datos);
    }
    private class GuardarPalabrasTask extends AsyncTask<List<BDPalabraDTO>, Void, Void> {
        @Override
        protected Void doInBackground(List<BDPalabraDTO>... palabras ) {
            BDPalabraDao BDPalabraDao = ConfigDataBase.getInstance(getContext()).BDPalabraDao();
            BDPalabraDao.dellAll();
            BDPalabraDao.dellpm();
            for (BDPalabraDTO palabra : palabras[0]) {
                palabra.setLetra(palabra.getPalabra().substring(0,1).toUpperCase());
                BDPalabraDao.insert(palabra);
                for(MultimediaDTO contenido :palabra.getMultimedia()){
                    BDPalabraDao.insertm(contenido);
                    BDPalabraDao.insertpm(contenido.getPivot());
                }
            }
            return null;
        }
    }
    private void guardarConfig(String nombre, String valor, Context context) {
        new ConfigGuardarAsyncTask(
                nombre,
                valor,
                context).execute();
        txt_fechaActualizacion.setText("Hoy");
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}