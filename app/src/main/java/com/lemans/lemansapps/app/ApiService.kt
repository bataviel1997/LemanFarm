package com.lemans.lemansapps.app


import com.lemans.lemansapps.model.ResponModel
import com.lemans.lemansapps.model.rajaongkir.ResponOngkir
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

    @GET("province")
    fun getProvinsi(
        @Header("key") key: String,
    ):Call<ResponModel>

    @GET("city")
    fun getKota(
            @Header("key") key: String,
            @Query("province") id: String
    ):Call<ResponModel>

    @GET("kecamatan")
    fun getKecamatan(
        @Query("id_kota") id: Int
    ):Call<ResponModel>

    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
        @Header("key") key: String,
        @Field("origin") origin: String,
        @Field("destination") destination: String,
        @Field("weight") weight: Int,
        @Field("destination") courier: String
    ): Call<ResponOngkir>

}


