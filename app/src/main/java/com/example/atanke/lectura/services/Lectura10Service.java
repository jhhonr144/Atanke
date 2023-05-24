package com.example.atanke.lectura.services;

import com.example.atanke.lectura.models.LecturaTitulosResponse;

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Lectura10Service {
    @GET("api/Lecturas")
    Call<LecturaTitulosResponse> getLecturaTitulos(
            @Header("Authorization") String authToken,
            @Query("cantidad") String cantidad);
}