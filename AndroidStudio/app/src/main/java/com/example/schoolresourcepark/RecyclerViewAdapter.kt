package com.example.schoolresourcepark

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import android.widget.TextView


    class RecyclerViewAdapter(val data: List<ResourceList>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: TextView = itemView.findViewById(com.example.schoolresourcepark.R.id.communityOption)
        val name: TextView = itemView.findViewById(com.example.schoolresourcepark.R.id.communityOption)
        val time: TextView = itemView.findViewById(com.example.schoolresourcepark.R.id.communityOption)
        val content: TextView = itemView.findViewById(com.example.schoolresourcepark.R.id.communityOption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int):ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.compagerfragment2, parent, false)
        return ViewHolder(view)
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        viewHolder.tv.text= data[position].toString()
        val fragment = data[position]
//        holder.name.text=fragment.name
//        holder.questioncount.text= question.count.toString()
//        holder.questiontime.text = question.time
//        holder.questiontime.text = question.time
    }
    override fun getItemCount(): Int {
        return data.size
    }


}