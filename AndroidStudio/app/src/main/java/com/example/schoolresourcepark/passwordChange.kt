package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_password_change.*
import kotlinx.android.synthetic.main.title.*

class passwordChange : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_change)

        supportActionBar?.hide()
        titleText.setText("密码修改")

        bt_changepw.setOnClickListener{
            val intent = Intent(this, myInfo::class.java)
            startActivity(intent)
        }

    }
}