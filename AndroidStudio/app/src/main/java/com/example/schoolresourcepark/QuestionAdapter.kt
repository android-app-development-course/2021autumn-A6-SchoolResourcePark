package com.example.schoolresourcepark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(val questionList: List<Question>) :RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questiontitle: TextView = view.findViewById(R.id.questionTitle)
        val questioncount: TextView = view.findViewById(R.id.questionCount)
        val questiontime: TextView = view.findViewById(R.id.questionTime)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val question = questionList[position]
        holder.questiontitle.text = question.title
        holder.questioncount.text= question.count.toString()
        holder.questiontime.text = question.time

    }
    override fun getItemCount() = questionList.size
}