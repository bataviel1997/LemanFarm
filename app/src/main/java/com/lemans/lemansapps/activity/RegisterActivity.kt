package com.lemans.lemansapps.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lemans.lemansapps.MainActivity
import com.lemans.lemansapps.R
import com.lemans.lemansapps.app.ApiConfig
import com.lemans.lemansapps.helper.SharedPref
import com.lemans.lemansapps.model.ResponModel
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        s = SharedPref(this)

        btn_register.setOnClickListener{
                register()
        }

        btn_google.setOnClickListener {
            dataDummy()
        }
    }

    fun dataDummy() {
        edt_nama.setText("Batavi")
        edt_email.setText("Batavi@gmail.com")
        edt_phone.setText("09847583945")
        edt_password.setText("12345678")
    }


    fun register() {
        if(edt_nama.text.isEmpty()) {
            edt_nama.error = " Kolom Nama Tidak Boleh Kosong"
            edt_nama.requestFocus()
            return
        } else if(edt_email.text.isEmpty()) {
            edt_email.error = " Kolom Email Tidak Boleh Kosong"
            edt_email.requestFocus()
            return
        } else if(edt_phone.text.isEmpty()) {
            edt_phone.error = " Kolom Nomor Telepon Tidak Boleh Kosong"
            edt_phone.requestFocus()
            return
        } else if(edt_password.text.isEmpty()) {
            edt_password.error = " Kolom Password Tidak Boleh Kosong"
            edt_password.requestFocus()
            return
        }

        pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(edt_nama.text.toString(), edt_email.text.toString(), edt_phone.text.toString(), edt_password.text.toString()).enqueue(object : Callback<ResponModel>{

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb.visibility = View.GONE
                val respon = response.body()!!

                if (respon.success == 1 ) {
                    //berhasil
                    s.setStatusLogin(true)
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK) // untuk distroy activity sebelum nya
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, "Selamat Datang "+respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error:"+respon.message, Toast.LENGTH_SHORT).show()

                }
            }

        })

    }
}