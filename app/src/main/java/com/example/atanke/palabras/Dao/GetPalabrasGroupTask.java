package com.example.atanke.palabras.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.BDLecturaDao;
import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.palabras.models.letraGruop;

import java.util.List;

public class GetPalabrasGroupTask extends AsyncTask<Void, Void, List<letraGruop>> {
    private BDPalabraDao palabras;
    public GetPalabrasGroupTask(BDPalabraDao LecturaDao) {
        this.palabras = LecturaDao;
    }
    @Override
    protected List<letraGruop> doInBackground(Void... voids) {
        return palabras.getGroup();
    }

}
