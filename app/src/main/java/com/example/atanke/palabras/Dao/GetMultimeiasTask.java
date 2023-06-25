package com.example.atanke.palabras.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.dto.api.palabras.palabraMultiDTO;

import java.util.List;

public class GetMultimeiasTask extends AsyncTask<Void, Void, List<palabraMultiDTO>> {

    private BDPalabraDao palabra;
    private int id=0;

    public GetMultimeiasTask(BDPalabraDao userDao,int elId) {
        this.palabra = userDao;
        this.id=elId;
    }
    @Override
    protected List<palabraMultiDTO> doInBackground(Void... voids) {
        return palabra.getMultiPalabra(id);
    }

}
