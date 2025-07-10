package com.example.prm392_coffeeapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SQLiteCloudService {
    @GET("coffeeapp.sqlite")
    Call<String> runQuery(
            @Query("apikey") String apiKey,
            @Query("query") String query
    );
}

