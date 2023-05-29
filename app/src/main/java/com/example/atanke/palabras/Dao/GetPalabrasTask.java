package com.example.atanke.palabras.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.general.dto.api.palabras.BDPalabraDTO;

import java.util.List;

public class GetPalabrasTask extends AsyncTask<Void, Void, List<BDPalabraDTO>> {
    private BDPalabraDao palabras;
    private String letra="";
    public GetPalabrasTask(BDPalabraDao palabraDao,String letra) {
        this.letra=letra;
        this.palabras = palabraDao;
    }
    @Override
    protected List<BDPalabraDTO> doInBackground(Void... voids) {
        if(letra.equals(""))
            return palabras.getAllPalabras();
        return palabras.getPalabra(letra);
    }

}
