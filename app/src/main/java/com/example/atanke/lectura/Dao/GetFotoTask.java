package com.example.atanke.lectura.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.BDLecturaSesionDao;
import com.example.atanke.general.dto.api.lecturas.AfotosDTO;

public class GetFotoTask extends AsyncTask<Void, Void, AfotosDTO> {

    private BDLecturaSesionDao lSesion;
    private String url;
    public GetFotoTask(BDLecturaSesionDao lSesion, String url) {
        this.lSesion = lSesion;
        this.url=url;
    }
    @Override
    protected AfotosDTO doInBackground(Void... voids) {
        return lSesion.getSelect_foto(url);
    }

}
