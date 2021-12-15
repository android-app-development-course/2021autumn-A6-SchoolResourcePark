package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        login.setOnClickListener{
            val intent = Intent(this, CommunityList::class.java)
            startActivity(intent)
        }

        bt_register.setOnClickListener{
            val intent = Intent(this, register::class.java)
            startActivity(intent)
        }
    }
}