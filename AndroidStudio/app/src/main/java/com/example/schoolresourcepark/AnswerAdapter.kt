package com.example.schoolresourcepark

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_login.*

//class AnswerAdapter(val answerList: List<Answer>)  :
//    RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {
//        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val name: TextView = view.findViewById(R.id.name)
//            val head: ImageView = view.findViewById(R.id.head)
//            val time: TextView = view.findViewById(R.id.time)
//            val answerImage: ImageView=view.findViewById(R.id.answerImage)
//            val answerText: TextView = view.findViewById(R.id.answerText)
//        }
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.answer_list_item, parent, false)
//            return ViewHolder(view)
//        }
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//            val answer = answerList[position]
//            holder.name.text = answer.name
////            holder.head.setImageResource(answer.head)
//            holder.time.text=answer.time
//            holder.answerImage.setImageResource(answer.answerImage)
//            holder.answerText.text=answer.answerText
//
//        }
//        override fun getItemCount() = answerList.size
//}