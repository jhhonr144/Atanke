package com.example.atanke.sugerirtraduccion.Dao;

import android.os.AsyncTask;

import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.Dao.UsuarioDao;

import java.util.List;

public class GetAllUsuariosTask extends AsyncTask<Void, Void, List<UsuarioDTO>> {

    private UsuarioDao userDao;

    public GetAllUsuariosTask(UsuarioDao userDao) {
        this.userDao = userDao;
    }
    @Override
    protected List<UsuarioDTO> doInBackground(Void... voids) {
        return userDao.getAllUsers();
    }
}
