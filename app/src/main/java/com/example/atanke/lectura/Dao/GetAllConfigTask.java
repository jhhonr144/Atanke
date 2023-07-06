package com.example.atanke.lectura.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.dto.ConfigDTO;

import java.util.List;

public class GetAllConfigTask extends AsyncTask<Void, Void, List<ConfigDTO>> {

    private ConfigDao configDao;
    public GetAllConfigTask(ConfigDao configDao) {
        this.configDao = configDao;
    }
    @Override
    protected List<ConfigDTO> doInBackground(Void... voids) {
        return configDao.getAllconfig();
    }

}
