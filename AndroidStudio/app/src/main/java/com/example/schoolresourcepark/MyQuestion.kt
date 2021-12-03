package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_question.*
import kotlinx.android.synthetic.main.title.*


class MyQuestion : AppCompatActivity() {
    private val questionList=ArrayList<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_question)
        supportActionBar?.hide()
        titleText.setText("我的提问")

        initQuestion()
        val layoutManager= LinearLayoutManager(this)
        myQuestion.layoutManager=layoutManager
        val adapter=QuestionAdapter(questionList)
        myQuestion.adapter=adapter

        var titleBack=findViewById<ImageView>(R.id.titleBack)
        //点击返回个人中心
        titleBack.setOnClickListener{
            val intent = Intent(this, PersonalCenter::class.java)
            startActivity(intent)
        }
    }
    private fun initQuestion(){
        repeat(10){
            questionList.add(Question("Linux进程通信",0,"2019-2-21"))
        }
    }
}