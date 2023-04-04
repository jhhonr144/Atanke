package com.example.atanke.registrarusuario.services;



import com.example.atanke.registrarusuario.models.RegistrarUsuarioRequest;
import com.example.atanke.registrarusuario.models.RegistrarUsuarioResponse;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistrarUsuarioService {

    @POST("/api/user/nuevo")
    Call<RegistrarUsuarioResponse> registrarUsuario(@Body RegistrarUsuarioRequest request);
}


