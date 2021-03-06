@file:Suppress("KDocUnresolvedReference")

package com.lemans.lemansapps.fragment

//import com.lemans.lemansapps.activity.PengirimanActivity
//import com.lemans.lemansapps.activity.PengirimanActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lemans.lemansapps.R
import com.lemans.lemansapps.activity.PengirimanActivity
import com.lemans.lemansapps.adapter.AdapterKeranjang
import com.lemans.lemansapps.helper.Helper
import com.lemans.lemansapps.model.Produk
import com.lemans.lemansapps.room.MyDatabase


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KeranjangFragment : Fragment() {
    lateinit var myDb : MyDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //dipanggil sekali ketika aktiviti aktiv

        val view:View = inflater.inflate(R.layout.fragment_keranjang, container, false)
        init(view)
        myDb = MyDatabase.getInstance(requireActivity())!!
        mainButton()
        return view
    }

    lateinit var adapter : AdapterKeranjang
    var listProduk = ArrayList<Produk>()
    private fun displayProduk() {
        listProduk = myDb.daoKeranjang().getAll() as ArrayList
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = AdapterKeranjang(requireActivity(), listProduk, object : AdapterKeranjang.Listeners{
            override fun onUpdate() {
                hitungTotal()
            }
            override fun onDelete(position: Int) {
                listProduk.removeAt(position)
                adapter.notifyDataSetChanged()
                hitungTotal()
            }
        })
        rvProduk.adapter = adapter
        rvProduk.layoutManager = layoutManager
    }

    fun hitungTotal() {

        val listProduk = myDb.daoKeranjang().getAll() as ArrayList
        var totalHarga = 0
        var isSelectedAll = true
        for( produk in listProduk){
            if (produk.selected) {
                val harga = Integer.valueOf(produk.harga)
                totalHarga += (harga* produk.jumlah)
            } else {
                isSelectedAll = false
            }
        }

        cbAll.isChecked = isSelectedAll
        tvTotal.text = Helper().gantiRupiah(totalHarga)
    }

    private fun mainButton() {
        btnDelete.setOnClickListener{

        }
        btnBayar.setOnClickListener {
            startActivity(Intent(requireActivity(), PengirimanActivity::class.java))

        }
        cbAll.setOnClickListener {
            for (i in listProduk.indices){
             val produk = listProduk[i]
                produk.selected = cbAll.isChecked
                listProduk[i] = produk
            }
            adapter.notifyDataSetChanged()
        }
    }

    lateinit var btnDelete: ImageView
    lateinit var rvProduk: RecyclerView
    lateinit var tvTotal: TextView
    lateinit var btnBayar: TextView
    lateinit var cbAll: CheckBox
    private fun init(view: View) {

        btnDelete = view.findViewById(R.id.btn_delete)
        rvProduk = view.findViewById(R.id.rv_produk)
        tvTotal = view.findViewById(R.id.tv_total)
        btnBayar = view.findViewById(R.id.btn_bayar)
        cbAll = view.findViewById(R.id.cb_all)
        }

    override fun onResume() {
        displayProduk()
        hitungTotal()
        super.onResume()
    }
}
