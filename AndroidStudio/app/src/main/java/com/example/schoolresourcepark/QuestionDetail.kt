package com.example.schoolresourcepark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolresourcepark.register
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.title.*

class QuestionDetail : AppCompatActivity() {

    private val answerList=ArrayList<Answer>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        supportActionBar?.hide()
        titleText.setText("问题详情")
        initAnswers()
        val layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        val adapter=AnswerAdapter(answerList)
        recyclerView.adapter=adapter
        answerbutton.setOnClickListener {
            val intent = Intent(this, AnswerActivity::class.java)
            startActivity(intent)
        }


    }
    private fun initAnswers(){
        repeat(10){
            answerList.add(Answer("回答者",R.drawable.head, "3min前",R.drawable.answer,"利用position：fixed 固定定位"))
        }
    }
}