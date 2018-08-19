package com.imagine.bd.hayvenapp.Retrofit;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.imagine.bd.hayvenapp.Model.SigninResponse;
import com.imagine.bd.hayvenapp.Model.user;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * Created by User on 6/6/2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("hv_mobile/")
    Call<ResponseBody>  requestRoom(
            @Field("user_name") String name,
            @Field("room_id") String room
    );


    @POST("login")
    SigninResponse postJson(@Body user body);


    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json;charset=utf-8",
            "Cache-Control: max-age=640000"
    })

    @POST("login")
    Call<user> getUser(@Body String body);

    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json;charset=utf-8",
            "Cache-Control: max-age=640000"
    })


    @POST("login")
    Call<SigninResponse> getUser2(@Body String  body);
}

