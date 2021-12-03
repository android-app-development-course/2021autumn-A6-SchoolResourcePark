package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.title.*

class myInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        supportActionBar?.hide()
        titleText.setText("我的信息")

        var changepw=findViewById<Button>(R.id.bt_changepw)
        var titleBack=findViewById<ImageView>(R.id.titleBack)
        //点击进入修改密码界面
        changepw.setOnClickListener{
            val intent = Intent(this, passwordChange::class.java)
            startActivity(intent)
        }
        //点击返回个人中心
        titleBack.setOnClickListener{
            val intent = Intent(this, PersonalCenter::class.java)
            startActivity(intent)
        }
    }
}