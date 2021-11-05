package com.lemans.lemansapps.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.lemans.lemansapps.R
import com.lemans.lemansapps.helper.Helper
import com.lemans.lemansapps.model.Produk
import com.lemans.lemansapps.room.MyDatabase
import com.lemans.lemansapps.util.Config
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.item_produk.tv_harga
import kotlinx.android.synthetic.main.item_produk.tv_nama
import kotlinx.android.synthetic.main.toolbar_custom.*

class DetailProdukActivity : AppCompatActivity() {

    lateinit var produk: Produk
    lateinit var myDb: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)
        myDb = MyDatabase.getInstance(this)!!

        getInfo()
        mainButton()
        checkKeranjang()
    }

    private fun mainButton(){
        btn_keranjang.setOnClickListener{
            val data = myDb.daoKeranjang().getProduk(produk.id)

            if(data == null){
                insert()
            } else {
                data.jumlah += 1
                update(data)
            }
        }

        btn_favorit.setOnClickListener {
             // call database
            val listData = myDb.daoKeranjang().getAll() // get All data
            for(note :Produk in listData){
                println("-----------------------")
                println(note.name)
                println(note.harga)
            }
        }

        btn_tokeranjang.setOnClickListener {
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }

    private fun insert() {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkKeranjang()
                Log.d("respons", "data inserted")
                Toast.makeText(this, "Barang Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            })
    }

    private fun update(data : Produk ) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkKeranjang()
                    Log.d("respons", "data inserted")
                    Toast.makeText(this, "Barang Sudah ditambahkan", Toast.LENGTH_SHORT).show()
                })
    }

    private fun checkKeranjang() {
        val dataKeranjang = myDb.daoKeranjang().getAll()

        if(dataKeranjang.isNotEmpty()){
            div_angka.visibility = View.VISIBLE
            tv_angka.text = dataKeranjang.size.toString()
        }else{
            div_angka.visibility = View.GONE
        }
    }

    fun getInfo(){
        val data = intent.getStringExtra("extra")
        produk = Gson().fromJson<Produk>(data, Produk::class.java)

        // set value
        tv_nama.text = produk.name
        tv_harga.text = Helper().gantiRupiah(produk.harga)
        tv_deskripsi.text = produk.deskripsi

        val img =  Config.productUrl + produk.image
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .resize(400,400)
                .into(image)

        //toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = produk.name
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}