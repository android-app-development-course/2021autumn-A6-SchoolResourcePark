package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_question.*
import kotlinx.android.synthetic.main.activity_my_question.myQuestion
import kotlinx.android.synthetic.main.activity_my_reply.*
import kotlinx.android.synthetic.main.title.*

class MyReply : AppCompatActivity() {
    private val replyList=ArrayList<Reply>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reply)
        supportActionBar?.hide()
        titleText.setText("我的回答")
        initReply()
        val layoutManager= LinearLayoutManager(this)
        myReply.layoutManager=layoutManager
        val adapter=ReplyAdapter(replyList)
        myReply.adapter=adapter

        var titleBack=findViewById<ImageView>(R.id.titleBack)
        //点击返回个人中心
        titleBack.setOnClickListener{
            val intent = Intent(this, PersonalCenter::class.java)
            startActivity(intent)
        }

    }
    private fun initReply(){
        repeat(10){
            replyList.add(Reply("造成软件缺陷的原因","原因是。。。"))
        }
    }
}