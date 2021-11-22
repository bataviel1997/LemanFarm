package com.lemans.lemansapps.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.lemans.lemansapps.model.User

class SharedPref(activity: Activity) {
    val login = "login"
    val nama = "nama"
    val phone = "phone"
    val email = "email"

    val user = "user"

    val mypref = "MAIN_PRF"
    val sp:SharedPreferences

    init {
        sp = activity.getSharedPreferences( mypref, Context.MODE_PRIVATE)

    }

    fun setStatusLogin(status:Boolean){
        sp.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin():Boolean{
        return sp.getBoolean(login, false)
    }
    // merubah data objek class kedalam bentuk string
    fun setUser(value: User) {
        val data: String = Gson().toJson(value, User::class.java)
        sp.edit().putString(user, data).apply()
    }
    // mengambil data user string,kalau data null kembali null
    fun getUser() : User? {
        val data: String = sp.getString(user, null ) ?: return null
        // merubah dari string ke dalam bentuk user objek
        return Gson().fromJson<User>(data, User::class.java)
    }

    fun setString(key: String, vlaue: String) {
        sp.edit().putString(key, vlaue).apply()
    }

    fun getString(key: String) : String {
        return sp.getString(key, "")!!
    }
    fun clear(){
        sp.edit().clear().apply()
    }

}