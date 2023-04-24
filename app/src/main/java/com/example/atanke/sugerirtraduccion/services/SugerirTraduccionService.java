package com.example.atanke.sugerirtraduccion.services;

import com.example.atanke.sugerirtraduccion.models.SugerirTraduccionRequest;
import com.example.atanke.sugerirtraduccion.models.SugerirTraduccionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SugerirTraduccionService {

    @POST("/api/sugerirTraduccion")
    Call<SugerirTraduccionResponse> registrarUsuario(@Body SugerirTraduccionRequest request);
}
