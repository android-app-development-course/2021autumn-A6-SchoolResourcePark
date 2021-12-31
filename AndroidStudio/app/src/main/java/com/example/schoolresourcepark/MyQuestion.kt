package com.example.schoolresourcepark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import kotlinx.android.synthetic.main.activity_my_question.*
import kotlinx.android.synthetic.main.activity_my_resource.*
import kotlinx.android.synthetic.main.title.*


class MyQuestion : AppCompatActivity() {
    private val questionList=ArrayList<Question>()
    var uid=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_question)
        supportActionBar?.hide()
        titleText.setText("我的提问")

        //initQuestion()
        val layoutManager= LinearLayoutManager(this)
        myQuestion.layoutManager=layoutManager
//        val adapter=QuestionAdapter(questionList)
//        myQuestion.adapter=adapter

//        var titleBack=findViewById<ImageView>(R.id.titleBack)
//        //点击返回个人中心
//        titleBack.setOnClickListener{
//            val intent = Intent(this, PersonalCenter::class.java)
//            startActivity(intent)
//        }
        uid=intent.getStringExtra("uid").toString()
        getQue()

    }

    private fun getQue() {
        //数据查询
        var bmobQuery: BmobQuery<ProblemTable> = BmobQuery()
        bmobQuery.addWhereEqualTo("askid", uid)
        bmobQuery.findObjects(object : FindListener<ProblemTable>() {
            override fun done(pros: MutableList<ProblemTable>?, ex: BmobException?) {
                //val replyList = ArrayList<Reply>()
                if (ex == null) {
//                Toast.makeText(parentFragment?.context, "查询成功", Toast.LENGTH_LONG).show()
                    if (pros != null) {
                        for (pro: ProblemTable in pros) {
//                            val cname=pro.cname.toString()
                            var title = pro.qtitle.toString()
                            var count=pro.comecount
                            var time = pro.createdAt.substringBefore(" ").toString()
                            val id=pro.objectId
                            questionList.add(Question(title, count,time,id))
                            Log.d("pro", questionList.size.toString())
                            question_Count.setText(questionList.size.toString()+"条提问")
                        }
                    }
                } else {
                    Log.d("pro", "失败")

//                Toast.makeText(parentFragment?.context, ex.message, Toast.LENGTH_LONG).show()
                }
                val adapter=QuestionAdapter(questionList)
                myQuestion.adapter=adapter
            }

        })
    }
    private fun initQuestion(){
        repeat(10){
            questionList.add(Question("Linux进程通信",0,"2019-2-21",""))
        }
    }

    inner class QuestionAdapter(val questionList: List<Question>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val questiontitle: TextView = view.findViewById(R.id.questionTitle)
            val questioncount: TextView = view.findViewById(R.id.questionCount)
            val questiontime: TextView = view.findViewById(R.id.questionTime)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
            val holder=ViewHolder(view)
            holder.itemView.setOnClickListener{
                val position = holder.adapterPosition
                val ques = questionList[position]
                val intent = Intent(parent.context, QuestionDetail::class.java)
                intent.putExtra("myrid", ques.QID)
                intent.putExtra("myuid", uid)
                intent.putExtra("from","my")
                startActivity(intent)
            }
            return holder
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val question = questionList[position]
            holder.questiontitle.text = question.title
            holder.questioncount.text= question.count.toString()
            holder.questiontime.text = question.time

        }
        override fun getItemCount() = questionList.size
    }
}