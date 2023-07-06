package com.example.atanke.lectura.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.BDLecturaSesionDao;
import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;

import java.util.List;

public class GetLecturaSesionFk_lecturaTask extends AsyncTask<Void, Void, List<BDLecturaSesionDTO>> {

    private BDLecturaSesionDao lSesion;
    private String Id;
    public GetLecturaSesionFk_lecturaTask(BDLecturaSesionDao lSesion,String Id) {
        this.lSesion = lSesion;
        this.Id=Id;
    }
    @Override
    protected List<BDLecturaSesionDTO> doInBackground(Void... voids) {
        return lSesion.getSelect_fk_lectura(Id);
    }

}
