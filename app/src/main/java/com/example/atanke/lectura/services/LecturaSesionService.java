package com.example.atanke.lectura.services;

import com.example.atanke.lectura.models.LecturaSesionResponse;
import com.example.atanke.lectura.models.LecturaTitulosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LecturaSesionService {
    @GET("api/Sesiones")
    Call<LecturaSesionResponse> getLecturaSesion(
            //@Header("Authorization") String authToken,
            @Query("fk_lectura") String fk_lectura);
}