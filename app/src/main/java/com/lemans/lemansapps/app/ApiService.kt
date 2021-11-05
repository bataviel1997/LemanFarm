package com.lemans.lemansapps.app


import com.lemans.lemansapps.model.ResponModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @FormUrlEncoded
    @POST("register")  //http://192.168.100.8/lemansweb/public/api/
    fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") phone: String,
            @Field("password") password: String
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String
    ): Call<ResponModel>


    @GET("produk")
    fun getProduk():Call<ResponModel>


}


