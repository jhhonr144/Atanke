package com.example.atanke.sugerirtraduccion.client;

import static com.example.atanke.config.ConfigClient.Url;

import com.example.atanke.traducirpalabras.services.TraducirPalabraService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SugerirTraduccionClient {

    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> true)
            .build();
    public static final String URL_BASE = Url;
    private static Retrofit retrofit;

    public static TraducirPalabraService getApiService(){
        if(retrofit == null){
            retrofit = new  Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TraducirPalabraService.class);
    }

}
