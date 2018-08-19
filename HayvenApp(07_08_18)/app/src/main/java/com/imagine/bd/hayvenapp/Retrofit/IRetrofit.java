package com.imagine.bd.hayvenapp.Retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IRetrofit {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("login")
    Call<JsonObject> postRawJSON(@Body JsonObject locationPost);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("personalConCreate")
    Call<JsonObject> getChathistory(@Body JsonObject locationPost);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("fcm-send")
    Call<JsonObject> sendCall(@Body JsonObject locationPost);
}