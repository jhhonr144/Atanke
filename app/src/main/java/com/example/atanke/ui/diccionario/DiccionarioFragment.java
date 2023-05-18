package com.example.atanke.ui.diccionario;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.databinding.DiccionarioFragmentBinding;
import com.example.atanke.diccionario.Dao.GetAllConfigTask;
import com.example.atanke.diccionario.models.ItemLecturaAdapter;
import com.example.atanke.diccionario.models.titulos;
import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.ConfigDTO;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.sugerirtraduccion.Dao.GetAllUsuariosTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiccionarioFragment extends Fragment {

    private DiccionarioFragmentBinding binding;
    private RecyclerView recicle;
    private int  botonSelecionado=1;
    private ItemLecturaAdapter itemsRecicle;
    private ConfigDataBase db;
    private List<ConfigDTO> config;

    private titulos t= new titulos();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DiccionarioFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = ConfigDataBase.getInstance(getContext());
        try {
            CargarConfig();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        recicle=binding.recicleP;
        return root;
    }

    private void CargarConfig() throws ExecutionException, InterruptedException {
        ConfigDao ConfigDao = db.configDao();
        GetAllConfigTask task = new GetAllConfigTask("ConfigDao");
        task.execute();
        config = task.get();
        if(config.isEmpty()){
            //$$MOSTRARCARGANDO$$
            //no hay datos descargado hay qe consultar a la web

        }
        else{
            //valido si ya paso una semana para actualizar
        }
    }

    private void cargarDatos() {
        t.foto=null;
        t.titulo="prueba1";
        t.descripcion="para probara vista";
        t.categoria ="1";
        t.agregarCuento(t);
        switch (botonSelecionado){
            case 1:
                break;
        }
        LinearLayoutManager layautManayer = new LinearLayoutManager(getContext());
        recicle.setLayoutManager(layautManayer);
        itemsRecicle= new ItemLecturaAdapter(t.getListaTodo());
        recicle.setAdapter(itemsRecicle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}