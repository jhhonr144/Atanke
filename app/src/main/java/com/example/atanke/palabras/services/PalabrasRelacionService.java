package com.example.atanke.palabras.services;

import com.example.atanke.palabras.models.PalabrasRelacionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
public interface PalabrasRelacionService {
    @GET("api/LecturasRela")
    Call<PalabrasRelacionResponse> getPalabrasRelacion();
}