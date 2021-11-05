package com.lemans.lemansapps.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lemans.lemansapps.R
import com.lemans.lemansapps.helper.SharedPref
import kotlinx.android.synthetic.main.activity_masuk.*

class MasukActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        s = SharedPref(this)

            mainButton()

        }

        private fun mainButton() {
            btn_Login.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            btn_register.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))

            }

        }
}
