package com.example.atanke.lectura.client;

import static com.example.atanke.config.ConfigClient.Url;

import com.example.atanke.lectura.services.LecturaSesionService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LecturaSesionClient {

    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> true)
            .build();

    public static final String URL_BASE = Url;
    private static Retrofit retrofit;
    public static LecturaSesionService getApiService(){
     if(retrofit == null){
         retrofit = new  Retrofit.Builder()
                 .baseUrl(URL_BASE)
                 .client(okHttpClient)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
     }
        return retrofit.create(LecturaSesionService.class);
    }
}
