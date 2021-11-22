package com.lemans.lemansapps.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.lemans.lemansapps.R
import com.lemans.lemansapps.adapter.AdapterProduk
import com.lemans.lemansapps.adapter.AdapterSlider
import com.lemans.lemansapps.app.ApiConfig
import com.lemans.lemansapps.model.Produk
import com.lemans.lemansapps.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*
*
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
*/
class HomeFragment : Fragment() {

    lateinit var vpSlider: ViewPager
    lateinit var rvProduk:RecyclerView
    lateinit var rvProdukTerlaris:RecyclerView
    lateinit var rvElektronik:RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        getProduk()
        return view
    }

    fun displayProduk(){
        // Array Slider
        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider3)
//        Membuat layout produk menjadi horizontal
        val adapterSlider = AdapterSlider(arrSlider, activity)
        vpSlider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(activity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        rvProduk.adapter = AdapterProduk(requireActivity(), listProduk)
        rvProduk.layoutManager = layoutManager

        rvProdukTerlaris.adapter = AdapterProduk(requireActivity(), listProduk)
        rvProdukTerlaris.layoutManager = layoutManager2

        rvElektronik.adapter = AdapterProduk(requireActivity(), listProduk)
        rvElektronik.layoutManager = layoutManager3
    }


    private var listProduk:ArrayList<Produk> = ArrayList()
    fun getProduk(){
        ApiConfig.instanceRetrofit.getProduk().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if(res.success == 1){
                    val arrayProduk = ArrayList<Produk>()
                    for (p in res.produks){
                        p.discount = 3000
                        arrayProduk.add(p)
                    }
                    listProduk = arrayProduk
                    displayProduk()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    fun init(view: View ){
        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvProdukTerlaris = view.findViewById(R.id.rv_produkTerlaris)
        rvElektronik = view.findViewById(R.id.rv_elektronik)

    }

//    Produk Yang di tampilkan
//        val arrProduk: ArrayList<Produk>get(){
//            val arr = ArrayList<Produk>()
//            val p1 =  Produk()
//            p1.nama = "Hp Core i3"
//            p1.harga = "Rp.7.432.442"
//            p1.gambar = R.drawable.hp_14_bs749tu
//
//            val p2 =  Produk()
//            p2.nama = "Lenovo Core i3"
//            p2.harga = "Rp.5.555.333"
//            p2.gambar = R.drawable.hp_14_bs749tu
//
//            val p3 =  Produk()
//            p3.nama = "Asus Core i3"
//            p3.harga = "Rp.7.345.222"
//            p3.gambar = R.drawable.hp_14_bs749tu
//
//        arr.add(p1)
//        arr.add(p2)
//        arr.add(p3)
//
//
//            return arr
//        }
//
//        val arrElektronik: ArrayList<Produk>get(){
//            val arr = ArrayList<Produk>()
//            val p1 =  Produk()
//            p1.nama = "Hp Core i3"
//            p1.harga = "Rp.7.432.442"
//            p1.gambar = R.drawable.hp_14_bs749tu
//
//            val p2 =  Produk()
//            p2.nama = "Lenovo Core i3"
//            p2.harga = "Rp.5.555.333"
//            p2.gambar = R.drawable.hp_14_bs749tu
//
//            val p3 =  Produk()
//            p3.nama = "Asus Core i3"
//            p3.harga = "Rp.7.345.222"
//            p3.gambar = R.drawable.hp_14_bs749tu
//
//            arr.add(p1)
//            arr.add(p2)
//            arr.add(p3)
//
//
//            return arr
//        }
//
//        val arrProdukTerlaris: ArrayList<Produk>get(){
//            val arr = ArrayList<Produk>()
//            val p1 =  Produk()
//            p1.nama = "Hp Core i3"
//            p1.harga = "Rp.7.432.442"
//            p1.gambar = R.drawable.hp_14_bs749tu
//
//            val p2 =  Produk()
//            p2.nama = "Lenovo Core i3"
//            p2.harga = "Rp.5.555.333"
//            p2.gambar = R.drawable.hp_14_bs749tu
//
//            val p3 =  Produk()
//            p3.nama = "Asus Core i3"
//            p3.harga = "Rp.7.345.222"
//            p3.gambar = R.drawable.hp_14_bs749tu
//
//            arr.add(p1)
//            arr.add(p2)
//            arr.add(p3)
//
//
//            return arr
//        }

}

