package com.example.atanke.registrarusuario.Dao;

import android.os.AsyncTask;

import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.registrarusuario.models.RegistrarUsuarioResponse;
import com.example.atanke.ui.registrarse.Registrarse;

import java.lang.ref.WeakReference;

import retrofit2.Response;

public class GuardarSesionAsyncTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<Registrarse> registrarseWeakReference;
    private Response<RegistrarUsuarioResponse> response;

    public GuardarSesionAsyncTask(Registrarse registrarse, Response<RegistrarUsuarioResponse> response) {
        registrarseWeakReference = new WeakReference<>(registrarse);
        this.response = response;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Registrarse registrarse = registrarseWeakReference.get();
        if (registrarse == null) {
            return null;
        }


        ConfigDataBase db = ConfigDataBase.getInstance(registrarse);
        UsuarioDao usuarioDao = db.usuarioDao();

        RegistrarUsuarioResponse registrarUsuarioResponse = response.body();

        assert registrarUsuarioResponse != null;
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(registrarUsuarioResponse.getDatos().getId())
                .name(registrarUsuarioResponse.getDatos().getName())
                .email(registrarUsuarioResponse.getDatos().getEmail())
                .r_users_roles(registrarUsuarioResponse.getDatos().getR_users_roles())
                .r_users_estados(registrarUsuarioResponse.getDatos().getR_users_estados())
                .updated_at(registrarUsuarioResponse.getDatos().getUpdated_at())
                .created_at(registrarUsuarioResponse.getDatos().getCreated_at())
                .token(registrarUsuarioResponse.getMensaje())
                .build();
        usuarioDao.insert(usuarioDTO);

        return null;
    }
}