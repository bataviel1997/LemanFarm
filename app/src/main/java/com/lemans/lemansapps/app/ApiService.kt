package com.lemans.lemansapps.app


import com.lemans.lemansapps.model.ResponModel
import retrofit2.Call
import retrofit2.http.*


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

    @GET("provinsi")
    fun getProvinsi():Call<ResponModel>

    @GET("kota")
    fun getKota(
            @Query("id_provinsi") id: Int
    ):Call<ResponModel>

    @GET("kecamatan")
    fun getKecamatan(
        @Query("id_kota") id: Int
    ):Call<ResponModel>


}


