package com.example.atanke.lectura.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.BDLecturaDao;
import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.dto.ConfigDTO;
import com.example.atanke.general.dto.api.lecturas.BDLecturaDTO;

import java.util.List;

public class GetAllLecturaTituloTask extends AsyncTask<Void, Void, List<BDLecturaDTO>> {

    private ConfigDao configDao;
    private BDLecturaDao LecturaDao;
    public GetAllLecturaTituloTask(BDLecturaDao LecturaDao) {
        this.LecturaDao = LecturaDao;
    }
    @Override
    protected List<BDLecturaDTO> doInBackground(Void... voids) {
        return LecturaDao.getAllLectura_titulo();
    }

}
