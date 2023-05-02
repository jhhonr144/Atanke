package com.example.atanke.iniciarsesion.dao;

import android.os.AsyncTask;

import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.iniciarsesion.models.IniciarSesionResponse;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.ui.login.Login;

import java.lang.ref.WeakReference;

import retrofit2.Response;

public class IniciarSesionAsyncTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<Login> iniciarWeakReference;
    private Response<IniciarSesionResponse> response;

    public IniciarSesionAsyncTask(Login iniciar, Response<IniciarSesionResponse> response) {
        iniciarWeakReference = new WeakReference<>(iniciar);
        this.response = response;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Login login = iniciarWeakReference.get();
        if (login == null) {
            return null;
        }
        ConfigDataBase db = ConfigDataBase.getInstance(login);
        UsuarioDao usuarioDao = db.usuarioDao();

        IniciarSesionResponse iniciar = response.body();
        assert iniciar != null;

        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(iniciar.getDatos().getId())
                .name(iniciar.getDatos().getName())
                .email(iniciar.getDatos().getEmail())
                .r_users_roles(iniciar.getDatos().getR_users_roles())
                .r_users_estados(iniciar.getDatos().getR_users_estados())
                .updated_at(iniciar.getDatos().getUpdated_at())
                .created_at(iniciar.getDatos().getCreated_at())
                .token(iniciar.getMensaje())
                .build();
        usuarioDao.insert(usuarioDTO);

        return null;
    }
}
