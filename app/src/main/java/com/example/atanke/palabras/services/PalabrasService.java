package com.example.atanke.palabras.services;

import com.example.atanke.lectura.models.LecturaTitulosResponse;
import com.example.atanke.palabras.models.PalabrasResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PalabrasService {
    @GET("api/Libre/Palabras")
    Call<PalabrasResponse> getPalabras(
            //@Header("Authorization") String authToken,
            @Query("pagina") int pagina,
            @Query("idioma") int idioma,
            @Query("cantidad") int cantidad);
}