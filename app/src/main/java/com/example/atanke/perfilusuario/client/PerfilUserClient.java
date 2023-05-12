package com.example.atanke.perfilusuario.client;

import static com.example.atanke.config.ConfigClient.Url;

import com.example.atanke.perfilusuario.services.PerfilUsuarioService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfilUserClient {

    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> true)
            .build();

    public static final String URL_BASE = Url;
    private static Retrofit retrofit;
    public static PerfilUsuarioService getApiService(){
     if(retrofit == null){
         retrofit = new  Retrofit.Builder()
                 .baseUrl(URL_BASE)
                 .client(okHttpClient)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
     }
     return retrofit.create(PerfilUsuarioService.class);
    }
}
