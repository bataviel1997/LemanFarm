package com.lemans.lemansapps.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lemans.lemansapps.R
import com.lemans.lemansapps.app.ApiConfigAlamat
import com.lemans.lemansapps.helper.Helper
import com.lemans.lemansapps.model.Alamat
import com.lemans.lemansapps.model.ModelAlamat
import com.lemans.lemansapps.model.ResponModel
import com.lemans.lemansapps.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.toolbar_custom.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahAlamatActivity : AppCompatActivity() {

    var provinsi = ModelAlamat.Provinsi()
    var kota = ModelAlamat.Provinsi()
    var kecamatan = ModelAlamat()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)
        Helper().setToolbar(this, toolbar, "Tambah Alamat")

        mainButton()
        getProvinsi()
    }

    private fun mainButton(){
        btn_simpan.setOnClickListener {
            simpan()
        }
    }

    private fun simpan() {
        when {
            edt_nama.text.isEmpty() -> {
                error(edt_nama)
                return
            }
            edt_type.text.isEmpty() -> {
                error(edt_type)
                return
            }
            edt_phone.text.isEmpty() -> {
                error(edt_phone)
                return
            }
            edt_alamat.text.isEmpty() -> {
                error(edt_alamat)
                return
            }
            edt_kodePos.text.isEmpty() -> {
                error(edt_kodePos)
                return
            }
        }

        if (provinsi.province_id == "0"){
            toast("Silahkan Pilih Provinsi")
            return
        }
        if (kota.city_id == "0"){
            toast("Silahkan Pilih Kota")
            return
        }
        /*if(kecamatan.id == 0){
            toast("Silahkan Pilih Kecamatan")
            return
        }*/

        val alamat = Alamat()
        alamat.name = edt_nama.text.toString()
        alamat.type = edt_type.text.toString()
        alamat.phone = edt_phone.text.toString()
        alamat.alamat = edt_alamat.text.toString()
        alamat.kodepos = edt_kodePos.text.toString()

        alamat.id_provinsi = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.province
        alamat.id_kota = Integer.valueOf(kota.city_id)
        alamat.kota = kota.city_name
//        alamat.id_kecamatan = kecamatan.id
//        alamat.kecamatan = kecamatan.nama

        insert(alamat)
    }

    fun toast(string: String){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun error(editText: EditText){
        editText.error = " Kolom Tidak Boleh Kosong"
        editText.requestFocus()
    }


    private fun getProvinsi(){
        ApiConfigAlamat.instanceRetrofit.getProvinsi(ApiKey.key).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {

                    pb.visibility = View.GONE
                    div_provinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Provinsi")

                    val listProvinsi = res.provinsi
                    for (prov in listProvinsi) {
                        arryString.add(prov.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_provinsi.adapter = adapter
                    spn_provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                provinsi = listProvinsi[position - 1]
                                val idProv = listProvinsi[position - 1].id
                                getKota(idProv)
                            }
                        }
                    }

                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun getKota(id: Int) {
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKota(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {

                    pb.visibility = View.GONE
                    div_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.kota_kabupaten

                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kota")
                    for (prov in listArray) {
                        arryString.add(prov.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter
                    spn_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                kota = listArray[position - 1]
                                val idKota  = listArray[position - 1].id
                                Log.d("respon", "Provinsi id:" + idKota + " - " + " name:" + listArray[position - 1].nama)
                                getKecamatan(idKota)
                            }
                        }
                    }
                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun getKecamatan(id: Int) {
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful) {
                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE
                    val res = response.body()!!
                    val listArray = res.kecamatan

                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kecamatan")
                    for (data in listArray) {
                        arryString.add(data.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter
                    spn_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0) {
                                kecamatan = listArray[position - 1]
                            }
                        }
                    }
                } else {
                    Log.d("Error", "gagal memuat data:" + response.message())
                }
            }
        })
    }

    private fun insert(data: Alamat) {
        val myDb = MyDatabase.getInstance(this)!!
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().insert(data) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                toast("Insert data success")
                for (alamat in myDb.daoAlamat().getAll()) {
                    Log.d("Alamat", "nama:" + alamat.name + " - " + alamat.alamat + " - " + alamat.kota)
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}