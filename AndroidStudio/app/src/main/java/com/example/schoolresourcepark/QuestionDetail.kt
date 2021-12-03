package com.example.schoolresourcepark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_question_detail.*

class QuestionDetail : AppCompatActivity() {

    private val answerList=ArrayList<Answer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        initAnswers()
        val layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        val adapter=AnswerAdapter(answerList)
        recyclerView.adapter=adapter


    }
    private fun initAnswers(){
        repeat(10){
            answerList.add(Answer("回答者",R.drawable.head, "3min前",R.drawable.answer,"利用position：fixed 固定定位"))
        }
    }
}