package com.example.atanke.lectura.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.BDLecturaSesionDao;
import com.example.atanke.general.dto.api.sesiones.BDLecturaContenidoDTO;
import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;

import java.util.List;

public class GetLecturaContenidoFk_sesionTask extends AsyncTask<Void, Void, List<BDLecturaContenidoDTO>> {

    private BDLecturaSesionDao lSesion;
    private String Id;
    public GetLecturaContenidoFk_sesionTask(BDLecturaSesionDao lSesion, String Id) {
        this.lSesion = lSesion;
        this.Id=Id;
    }
    @Override
    protected List<BDLecturaContenidoDTO> doInBackground(Void... voids) {
        return lSesion.getSelect_fk_sesion(Id);
    }

}
