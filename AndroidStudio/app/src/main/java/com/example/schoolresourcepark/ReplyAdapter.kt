package com.example.schoolresourcepark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReplyAdapter(val replyList: List<Reply>) :
    RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val replytitle: TextView = view.findViewById(R.id.replyTitle)
        val replycontent: TextView = view.findViewById(R.id.replyContent)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reply_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ReplyAdapter.ViewHolder, position: Int) {

        val reply = replyList[position]
        holder.replytitle.text = reply.title
        holder.replycontent.text = reply.content
    }
    override fun getItemCount() = replyList.size
}