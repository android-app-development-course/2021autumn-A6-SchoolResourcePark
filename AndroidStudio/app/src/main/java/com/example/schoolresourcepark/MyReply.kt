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
import kotlinx.android.synthetic.main.activity_my_question.myQuestion
import kotlinx.android.synthetic.main.activity_my_reply.*
import kotlinx.android.synthetic.main.comlistfragment1.*
import kotlinx.android.synthetic.main.title.*

class MyReply : AppCompatActivity() {
    private var uid:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reply)
        supportActionBar?.hide()
        titleText.setText("我的回答")

        val layoutManager= LinearLayoutManager(this)
        myReply.layoutManager=layoutManager
        uid=intent.getStringExtra("uid").toString()  //获取当前用户id
        getQue()
    }


    private fun getQue() {
        //数据查询
        var bmobQuery: BmobQuery<ReplyTable> = BmobQuery()
        bmobQuery.addWhereEqualTo("answerid", uid)
        bmobQuery.findObjects(object : FindListener<ReplyTable>() {
            override fun done(replys: MutableList<ReplyTable>?, ex: BmobException?) {
                val replyList = ArrayList<Reply>()
                if (ex == null) {
                    if (replys != null) {
                        for (reply: ReplyTable in replys) {
                            val replycontent = reply.replycontent.toString()
                            val quid = reply.qid.toString()
                            //根据qid找到问题的标题
                            var bmobQ: BmobQuery<ProblemTable> = BmobQuery()
                            bmobQ.findObjects(object : FindListener<ProblemTable>() {
                                override fun done(ques: MutableList<ProblemTable>?, ex: BmobException?) {
                                    if (ex == null) {
                                        if (ques != null) {
                                            for (que: ProblemTable in ques) {
                                                val id = que.objectId.toString()
                                                if(quid.equals(id)){
                                                    val title = que.qtitle.toString()
                                                    replyList.add(Reply(title, replycontent,que.objectId))
                                                    Log.d("list", replyList.toString())
                                                }
                                            }
                                            reply_count.setText(replyList.size.toString()+"条回答")
                                            val adapter=ReplyAdapter(replyList)
                                            myReply.adapter=adapter
                                        }
                                    }
                                }
                            })
                        }
                    }
                }
            }
        })
    }

    inner class ReplyAdapter(val replyList: List<Reply>) :
        RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val replytitle: TextView = view.findViewById(R.id.replyTitle)
            val replycontent: TextView = view.findViewById(R.id.replyContent)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.reply_item, parent, false)
            val holder=ViewHolder(view)
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
                val ques = replyList[position]
                val intent = Intent(parent.context, QuestionDetail::class.java)
                intent.putExtra("Rmyqid", ques.QID)
                intent.putExtra("Rmyuid", uid)
                intent.putExtra("Rfrom","my")
                startActivity(intent)
                //传数据，再跳转
            }
            return holder
        }
        override fun onBindViewHolder(holder: ReplyAdapter.ViewHolder, position: Int) {

            val reply = replyList[position]
            holder.replytitle.text = reply.title
            holder.replycontent.text = reply.content
        }
        override fun getItemCount() = replyList.size


    }
}