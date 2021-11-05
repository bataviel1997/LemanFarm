package com.lemans.lemansapps.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lemans.lemansapps.R
import com.lemans.lemansapps.activity.DetailProdukActivity
import com.lemans.lemansapps.helper.Helper
import com.lemans.lemansapps.model.Produk
import com.lemans.lemansapps.util.Config
import com.squareup.picasso.Picasso
import java.util.*

class AdapterProduk(var activity: Activity, var data: ArrayList<Produk>): RecyclerView.Adapter<AdapterProduk.Holder>() {
//    Untuk memanggil nama id pada view
        class Holder(view: View): RecyclerView.ViewHolder(view){
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    //Menampilkan banyak data yang akan di tampilkan
    override fun getItemCount(): Int {
        return  data.size
    }
//    Menampilkan logika gambar,harga dan nama barang
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().gantiRupiah(data[position].harga)
//        holder.imgProduk.setImageResource( data[position].image)

     val image = Config.productUrl + data[position].image
    Picasso.get()
            .load(image)
            .placeholder(R.drawable.product)
            .error(R.drawable.product)
            .into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val activiti = Intent(activity, DetailProdukActivity::class.java)
            val str = Gson().toJson( data[position], Produk::class.java)
            activiti.putExtra("extra",str)
            activity.startActivity(activiti)

        }

    }

}