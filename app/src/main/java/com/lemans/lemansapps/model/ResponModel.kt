package com.lemans.lemansapps.model

class ResponModel {
    var success = 0
    lateinit var message:String
    var user = User()
    var produks: ArrayList<Produk> = ArrayList()
}