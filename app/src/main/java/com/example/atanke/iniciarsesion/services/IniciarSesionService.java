package com.example.atanke.iniciarsesion.services;

import com.example.atanke.iniciarsesion.models.IniciarSesionRequest;
import com.example.atanke.iniciarsesion.models.IniciarSesionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IniciarSesionService {
    @POST("/api/login")
    Call<IniciarSesionResponse> registrarUsuario(@Body IniciarSesionRequest request);
}
