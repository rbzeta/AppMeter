package app.rbzeta.applicationmeter.rest;

import app.rbzeta.applicationmeter.app.AppConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Robyn on 25/10/2016.
 */

public class ApiClient {
    public static final String BASE_URL = AppConfig.BASE_URL;
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
