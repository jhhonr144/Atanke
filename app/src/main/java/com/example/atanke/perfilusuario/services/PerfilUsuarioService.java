package com.example.atanke.perfilusuario.services;

import com.example.atanke.perfilusuario.models.PerfilUsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PerfilUsuarioService {
    @GET("api/users")
    Call<PerfilUsuarioResponse> getPerfil(@Header("Authorization") String authToken);
}