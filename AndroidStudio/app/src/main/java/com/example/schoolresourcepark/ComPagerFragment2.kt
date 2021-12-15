package com.example.schoolresourcepark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.R
import android.content.Intent
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import java.util.ArrayList

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.compagerfragment1.*
import kotlinx.android.synthetic.main.compagerfragment2.*


class ComPagerFragment2: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.schoolresourcepark.R.layout.compagerfragment2, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager=LinearLayoutManager(activity)
        comPangerFrag2.layoutManager=layoutManager
        val adapter=CPFragAdapter2(getQue())
        comPangerFrag2.adapter=adapter
    }

    private fun getQue():List<ComResource>{
        val queList=ArrayList<ComResource>()
        repeat(10){
            queList.add(ComResource(1,"发布者","3min前","Android 开发自我学习笔记和思维导图，欢迎大家下载学习"))
        }
        return queList
    }

    inner class CPFragAdapter2(val ComResList: List<ComResource>):
        RecyclerView.Adapter<CPFragAdapter2.ViewHolder>(){
        inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
            //val QueImg: TextView =view.findViewById(com.example.schoolresourcepark.R.id.QueImg)
            val ResName: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ResName)
            val ResTime: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ResTime)
            val ResContent: TextView =view.findViewById(com.example.schoolresourcepark.R.id.ResContent)
        }
        override fun onCreateViewHolder(parent: ViewGroup,view:Int): ViewHolder {
            val view =LayoutInflater.from(parent.context).inflate(com.example.schoolresourcepark.R.layout.comrecycler2_item,parent,false)
            val holder=ViewHolder(view)
        holder.itemView.setOnClickListener{
//            val que=ComQueList[holder.adapterPosition]
//                NewsContentActivity.actionStart(parent.context,que.title,que.content)

            val intent= Intent(parent.context,ResourceDetail::class.java)
            startActivity(intent)
        }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val que=ComResList[position]
//        holder.QueImg.text=que.img
            holder.ResName.text=que.name
            holder.ResTime.text=que.time
            holder.ResContent.text=que.content
        }

        override fun getItemCount()=ComResList.size
    }
}