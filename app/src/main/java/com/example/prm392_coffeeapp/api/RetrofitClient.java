package com.example.prm392_coffeeapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://calviv9yhz.g2.sqlite.cloud:8860/";

    private static Retrofit retrofit = null;

    public static SQLiteCloudService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit.create(SQLiteCloudService.class);
    }
}
