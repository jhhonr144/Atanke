package com.example.atanke.palabras.client;

import static com.example.atanke.config.ConfigClient.Url;

import com.example.atanke.lectura.services.Lectura10Service;
import com.example.atanke.palabras.services.PalabrasService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PalabrasClient {

    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> true)
            .build();

    public static final String URL_BASE = Url;
    private static Retrofit retrofit;
    public static PalabrasService getApiService(){
     if(retrofit == null){
         retrofit = new  Retrofit.Builder()
                 .baseUrl(URL_BASE)
                 .client(okHttpClient)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
     }
     return retrofit.create(PalabrasService.class);
    }
}
