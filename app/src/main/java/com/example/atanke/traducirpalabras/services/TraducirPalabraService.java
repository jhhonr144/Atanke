package com.example.atanke.traducirpalabras.services;

import com.example.atanke.traducirpalabras.models.TraducirPalabraRequest;
import com.example.atanke.traducirpalabras.models.TraducirPalabraResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TraducirPalabraService {

    @FormUrlEncoded
    @POST("api/traducir")
    Call<TraducirPalabraResponse> getTraducir(@Field("data") String data);

}
