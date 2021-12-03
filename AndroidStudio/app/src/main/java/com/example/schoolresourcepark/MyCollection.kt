package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_collection.*
import kotlinx.android.synthetic.main.title.*

class MyCollection : AppCompatActivity() {
    private val collectionList=ArrayList<Collection>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_collection)

        supportActionBar?.hide()
        titleText.setText("我的收藏")

        initCollection()
        val layoutManager= LinearLayoutManager(this)
        myCollection.layoutManager=layoutManager
        val adapter=CollectionAdapter(collectionList)
        myCollection.adapter=adapter

        var titleBack=findViewById<ImageView>(R.id.titleBack)
        //点击返回个人中心
        titleBack.setOnClickListener{
            val intent = Intent(this, PersonalCenter::class.java)
            startActivity(intent)
        }
    }
    private fun initCollection(){
        repeat(10){
            collectionList.add(Collection("Linux进程通信","发布于xxx"))
        }
    }
}