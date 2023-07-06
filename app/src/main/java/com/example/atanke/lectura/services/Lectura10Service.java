package com.example.atanke.lectura.services;

import com.example.atanke.lectura.models.LecturaTitulosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Lectura10Service {
    @GET("api/Lecturas")
    Call<LecturaTitulosResponse> getLecturaTitulos(
            @Query("cantidad") String cantidad);
}