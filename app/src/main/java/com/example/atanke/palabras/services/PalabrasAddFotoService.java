package com.example.atanke.palabras.services;

import com.example.atanke.palabras.models.PalabrasAddFotoResponse;
import com.example.atanke.palabras.models.addFotoRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PalabrasAddFotoService {
    @POST("api/Palabras/foto")
    Call<PalabrasAddFotoResponse> postAddFofo(
            @Header("Authorization") String authToken,
            @Body addFotoRequest body);
}