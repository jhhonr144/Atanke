package com.example.atanke.lectura.Dao;

import android.content.Context;
import android.os.AsyncTask;

import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.dto.ConfigDTO;
import com.example.atanke.ui.registrarse.Registrarse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ConfigGuardarAsyncTask extends AsyncTask<Void, Void, Void> {

    private String nombre;
    private String valor;
    private Context thiss;
    public ConfigGuardarAsyncTask(String nombre,String valor,Context contexto ) {
        this.nombre=nombre;
        this.valor=valor;
        this.thiss=contexto;
    }


    @Override
    protected Void doInBackground(Void... voids) {
            ConfigDao configDao = ConfigDataBase.getInstance(thiss).configDao();
            // Eliminar el objeto ConfigDTO con el mismo nombre (si existe)
            configDao.deleteByNombre(nombre);
            // Obtener la fecha actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechaActual = sdf.format(new Date());
            // Crear una nueva instancia de ConfigDTO
            ConfigDTO configDTO = new ConfigDTO();
            configDTO.setNombre(nombre);
            configDTO.setInfo(valor);
            configDTO.setFecha(fechaActual);
            // Insertar el objeto ConfigDTO en la base de datos
            configDao.insert(configDTO);
        return null;
    }
}