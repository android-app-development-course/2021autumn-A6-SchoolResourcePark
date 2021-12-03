package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_resource.*
import kotlinx.android.synthetic.main.title.*

class MyResource : AppCompatActivity() {
    private val resourceList=ArrayList<Resource>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_resource)
        supportActionBar?.hide()
        titleText.setText("我的资源")

        initResource()
        val layoutManager=LinearLayoutManager(this)
        myResource.layoutManager=layoutManager
        val adapter=ResourceAdapter(resourceList)
        myResource.adapter=adapter

        var titleBack=findViewById<ImageView>(R.id.titleBack)
        //点击返回个人中心
        titleBack.setOnClickListener{
            val intent = Intent(this, PersonalCenter::class.java)
            startActivity(intent)
        }

    }
    private fun initResource(){
        repeat(10){
            resourceList.add(Resource("高数一复习资料","发布于2019-2-21"))
        }
    }
}