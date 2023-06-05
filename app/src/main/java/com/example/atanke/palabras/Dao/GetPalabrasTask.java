package com.example.atanke.palabras.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.general.dto.api.palabras.BDPalabraDTO;
import com.example.atanke.palabras.models.palabrasRelacion;

import java.util.List;

public class GetPalabrasTask extends AsyncTask<Void, Void, List<palabrasRelacion>> {
    private BDPalabraDao palabras;
    private String letra="";
    public GetPalabrasTask(BDPalabraDao palabraDao,String letra) {
        this.letra=letra;
        this.palabras = palabraDao;
    }
    @Override
    protected List<palabrasRelacion> doInBackground(Void... voids) {
        if(letra.equals(""))
            return palabras.getAllPalabras2();
        return palabras.getPalabra2(letra);
    }

}
